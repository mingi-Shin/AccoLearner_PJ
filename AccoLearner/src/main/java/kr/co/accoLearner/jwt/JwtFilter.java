package kr.co.accoLearner.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import kr.co.accoLearner.dto.Role;
import kr.co.accoLearner.dto.UserDTO;

/**
 * JWT 검증 담당
 * 로그인 이후 모든 요청 : 토큰 검증 및 SecurityContextHolder세팅, stateless 방식
 * SecurityContextHolder는 토큰에서 파싱 후 수동 설정  
 */
public class JwtFilter extends OncePerRequestFilter {
  
  private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper;
  
  public JwtFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    /**
     * 사용자의 인증(Authentication) 정보 = Principal(사용자정보) + Credentials(인증정보) + Authorities(권한)
     */
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   
    logger.info("JwtFilter 통과 시작", auth);
    
    /**
     *  1. 헤더 Authorization 유무 체크
     */
    String requestHeader = request.getHeader("Authorization");
    String accessToken = null;
    
    // 1. Authorization 헤더가 존재하고 Bearer로 시작하는 경우
    if(requestHeader != null && requestHeader.startsWith("Beare ")) {
      accessToken = requestHeader.substring(7);
    }
    
    // 2. Authorization 헤더가 없어서 refresh에서 가져옴 (로그인 직후에만 해당할 예정) 
    if(requestHeader == null) {
      //refreshToken 쿠키에서 토큰 빼와 
      String refreshToken = jwtUtil.resolveToken(request);
      
      logger.info("리프레쉬 토큰 : {}", refreshToken);
      
      //refresh도 없다? 그럼 다음 필터 
      if(refreshToken == null) {
        filterChain.doFilter(request, response);
        return;
      }
      
      //refresh가 있다. 검증
      jwtUtil.validateToken(refreshToken);
      
      //검증 통과 -> setAuthentication 메서드 호출
      afterLoginAddAuthentication(refreshToken);
      
      //테스트 중 
      Authentication authen = SecurityContextHolder.getContext().getAuthentication();
      logger.info("getAuthentication : {}", authen);
      logger.info("authen.getAuthorities : {}", authen.getAuthorities());

      
      filterChain.doFilter(request, response);
      return;
    }
    
    
    /**
     *  2. Authorization 헤더 존재, access토큰 검증 시작 
     */
    // 1. 서명 검증
    try {
      jwtUtil.validateToken(accessToken);
      
    } catch (JwtException e) {
      logger.info("토큰 검증 결과 실패, 위조된 사인", e);
      
      //응답 설정
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
      
      // 응답 메시지 전송(Java -> JSON)
      Map<String, Object> message = new HashMap<String, Object>();
      message.put("message", "잘못된 접근입니다.");
      objectMapper.writeValue(response.getWriter(), message);
      
      return;
    }
    
    
    // 2. access인지 카테고리 확인 (굳이..?)
    String category = jwtUtil.getCategory(accessToken);
    if(!category.equals("access")) {
      
      // 응답 타입 설정
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
      
      // 응답 메시지 전송(Java -> JSON)
      Map<String, Object> message = new HashMap<String, Object>();
      message.put("message", "access 토큰이 아닙니다.");
      objectMapper.writeValue(response.getWriter(), message);
      
      return;
    }
    
    // 3. 만료시간 확인 (access : 10분)
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      e.printStackTrace();
      logger.info("access토큰 만료");
      
      // access 토큰 재발급
      
      // refresh 토큰 재발급 및 DB데이터 수정 
    }
    
    
    /**
     *  모든 검증 통과함 
     */
    logger.info("모든 토큰검증을 통과 -> 유저정보 SecurityContextHolder 세션에 저장  ");
    
    // 1. 토큰정보로 userDTO 생성
    UserDTO userVo = new UserDTO();
    userVo.setUserIdx(Long.parseLong(jwtUtil.getUserIdx(accessToken)));
    userVo.setEmail(jwtUtil.getEmail(accessToken));
    userVo.setNickname(jwtUtil.getNickname(accessToken));
    userVo.setRole(Role.valueOf(jwtUtil.getRole(accessToken)));

    // 2. userDTO를 UserDetails 구현체로 변환 ( security는 UserDetails 타입만 인식하기 때문에)
    CustomUserDetails customUser = new CustomUserDetails(userVo);
    
    // 3. UserDetails로 인증객체 생성(Authentication)
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
                       
    // 4. 현재 요청 스레드에 인증정보 보관 : @AuthenticationPrincipal로 사용가능 
    SecurityContextHolder.getContext().setAuthentication(authToken);
    
    logger.info("현재 요청 스레드 보관 정보 : {}", SecurityContextHolder.getContext().getAuthentication());
    
    // 5. 다음 필터 
    filterChain.doFilter(request, response);
    
  }
  
  /**
   * refresh토큰으로 Authentication설정 
   * @param refreshToken
   */
  private void afterLoginAddAuthentication(String refreshToken) {
    
    UserDTO userVo = new UserDTO();
    userVo.setUserIdx(Long.parseLong(jwtUtil.getUserIdx(refreshToken))); // String -> Long 형변환 주의 
    userVo.setEmail(jwtUtil.getEmail(refreshToken));
    userVo.setNickname(jwtUtil.getNickname(refreshToken));
    userVo.setRole(Role.valueOf(jwtUtil.getRole(refreshToken)));
    
    CustomUserDetails customUser = new CustomUserDetails(userVo);
    
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
    
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
  
  
  
  
}
