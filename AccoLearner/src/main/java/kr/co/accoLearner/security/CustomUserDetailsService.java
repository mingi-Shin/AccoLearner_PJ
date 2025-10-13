package kr.co.accoLearner.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService") // Bean 이름을 지정해서 DaoAuthenticationProvider에서 찾을 수 있게 함
public class CustomUserDetailsService implements UserDetailsService {

  /**
   * 스프링 시큐리티는 로그인 시 입력한 username을 loadUserByUsername에 전달합니다.
   * 우리가 구현한 UserDetailsService는 DB나 다른 저장소에서 사용자 정보를 조회합니다.
   * 조회된 정보(UserDetails)에는 username, password, 권한(roles)이 들어있습니다.
   * 시큐리티가 이 정보를 가져와서 패스워드 확인 + 권한 체크를 수행합니다.
   * 즉, 로그인 검증 로직의 핵심 역할을 합니다.  
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodedPw = encoder.encode("1234");
    
    if(!"admin".equals(username)) {
      throw new UsernameNotFoundException("-------------[throw] User not found Exception-------------");
    }
    
    // User 객체: username, password, 권한
    return org.springframework.security.core.userdetails.User
        .withUsername("admin")
        .password(encodedPw) //{noop}은 테스트용 암호화 없이 사용, 실제 서비스면 BCryptPasswordEncoder 쓰기
        .roles("ADMIN")
        .build();
  }

}
