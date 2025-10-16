package kr.co.accoLearner.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT 발급 담당 
 * 로그인 시도 필터 : 토큰 생성 및 응답헤더, 쿠키 처리 
 * 커스텀 로그인(formLogin, httpBasic제외)은 인증 객체를 직접 만들어서 setAuthentication 해줘야해. 
 * SecurityContextHolder는 내부적으로 AuthenticationManager가 설정. 
 */
public class LoginFilter {

  private final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
  
  
}
