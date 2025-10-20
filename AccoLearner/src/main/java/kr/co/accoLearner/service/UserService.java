package kr.co.accoLearner.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.accoLearner.dto.UserDTO;
import kr.co.accoLearner.mapper.UserMapper;

@Service
public class UserService {
  
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;
  
  public UserService(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }
  
  /**
   * 회원가입
   * userIdx 가져옴 (= mapper의 useGeneratedKeys="true" 설정)
   */
  public Long registerUser(UserDTO user) {
    String encodedPwd = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPwd);
    userMapper.insertUser(user);
    //insert가 끝나면 user 객체 안에 자동으로 user_idx가 들어있음.
    return user.getUserIdx();
  }
  
  /**
   * 회원탈퇴
   */
  
  
  
  

}
