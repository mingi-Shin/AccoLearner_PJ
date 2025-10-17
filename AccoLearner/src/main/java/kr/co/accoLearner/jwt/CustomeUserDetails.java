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
public class CustomeUserDetails implements UserDetails {
  
  private UserDTO userDTO;
  
  public CustomeUserDetails(UserDTO userDTO) {
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

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return false;
  }
  
  
  

  
}
