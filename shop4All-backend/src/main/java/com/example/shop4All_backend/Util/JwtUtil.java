package com.example.shop4All_backend.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "shop4All-backend";
    private static final int TOKEN_VALIDITY = 3600 * 7;

    // Extract the user's email from the JWT token
    public String getUserEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract claims from the JWT token using a function to process claims
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from the JWT token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // Validate the JWT token against the user details and expiration
    public boolean validateToken(String token, UserDetails userDetails) {
        String userEmail = getUserEmailFromToken(token);
        return ((userDetails.getUsername().equals(userEmail)) && !isTokenExpired(token));
    }

    // Check if the JWT token has expired
    private boolean isTokenExpired(String token) {
        final Date getexpirationDateFromToken = getExpirationDateFromToken(token);
        return getexpirationDateFromToken.before(new Date());
    }

    // Extract the expiration date from the JWT token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Generate a new JWT token for the given user details
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }

}
