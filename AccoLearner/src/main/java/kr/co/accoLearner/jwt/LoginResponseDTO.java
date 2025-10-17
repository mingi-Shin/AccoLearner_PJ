package kr.co.accoLearner.jwt;

import lombok.Data;

@Data
public class LoginResponseDTO {
  
  private boolean success;
  private String message;
  private String accessToken;
  private String refreshToken;
  private String redirectUrl;
  private String role;
  

}
