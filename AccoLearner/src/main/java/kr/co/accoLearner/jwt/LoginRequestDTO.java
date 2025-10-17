package kr.co.accoLearner.jwt;

import lombok.Data;

@Data
public class LoginRequestDTO {

  private String loginId;
  private String password;
}
