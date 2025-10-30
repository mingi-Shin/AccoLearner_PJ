package kr.co.accoLearner.service;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.accoLearner.mapper.UserMapper;

@Service
public class MainService {

  private Logger logger = LoggerFactory.getLogger(MainService.class);

  private final UserMapper userMapper;
  
  public MainService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }
  
  /**
   * 로그아웃
   */
  public boolean logout(String userSeq, String refreshToken) {
    
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("userSeq", userSeq);
    param.put("refreshToken", refreshToken);
    
    int result = userMapper.updateRefreshToken(param);
    
    return result > 0;
  }
  
  
  
}
