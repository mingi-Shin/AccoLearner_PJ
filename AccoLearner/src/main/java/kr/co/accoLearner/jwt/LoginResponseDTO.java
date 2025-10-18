package kr.co.accoLearner.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
  
  private boolean success;
  private String message;
  private String accessToken;
  private String refreshToken;
  private String redirectUrl;
  private String role;
  

}
