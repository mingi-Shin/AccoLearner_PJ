package kr.co.accoLearner.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshDTO {

  private Long refreshSeq;
  private Long userIdx;
  private String refreshToken;
  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;
  private String isActive;
  
}
