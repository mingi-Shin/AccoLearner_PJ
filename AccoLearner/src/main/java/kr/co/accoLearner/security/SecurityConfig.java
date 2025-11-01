package kr.co.accoLearner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
  private final UserDetailsServiceImpl userDetailsServiceImpl;
  
  public SecurityConfig(JwtUtil jwtUtil, ObjectMapper objectMapper, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }
  
  /*
   * ObjectMapper클래스를 따로 만들어서 @Configuration 해줬음. 생성자 주입 중~
   * @Bean 
   * public ObjectMapper objectMapper() { 
   *    return new ObjectMapper(); 
   *  }
   */
  
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	  AuthenticationManagerBuilder authenticationManagerBuilder = 
      http.getSharedObject(AuthenticationManagerBuilder.class);
	    
    authenticationManagerBuilder
      .userDetailsService(userDetailsServiceImpl) //DB에서 사용자 정보(UserDetails)를 가져오는 방법을 알랴줌
      .passwordEncoder(passwordEncoder()); //스프링 시큐리티가 로그인 시 입력한 비밀번호를 암호화된 DB 비밀번호와 비교할 때 사용할 인코더를 지정
	    //DaoAuthenticationProvider가 내부적으로: passwordEncoder.matches(rawPassword, encodedPasswordFromDB) 수행
    
    return authenticationManagerBuilder.build();
	}
	
	
	//나만의 커스텀 SecurityFilterChain 생성(@Bean) = @EnableWebSecurity가 있을 때만 활성화
  @Bean
  public SecurityFilterChain customSecurityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
  	
  	http
  		.csrf(csrf -> csrf.disable()) //jwt를 SPA환경에서 쓰면 (header), CSRF토큰 사용할 필요 없음 
  		.authorizeHttpRequests(auth -> auth
  			//극초반에는 모두 오픈하고 개발
      	.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
      	
      	/**
      	 * hasRole은 내부적으로 ROLE_을 붙여서 비교한다. SecurityContextHolder에 ROLE_이 붙여 있어야 매치 가능 
      	 */
      	//.requestMatchers("/api/join/**", ).permitAll() //회원가입 공개 API
      	//.requestMatchers("/api/user/**").authenticated() //로그인 후 API
      	//.requestMatchers("/api/admin/**").hasRole("ADMIN") //관리자만 api 요청 가능 
      	//.requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능  
      	//.requestMatchers("/user/**").authenticated() //로그인 유저만 접근 가능 
      	
      	//.requestMatchers(HttpMethod.GET, "/board/notice").permitAll()
      	//.requestMatchers(HttpMethod.POST, "/board/notice").hasRole("ADMIN")
      	//.anyRequest().authenticated() //다른 요청 모두 로그인 필요 
      	
			)
  		
  		.httpBasic(basic -> basic.disable()) //HTTP Basic 인증필터 추가: 브라우저의 인증 팝업 창이나 API 클라이언트(Postman, curl 등)를 통해 인증이 가능
  		.formLogin(form -> form.disable())
  		.logout(logout -> logout.disable())
  	  //아래는 JWT작업 
  	  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션사용안함, 

  	//jwt용 LoginFilter 생성
	  LoginFilter loginFilter = new LoginFilter(jwtUtil, objectMapper, authManager);
	  loginFilter.setFilterProcessesUrl("/api/auth/login"); //로그인필터 연동 
  	  
	  //필터 등록 (Jwtfilter -> loginFilter)
	  http
  	  .addFilterBefore(new JwtFilter(jwtUtil, objectMapper), UsernamePasswordAuthenticationFilter.class) //JWT를 필터체인에 등록을 해야 실행됨 
  	  .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  		return http.build();
  }
  
}
