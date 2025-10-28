package kr.co.accoLearner.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.accoLearner.dto.JwtRefreshDTO;
import kr.co.accoLearner.dto.UserDTO;
import kr.co.accoLearner.mapper.UserMapper;

@Service
public class UserService {

    private final AuthenticationManager authManager;
  
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;
  
  public UserService(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authManager) {
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.authManager = authManager;
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
   * 회원탈퇴 (상태값 = DELETED)
   */
  public int deleteUser(Long userIdx) {
    int result = userMapper.deleteUser(userIdx);
    return result;
  }
  
  
  /**
   * 아이디, 이메일, 닉네임 중복 체크
   */
  public boolean duplicateUserBy(String checkField, String checkValue) {
    //Object는 제너릭타입 : 어떤 값이라도 가능 
    Map<String, Object> param = new HashMap<String, Object>();
    
    param.put("checkField", checkField);
    param.put("checkValue", checkValue);
    
    int result = userMapper.selectUserBy(param);
    
    return result > 0;
  }
  
  /**
   * Refresh 토큰 조회 : 보안용 / 다중접속 프로젝트라서 refreshToken도 매개변수로 줘야함 
   * NullPointerException 처리 -> Optional 사용 
   */
  public Optional<JwtRefreshDTO> getRefreshToken(Long userIdx, String refreshToken) {
    
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("userIdx", userIdx);
    param.put("refreshToken", refreshToken);
    
    JwtRefreshDTO jwtRefreshDTO = userMapper.selectRefreshToken(param);
    
    return Optional.ofNullable(jwtRefreshDTO);
    
  }
  
  
  /**
   * Refresh 토큰 생성 
   */
  public boolean newRefreshToken(Long userIdx, String refreshToken, LocalDateTime createdAt, LocalDateTime expiresAt) {
    
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("userIdx", userIdx);
    param.put("refreshToken", refreshToken);
    param.put("createdAt", createdAt);
    param.put("expiresAt", expiresAt);
    
    int result = userMapper.insertRefreshToken(param);
    
    return result > 0;
  }
  
  /**
   * Refresh 토큰 무효화 업데이트 
   */
  public boolean deleteRefreshToken(Long userIdx) {
    
    
    
  }
  

}
