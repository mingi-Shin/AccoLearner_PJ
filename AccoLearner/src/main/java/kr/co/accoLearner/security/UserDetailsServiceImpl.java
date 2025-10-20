package kr.co.accoLearner.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.accoLearner.dto.UserDTO;
import kr.co.accoLearner.jwt.CustomUserDetails;
import kr.co.accoLearner.mapper.UserMapper;

/**
 *  
   http.formLogin().loginProcessingUrl(...)에 의해 로그인 요청이 들어오면,

1. UsernamePasswordAuthenticationFilter가 요청을 가로채고,
   내부적으로 AuthenticationManager.authenticate( authToken )를 호출합니다.

2. AuthenticationManager는 등록된 AuthenticationProvider(기본: DaoAuthenticationProvider)를 사용해
   UserDetailsService.loadUserByUsername()를 자동 호출하여 사용자 정보를 로드하고
   PasswordEncoder로 비밀번호를 검증합니다.
*/
@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
  private final UserMapper userMapper;
  
  public UserDetailsServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    logger.info("--- loadUserByUsername() 실행 ---");
    UserDTO user = userMapper.SelectUserByUsername(username);
    if(user == null) {
      throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
    }
    
    CustomUserDetails cUserDetails = new CustomUserDetails(user);
    
    return cUserDetails;
  }

}
