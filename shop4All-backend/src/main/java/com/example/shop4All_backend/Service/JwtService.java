package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.Entity.JwtRequest;
import com.example.shop4All_backend.Entity.JwtResponse;
import com.example.shop4All_backend.Entity.User;
import com.example.shop4All_backend.Repository.UserRepo;
import com.example.shop4All_backend.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userEmail = jwtRequest.getUserEmail();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userEmail,userPassword);

        final UserDetails userDetails = loadUserByUsername(userEmail);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userRepo.findById(userEmail).get();

        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    // Load user details by username (email in this case)
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepo.findById(userEmail).get();

        if (user != null){
            return new org.springframework.security.core.userdetails.User(
              user.getUserEmail(),user.getUserPassword(),getAuthorities(user)
            );
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    // Get authorities (roles) for the user
    private Set getAuthorities(User user) {
        Set authorities = new HashSet();

        user.getRole().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName())));

        return authorities;
    }

    // Authenticate user credentials
    private void authenticate(String userEmail, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, userPassword));
        } catch (DisabledException e) {
            throw new Exception("User is disable");
        } catch (BadCredentialsException e){
            throw new Exception("Bad credential for user");
        }
    }
}
