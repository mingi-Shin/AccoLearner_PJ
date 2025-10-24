package kr.co.accoLearner.dto;

public enum Role {
  
  ADMIN,
  USER,
  GUEST
  
}

/**
 * 대부분의 기업 실무에서는
  DB에는 "USER"로 저장하고,
  코드에서 "ROLE_" 접두어를 붙이는 방식(B) 을 사용합니다.
 */
