package kr.co.accoLearner.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import kr.co.accoLearner.dto.UserDTO;

@Component
public class JwtUtil {
  
  private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

  //JWT 생성시 : secretKey로 Header + Payload를 서명(Signature 생성)
  //JWT 검증시 : 클라이언트가 보낸 JWT의 서명을 같은 sercretKey로 다시 계산해 비교 
  private final SecretKey secretKey;

  private final Environment environment; //properties등을 관리하는 컨테이너 
  
  /**
   * jwtKey를 배열변환하여 secretKey변수에 초기화 
   */
  public JwtUtil(@Value("${spring.jwt.key}") String jwtKey, Environment environment) {
    // 최소 256비트(32바이트) 이상이어야 HS256 사용 가능
    secretKey = new SecretKeySpec(
        jwtKey.getBytes(StandardCharsets.UTF_8), //문자열 키를 바이트 배열(byte[]) 로 변환
        Jwts.SIG.HS256.key().build().getAlgorithm()
    );
    
    this.environment = environment;
  }
  
  /**
   * 토큰 생성
   * @param category (access, refresh)
   * @param user (로그인 사용자 정보)
   * @param expiredMs (토큰 만료시간)
   * @return
   */
  public String createJwtToken(String category, UserDTO user, Long expiredMs) {
    logger.info("jwt 토큰 생성 시작, 타입 : ", category);
    try {
      return Jwts.builder()
          .claim("category", category)
          .claim("userIdx", String.valueOf( user.getUserIdx()))
          .claim("nickname", user.getNickname())
          .claim("email", user.getEmail())
          .claim("role", user.getRole())
          .claim("iss", "Shinmingi")
          .issuedAt(new Date(System.currentTimeMillis())) //토큰의 발급시간 설정
          .expiration(new Date(System.currentTimeMillis() + expiredMs)) //토큰 만료시간 설정
          .signWith(secretKey) //jwtKey를 HS256알고리즘으로 변환한 secretKey로 서명생성
          .compact(); //JWT를 문자열로 압축하여 반환.. 끝! 
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("JWT 생성 실패", e);
    }
  }
  
  /**
   * JWT 토큰 검증 (사인검증, 속성 꺼내기 : category, userIdx, nickname, email, role, iss, expiration)
   */
  
  //서명검증! Filter에서 catch하고 있으므로 여기서 할필요 없음 
  public void validateToken(String token) throws JwtException {
    Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token);
  }
  
  public String getCategory(String token) {
    try {
      return Jwts.parser() // 파서(parser)를 생성
          .verifyWith(secretKey) // 서명 검증
          .build() // 빌더를 완성
          .parseSignedClaims(token) // 서명을 검증한 뒤 JWT의 본문(Claims)을 파싱
          .getPayload() // 파싱된 클레임 데이터에서 페이로드 부분(실제 데이터를 담고 있는 JSON)을 반환
          .get("category", String.class); // JSON 클레임 중 "category"이라는 키에 해당하는 값을 추출
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public String getUserIdx(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .get("userIdx", String.class); 
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public String getNickname(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .get("nickname", String.class); 
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public String getEmail(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .get("email", String.class); 
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public String getRole(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .get("role", String.class); 
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public Boolean isExpired(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .getExpiration().before(new Date());
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  public String getIss(String token) {
    try {
      return Jwts.parser() 
          .verifyWith(secretKey) 
          .build() 
          .parseSignedClaims(token) 
          .getPayload() 
          .get("iss", String.class);
    } catch (JwtException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid JWT Token", e);
    }
  }
  
  /**
   * Refresh 토큰 HttpOnly 쿠키 생성 메서드
   */
  public ResponseCookie createRefreshCookie(String refreshToken) {
    return ResponseCookie.from("refresh", refreshToken)
        .httpOnly(true) // js에서 쿠키 접근 불가
        .secure(false) // 개발환경 : false, 운영환경 : ture
        .sameSite("Lax") // CSRF방지 (Lax : 크로스사이트 GET 요청만 허용, 보안성은 Strict가 더 높음)
        .path("/")
        .maxAge(Duration.ofHours(24))
        .build();
  }
  
  
  /**
   * 쿠키 꺼내기 (refresh같은 거)
   */
  public String resolveToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    
    if(cookies != null) {
      for(Cookie cookie : cookies) {
        if(cookie.getName().equals("refresh")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
  
  
  
  
  
  
  
}
