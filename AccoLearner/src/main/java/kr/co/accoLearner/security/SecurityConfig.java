package kr.co.accoLearner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * security-config.xml 을 대체함 
 */
@Configuration
@EnableWebSecurity(debug=true) //개발할 동안 디버깅 
@EnableMethodSecurity
public class SecurityConfig {

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
			);
  		
  		return http.build();
  }
  
}
