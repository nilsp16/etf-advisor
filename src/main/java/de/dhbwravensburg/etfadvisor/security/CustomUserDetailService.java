package de.dhbwravensburg.etfadvisor.security;

import de.dhbwravensburg.etfadvisor.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
   private final UserRepository repository;

   public CustomUserDetailService(UserRepository repository){
       this.repository = repository;
   }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       var user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
