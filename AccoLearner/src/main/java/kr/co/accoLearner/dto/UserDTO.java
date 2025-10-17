package kr.co.accoLearner.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {

  private Long userIdx;
  private LocalDateTime createdAt;
  private String loginId;
  private String password;
  private LocalDateTime pwChangedAt;
  private String nickname;
  private String email;
  private String accountStatus; //'ACTIVE','SUSPENDED','DELETED','PENDING'
  private LocalDateTime statusChangedAt;
  private LocalDateTime lastLoginAt;
  private Boolean emailSubscribed;
  private Boolean kakaoSubscribed;
  private Boolean loginStatus; 
  
  private Enum<Role> role; //ADMIN, USER, GUEST
  
}


