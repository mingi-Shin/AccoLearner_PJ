package kr.co.accoLearner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
  
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @GetMapping("/login")
  public String loginPage() {
    logger.info("로그인 페이지 요청");
    return "login/loginPage";
  }
  
  
  
}
