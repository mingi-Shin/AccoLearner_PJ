package kr.co.accoLearner.jwt;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    this.objectMapper = objectMapper; //json 처리 객체 
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
      
      if(loginRequestVO.getLoginId().trim() == null || loginRequestVO.getPassword() == null) {
        throw new BadCredentialsException("이메일과 비밀번호 입력해주세요.");
      }
      
      //인증 토큰 생성 (id, password)
      UsernamePasswordAuthenticationToken authToken = 
          new UsernamePasswordAuthenticationToken(loginRequestVO.getLoginId(), loginRequestVO.getPassword());
      

   // 1. AuthenticationManager의 authenticate()에 로그인 토큰(authToken)을 전달
   // 2. 내부의 AuthenticationProvider가 아이디/비밀번호 검증 수행
   // 3. 성공 시 → 사용자정보(UserDetails)가 담긴 Authentication 객체 리턴
   //    실패 시 → AuthenticationException 발생 (return 없음)
   // 4. Authentication은 principal(사용자정보 : UserDetails 타입), credentials(비번/null), authorities(권한), authenticated(boolean) 등으로 구성
   // 5. 성공한 Authentication은 SecurityContextHolder에 저장되어 이후 요청에서 인증된 사용자로 인식됨
      Authentication authResult = null;
      authResult = authManager.authenticate(authToken);
      
     return authResult;  
      
    } catch (IOException e) {
      logger.error("JSON 파싱 오류", e);
      throw new BadCredentialsException("요청 데이터 파싱에 실패했음 ");
    } catch (AuthenticationException e) {
      logger.error("인증 실패", e);
      throw e;
    }
    
  }
  
  
  /**
   *  로그인 성공시 ( = .authenticate(authToken) 성공)
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
  
    logger.info("=== JWT 로그인 성공 ===");
    
    try {
      // 1. JWT 생성을 위해, 사용자 정보 추출 
      CustomUserDetails customUser  = (CustomUserDetails) authResult.getPrincipal();
      logger.info("사용자 정보 : {}", customUser.getUsername() );
      
      // 2. JWT토큰 생성
      String accessToken = jwtUtil.createJwtToken("access", customUser.getUserDTO(), 10 * 60 * 1000L); //10분 
      String refreshToken = jwtUtil.createJwtToken("refresh", customUser.getUserDTO(), 24 * 60 * 60 * 1000L); //24시간 
      
      // 3. access 토큰 -> 응답헤더 설정
      response.setHeader("Authorization", "Bearer " + accessToken);
      
      // 4. refresh 토큰 -> 쿠키 설정 ("Set-Cookie")
      ResponseCookie refreshCookie = createRefreshCookie(refreshToken);
      response.addHeader("Set-Cookie", refreshCookie.toString());
      
      // 5. (Option) 성공 응답 객체 생성
      String rootUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
          true,
          "로그인 성공",
          accessToken,
          refreshToken,
          rootUrl + "/home",
          customUser.getUserDTO().getRole().toString()
          );
      
      // 6. 응답타입 설정 
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpStatus.OK.value()); //프론트엔드 if문 분기점 
      
      // 7. 실제 데이터 전송 (java객체 -> josn문자열), 프론트엔드에서 받아서 여러가지 작업 처리 
      objectMapper.writeValue(response.getWriter(), loginResponseDTO);
      
    } catch (Exception e) {
      logger.info("로그인 성공 처리중 오류 ", e);
      
      // 6-2. 응답타입 설정 
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
      
      logger.error("로그인 성공처리 에러 : {} ", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      
      // 7-2. 오류 메시지 JSON 전송 
      Map<String, Object> errorResponse = new HashMap<String, Object>();
      errorResponse.put("message", "로그인 처리 중 서버 오류 발생 ");
      objectMapper.writeValue(response.getWriter(), errorResponse);
      
    }
  
  }
  
  /**
   *  로그인 실패시 ( = .authenticate(authToken)에서 예외발생)
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
          AuthenticationException failed) throws IOException, ServletException {
      
    logger.error("=== JWT 로그인 실패 ===", failed);
    
    /**
     *  여기도 로그인 정보에 따른 예외처리 수정해야함 
     */
    String failMessage;
    if(failed instanceof BadCredentialsException) {
      failMessage = "이메일 또는 비밀번호가 옳바르지 않습니다.";
    } else if(failed instanceof InternalAuthenticationServiceException) {
      failMessage = "사용자 정보를 찾을 수 없습니다.";
    } else {
      failMessage = "로그인에 실패했습니다.";
    }
    
    // 로그인 실패 응답 설정 
    response.setContentType("applicatioin/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
    
    // 응답 전송
    Map<String, Object> failResponse = new HashMap<String, Object>();
    failResponse.put("message", failMessage);
    objectMapper.writeValue(response.getWriter(), failResponse);
    
  }
  
  /**
   * Refresh 토큰 HttpOnly 쿠키 생성 메서드
   */
  private ResponseCookie createRefreshCookie(String refreshToken) {
    return ResponseCookie.from("refresh", refreshToken)
        .httpOnly(true) // js에서 쿠키 접근 불가
        .secure(false) // 개발환경 : false, 운영환경 : ture
        .sameSite("Lax") // CSRF방지 (Lax : 크로스사이트 GET 요청만 허용, 보안성은 Strict가 더 높음)
        .path("/")
        .maxAge(Duration.ofHours(24))
        .build();
  }
  
}
