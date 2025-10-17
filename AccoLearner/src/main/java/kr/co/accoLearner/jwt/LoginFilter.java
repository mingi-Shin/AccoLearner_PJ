package kr.co.accoLearner.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.accoLearner.dto.UserDTO;

/**
 * JWT 발급 담당 
 * UsernamePasswordAuthenticationFilter가 jwt를 못쓰고, json을 못쓰는 등의 한계를 넘기위해 새로만듬 
 * 로그인 시도 필터 : 토큰 생성 및 응답헤더, 쿠키 처리 
 * 커스텀 로그인(formLogin, httpBasic제외)은 인증 객체를 직접 만들어서 setAuthentication 해줘야해. 
 * SecurityContextHolder는 내부적으로 AuthenticationManager가 설정. 
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
  
  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper;
  private final AuthenticationManager authManager;
  
  public LoginFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, AuthenticationManager authManager) {
    this.jwtUtil = jwtUtil; //토큰생성 
    this.objectMapper = objectMapper; //json읽기 
    this.authManager = authManager; //인증토큰 받아서 내부의 AuthenticationProvider가 검증 수행하는 객체 
  }
  
  /**
   *  로그인 시도 메서드 
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    
    logger.info("=== JWT 로그인 시도 ===");
    
    //Content-Type 검증 : JSON인지 
    String contentType = request.getContentType();
    if(contentType == null || !contentType.contains("application/json")) {
      logger.error("잘못된 Content_Type : {} ", contentType);
      throw new BadCredentialsException("JSON 요청만 가능합니다. ");
      //스프링 시큐리티는 던진 예외를 받아 AuthenticationFailureHandler 호출 
    }
    
    try {
      //JSON 파싱 = .getInputStream() -> HTTP요청 바디 읽기, .readValue() -> JSON을 Java객체(LoginRequest)로 변환
      LoginRequestDTO loginRequestVO = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
      logger.info("로그인 시도 아이디 : {}", loginRequestVO.getLoginId());
      
      if(loginRequestVO.getLoginId() == null || loginRequestVO.getPassword() == null) {
        throw new BadCredentialsException("이메일과 비밀번호 입력해주세요.");
      }
      
      //인증 토큰 생성 
      UsernamePasswordAuthenticationToken authToken = 
          new UsernamePasswordAuthenticationToken(loginRequestVO.getLoginId(), loginRequestVO.getPassword());
      
      //AuthenticationManager의 인증메서드에 토큰을 매개변수로 보내서, 내부의 AuthenticationProvider가 검증처리 
      Authentication authentication = null;
      authentication = authManager.authenticate(authToken);
      
     return authentication;
      
    } catch (IOException e) {
      logger.error("JSON 파싱 오류", e);
      throw new BadCredentialsException("요청 데이터 파싱에 실패했음 ");
    } catch (AuthenticationException e) {
      logger.error("인증 실패", e);
      throw e;
    }
    
  }
  
  
  /**
   *  로그인 성공시 
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
  
    logger.info("=== JWT 로그인 성공 ===");
    
    try {
      //JWT 생성을 위해, 사용자 정보 추출 
      UserDTO userDto = (UserDTO) authResult.getPrincipal();
      logger.info("사용자 정보 : {}", user );
      
    } catch (Exception e) {
      logger.info("로그인 성공 처리중 오류 ", e);
      
    }
  
  }
  
  /**
   *  로그인 실패시 
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
          AuthenticationException failed) throws IOException, ServletException {
      
    logger.info("=== JWT 로그인 실패 ===");
  }
  
}
