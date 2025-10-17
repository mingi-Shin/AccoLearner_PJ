package kr.co.accoLearner.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 검증 담당
 * 로그인 이후 모든 요청 : 토큰 검증 및 SecurityContextHolder세팅, stateless 방식
 * SecurityContextHolder는 토큰에서 파싱 후 수동 설정  
 */
public class JwtFilter extends OncePerRequestFilter {
  
  private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

  private final JwtUtil jwtUtil;
  
  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    /**
     * 사용자의 인증(Authentication) 정보 = Principal(사용자정보) + Credentials(인증정보) + Authorities(권한)
     */
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   
    logger.info("JwtFilter 통과 시작", auth);
  }
  
  
  
  
}
