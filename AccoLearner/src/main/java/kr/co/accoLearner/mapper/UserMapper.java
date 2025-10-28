package kr.co.accoLearner.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.accoLearner.dto.JwtRefreshDTO;
import kr.co.accoLearner.dto.UserDTO;

//@Mapper //myBatis API
public interface UserMapper {

  //회원가입
  public int insertUser(UserDTO userDto);
  
  //회원탈퇴
  public int deleteUser(Long userIdx);
  
  //로그인
  public UserDTO SelectUserByUsername(String username);
  
  //회원정보 중복 체크 (아이디, 이메일, 닉네임) 
  public int selectUserBy(Map<String, Object> param);
  
  //Refresh 토큰 조회
  public JwtRefreshDTO selectRefreshToken(Map<String, Object> param);
  
  //Refresh 토큰 생성 
  public int insertRefreshToken(Map<String, Object> param);
  
  //Refresh 토큰 업데이트 : 무효화 
  public int updateRefreshToken(Map<String, Object> param);
  
  //Refresh 토큰 물리적 삭제 -> 어플리케이션 배치 실행 중 
  
  
  
}
