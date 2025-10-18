package kr.co.accoLearner.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    
    if(requestHeader == null) {
      logger.info("Authorization 헤더 없음. 다음 필터로 이동");
      filterChain.doFilter(request, response);
      return;
    }
    
    if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
      accessToken = requestHeader.substring(7);
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
     *  모든 검증 통과 
     */
    logger.info("모든 토큰검증을 통과 -> SecurityContextHolder 세션 저장  ");
    UserDTO userVo = new UserDTO();
    userVo.setUserIdx(Long.parseLong(jwtUtil.getUserIdx(accessToken)));
    userVo.setEmail(jwtUtil.getEmail(accessToken));
    userVo.setNickname(jwtUtil.getNickname(accessToken));
    userVo.setRole(Role.valueOf(jwtUtil.getRole(accessToken)));

    
    
  }
  
  
  
  
}
