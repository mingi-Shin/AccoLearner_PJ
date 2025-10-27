package kr.co.accoLearner.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
  // @ResponseBody 는 컨트롤러 메서드의 반환값을 HTTP 응답 바디에 직접 쓰겠다는 의미 
  // 그런데 ResponseEntity 는 @ResponseBody 역할을 포함하고 있어서 @ResponseBody안써도 됨
  public ResponseEntity<Map<String, Object>> joinUser(@RequestBody UserDTO user){
    Long userIdx = userService.registerUser(user);
    logger.info(userIdx + "번 회원 등록 성공");
    return ResponseEntity.ok(Map.of("success", true)); //HTTP status 200 + body에 Map(JSON)
  }
  
  /**
   * 회원가입 시 개인정보 동의 페이지 이동 
   */
  @GetMapping("/legal/terms")
  public String terms() {
    return "legal/terms";
  }
  
  @GetMapping("/legal/privacy")
  public String privacy() {
    return "legal/privacy";
  }
  
  /**
   * 회원탈퇴 (논리적)
   */
  
  
  /**
   * 회원 정보 중복 체크  (loginId, email, nickname)
   * body에 담긴 값이 단일이면 String 가능, 하지만 n개면 Map or DTO 사용 (스프링이 JSON을 Map, DTO로 자동변환해줌)
   */
  @PostMapping("/api/user/duplicate")
  public ResponseEntity<Map<String, Object>> duplicateUser(@RequestBody Map<String, Object> requestBody){
    
    String checkField = (String)requestBody.get("checkField");
    String checkValue = (String)requestBody.get("checkValue");

    Map<String, Object> resultMap = new HashMap<String, Object>();
    boolean result = userService.duplicateUserBy(checkField, checkValue);
    
    resultMap.put("isDuplicated", result);
    
    return ResponseEntity.ok(resultMap); //Map.of를 해서 넣어도 되고 
  }
  
  
  
}
