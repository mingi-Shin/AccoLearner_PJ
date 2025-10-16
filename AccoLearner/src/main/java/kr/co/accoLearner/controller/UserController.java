package kr.co.accoLearner.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.accoLearner.dto.UserDTO;
import kr.co.accoLearner.service.UserService;

@Controller
public class UserController {
  
  private final UserService userService;
  
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }
  
  @GetMapping("/login")
  public String loginPage() {
    logger.info("로그인 페이지 요청");
    return "login/loginPage";
  }
  
  //회원가입 페이지 이동
  @GetMapping("/join")
  public String joinPage() {
    logger.info("회원가입 페이지 요청");
    return "login/joinPage";
  }
  
  /**
   * 회원가입 처리 (@RequestBody = 컨트롤러에서 josn받기 / @ResponseBody = 응답할 때 json리턴)
   */
  //@PostMapping("/api/join")
  @ResponseBody
  public Map<String, Object> join(@RequestBody UserDTO user){ //@RequestBody = JSON → UserDTO로 자동 매핑
    Long userIdx = userService.registerUser(user);
    logger.info(userIdx + "번 회원 등록 성공");
    return Map.of("success", true, "message", "회원가입 성공");
    //이렇게 하면 Spring이 리턴값을 자동으로 JSON 문자열로 변환해서 브라우저에 보냅니다.
  }
  
  @PostMapping("/api/join")
  // 그런데 ResponseEntity 는 @ResponseBody 역할을 포함하고 있어서 @ResponseBody안써도 됨
  public ResponseEntity<Map<String, Object>> joinUser(@RequestBody UserDTO user){
    Long userIdx = userService.registerUser(user);
    logger.info(userIdx + "번 회원 등록 성공");
    return ResponseEntity.ok(Map.of("success", true));
  }
  
  /**
   * 회원탈퇴 (논리적)
   */
  
  
}
