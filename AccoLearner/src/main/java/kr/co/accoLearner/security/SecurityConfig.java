package kr.co.accoLearner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.accoLearner.jwt.JwtFilter;
import kr.co.accoLearner.jwt.JwtUtil;
import kr.co.accoLearner.jwt.LoginFilter;

/**
 * security-config.xml 을 대체함 
 */
@Configuration
@EnableWebSecurity(debug=true) //개발할 동안 디버깅 
@EnableMethodSecurity
public class SecurityConfig {
  
  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper;
  private final AuthenticationManager authManager;
  
  public SecurityConfig(JwtUtil jwtUtil, ObjectMapper objectMapper, AuthenticationManager authManager) {
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
    this.authManager = authManager;
  }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception{
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
		
		return authenticationManagerBuilder.build();
	}
	
	//나만의 커스텀 SecurityFilterChain 생성(@Bean) = @EnableWebSecurity가 있을 때만 활성화
  @Bean
  public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
  	
  	http
  		.csrf(csrf -> csrf.disable()) //jwt를 SPA환경에서 쓰면 (header), CSRF토큰 사용할 필요 없음 
  		.authorizeHttpRequests(auth -> auth
  			//극초반에는 모두 오픈하고 개발
      	.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
      	
			)
  		
  		.httpBasic(basic -> basic.disable()) //HTTP Basic 인증필터 추가: 브라우저의 인증 팝업 창이나 API 클라이언트(Postman, curl 등)를 통해 인증이 가능
  		.formLogin(form -> form.disable())
  		.logout(logout -> logout.disable())
  	  //아래는 JWT작업 
  	  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션사용안함, 

  	//jwt용 LoginFilter 생성
	  LoginFilter loginFilter = new LoginFilter(jwtUtil, objectMapper, authManager);
	  loginFilter.setAuthenticationManager(authManager(http));
	  loginFilter.setFilterProcessesUrl("/api/auth/login"); //로그인필터 연동 
  	  
	  //필터 등록 (Jwtfilter -> loginFilter)
	  http
  	  .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) //JWT를 필터체인에 등록을 해야 실행됨 
  	  .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  		return http.build();
  }
  
}
