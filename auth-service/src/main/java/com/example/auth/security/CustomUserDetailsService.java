package com.example.auth.security;

import com.example.auth.domain.user.UserAccount;
import com.example.auth.domain.user.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

   private final UserAccountRepository repo;

   public CustomUserDetailsService(UserAccountRepository repo) {
      this.repo = repo;
   }

   @Override
   public UserDetails loadUserByUsername(String email)
         throws UsernameNotFoundException {

      System.out.println(">>> CustomUserDetailsService CALLED with email = " + email);

      UserAccount user = repo.findWithRolesByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      System.out.println(">>> User FOUND: " + user.getEmail());

      return new User(
            user.getEmail(),
            user.getPasswordHash(),
            user.getRoles().stream()
                  .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getCode()))
                  .toList()
      );
   }

}
