package kr.co.accoLearner.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.accoLearner.jwt.JwtUtil;
import kr.co.accoLearner.service.MainService;

@Controller
public class MainController {
  
  private static final Logger logger = LoggerFactory.getLogger(MainController.class);
  
  private final MainService mainService;
  private final JwtUtil jwtUtil;
  
  public MainController(MainService mainService, JwtUtil jwtUtil) {
    this.mainService = mainService;
    this.jwtUtil = jwtUtil;
  }
  
  @GetMapping("/")
  public String main() {
    logger.info("메인 페이지 요청 도착 ");
    logger.debug("디버그용 상세 로그입니다. ");
    return "index";
  }
  
  @GetMapping("/selfStudy")
  public String selfStudy() {
    logger.info("자바스크립트 연습 페이지 이동");
    return "selfStudy/javaScript";
  }
  
  /**
   * 로그아웃
   * @return
   */
  @PostMapping("/api/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) { //<body에 들어갈 실제 응답 데이터 타입> : Void는 body에 내용 없음을 의미 
    String requestHeader = request.getHeader("Authorization");

    if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
      String accessToken = requestHeader.substring(7);
      logger.info("accessToken : {}", accessToken);

      String refreshToken = jwtUtil.resolveToken(request);
      String userSeq = jwtUtil.getUserSeq(accessToken);
      
      boolean result = mainService.logout(userSeq, refreshToken);
      
      
      
      logger.info("로그아웃 성공? : {}", result );
      
    } else {
      logger.warn("Authorization header is missing or empty");
    }
    // 항상 204 No Content 반환
    return ResponseEntity.noContent().build();
  }

}