package kr.co.accoLearner.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.accoLearner.dto.UserDTO;

//@Mapper //myBatis API
public interface UserMapper {

  //회원가입
  public int insertUser(UserDTO userDto);
  
  //회원탈퇴
  public UserDTO deleteUser(UserDTO userDto);
  
  //로그인
  public UserDTO SelectUserByUsername(String username);
  
}
