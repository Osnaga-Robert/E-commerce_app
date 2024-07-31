package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.DTO.JwtRequest;
import com.example.shop4All_backend.DTO.JwtResponse;
import com.example.shop4All_backend.Entity.UserFactory.Admin;
import com.example.shop4All_backend.Entity.UserFactory.Buyer;
import com.example.shop4All_backend.Entity.UserFactory.Seller;
import com.example.shop4All_backend.Entity.UserFactory.User;
import com.example.shop4All_backend.Repository.AdminRepo;
import com.example.shop4All_backend.Repository.BuyerRepo;
import com.example.shop4All_backend.Repository.SellerRepo;
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

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Create JWT token for authenticated user
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {

        String userEmail = jwtRequest.getUserEmail();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userEmail,userPassword);

        Optional<Admin> admin = adminRepo.findById(userEmail);
        Optional<Seller> seller = sellerRepo.findById(userEmail);
        Optional<Buyer> buyer = buyerRepo.findById(userEmail);

        User user;
        if (admin.isPresent()) {
            user = admin.get();
        }
        else if (seller.isPresent()) {
            user = seller.get();
            Seller adminCheck =  (Seller) user;
            if(!adminCheck.isUserIsValid())
                throw new ValidationException("Account was not validate by the admin");
        }
        else{
            user = buyer.get();
        }

        final UserDetails userDetails = loadUserByUsername(userEmail);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        return new JwtResponse(user, newGeneratedToken);
    }

    // Load user details by username (email in this case)
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepo.findById(userEmail);
        Optional<Seller> seller = sellerRepo.findById(userEmail);
        Optional<Buyer> buyer = buyerRepo.findById(userEmail);

        User user;
        if (admin.isPresent()) {
            user = admin.get();
        }
        else if (seller.isPresent()) {
            user = seller.get();
        }
        else{
            user = buyer.get();
        }

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
