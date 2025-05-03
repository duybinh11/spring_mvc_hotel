package Service;

import Entity.UserEntity;
import Exception1.ResourceNotFoundException;
import Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(s);
        if (!userEntity.isPresent()) {
            throw new BadCredentialsException("email not found");
        }
        return userEntity.get();
    }


    public List<SimpleGrantedAuthority> permissions(String email){
        Optional<List<String>> permissions = userRepository.findUserGetPermisstion(email);
        if (!permissions.isPresent()) {
            throw new ResourceNotFoundException("Invalid email1");
        }
        return permissions.get().stream().map(SimpleGrantedAuthority::new).toList();
    }

}
