package com.bcn.authService.service;

import com.bcn.authService.data.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "38851596fa3f94b2e3e67fcf593625aea1be1eb02d0a7572e0c206c8579002d6";
//    private final TokenRepository;



//    public JwtService(TokenRepository tokenRepository) {
//        this.tokenRepository = tokenRepository;
//    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

//        boolean validToken = tokenRepository
//                .findByToken(token)
//                .map(t -> !t.isLoggedOut())
//                .orElse(false);

//        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user){
//        String token = Jwts
//                .builder()
//                .subject(user.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000 ))
//                .signWith(getSigninKey())
//                .compact();
//
//
//        return  token;


            Date now = new Date();
            Date expiration = new Date(now.getTime() + 24*60*60*1000);

            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim("firstName", user.getFirstName())
                    .claim("lastName", user.getLastName())
                    .claim("role", user.getRole())
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getSigninKey())
                    .compact();





    }



    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
