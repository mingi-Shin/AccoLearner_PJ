package kr.co.accoLearner.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanScheduler {

  private Logger logger = LoggerFactory.getLogger(TokenCleanScheduler.class);
  private final JdbcTemplate jdbcTemplate; // root-context.xml에서 Bean 생성중 
  
  public TokenCleanScheduler(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  // (하루 4번 프로시저 호출! 단 서버가 켜져있어야 함 )
  @Scheduled(cron = "0 30 2,8,14,20 * * *")
  public void deleteExpiredTokens() {
    jdbcTemplate.execute("CALL delete_expired_refresh_token_proc()");
    logger.info("Expired tokens deleted at " + java.time.LocalDateTime.now());
  }
  
}
