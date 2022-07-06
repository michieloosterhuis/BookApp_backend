package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.Authority;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user;
        try {
            user = userRepository.findById(username)
                    .orElseThrow(() -> new UserNotFoundException(username));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        String password = user.getPassword();

        Set<Authority> authorities = user.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }

}