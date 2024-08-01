package com.example.shop4All_backend.service;

import com.example.shop4All_backend.dto.JwtRequest;
import com.example.shop4All_backend.dto.JwtResponse;
import com.example.shop4All_backend.entity.Role;
import com.example.shop4All_backend.entity.User;
import com.example.shop4All_backend.exceptions.ActivateAccountException;
import com.example.shop4All_backend.exceptions.LogInException;
import com.example.shop4All_backend.repository.UserRepo;
import com.example.shop4All_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Create JWT token for authenticated user
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws AuthenticationException {
        String userEmail = jwtRequest.getUserEmail();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userEmail, userPassword);
        User user;

        Optional<User> userOptional = userRepo.findByUserEmail(userEmail);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }

        if (user.getRole() == Role.SELLER && !user.isUserIsValid())
            throw new ActivateAccountException("Account was not validate by the admin");

        final UserDetails userDetails = loadUserByUsername(userEmail);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        return new JwtResponse(user, newGeneratedToken);
    }

    // Load user details by username (email in this case)
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepo.findByUserEmail(userEmail).orElse(null);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserEmail(), user.getUserPassword(), getAuthorities(user)
            );
        } else {
            throw new LogInException("User not found");
        }
    }

    // Get authorities (roles) for the user
    private Set getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Role role = user.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }


    // Authenticate user credentials
    private void authenticate(String userEmail, String userPassword) throws AuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, userPassword));
        } catch (DisabledException e) {
            throw new LogInException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new LogInException("Bad credentials for user");
        } catch (AuthenticationException e) {
            throw new LogInException("Authentication failed");
        }
    }


}
