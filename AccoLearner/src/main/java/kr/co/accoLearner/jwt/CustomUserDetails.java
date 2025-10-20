package kr.co.accoLearner.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.accoLearner.dto.UserDTO;
import lombok.Data;

/**
 *  (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 *  로 꺼내올수 있게된다. 
 *  꺼내서 쓸 값들을 여기서 get메서드로 만들어주면 된다.
 */
@Data
public class CustomUserDetails implements UserDetails {
  
  private UserDTO userDTO;
  
  public CustomUserDetails(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorityList= new ArrayList<GrantedAuthority>();
    authorityList.add(new SimpleGrantedAuthority(userDTO.getRole().toString()));
    
    return authorityList;
  }

  //스프링 시큐리티가 PasswordEncoder.matches() 자동 호출하여 비교
  @Override
  public String getPassword() {
    return userDTO.getPassword();
  }
  
  //프로필사진..? 없어 지금은 

  @Override
  public String getUsername() {
    return userDTO.getLoginId();
  }

  public String getAcctountStatus() {
    return userDTO.getAccountStatus();
  }

  public String getEmail() {
    return userDTO.getEmail();
  }
  
  public String nickname() {
    return userDTO.getNickname();
  }

  //시큐리티에서 모두 검증하는 요소들이다. 
  @Override //계정 만료
  public boolean isAccountNonExpired() { 
    return !userDTO.getAccountStatus().equals("DELETED");
  }

  @Override //계정 잠금
  public boolean isAccountNonLocked() {
    return userDTO.getAccountStatus().equals("ACTIVE");
  }

  @Override //비밀번호 만료 
  public boolean isCredentialsNonExpired() {
    // 기능없음 
    return true;
  }

  @Override //계정 활성화 여부 (계정 가입 미승인)
  public boolean isEnabled() {
    //승인 기능 없음 
    return true;
  }
  
  
  

  
}
