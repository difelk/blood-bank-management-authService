package com.bcn.authService.service;

import com.bcn.authService.data.*;
import com.bcn.authService.util.DateConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

//    private final TokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
//                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
//        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {

        System.out.println("inside of register method of service");
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setNic(request.getNic());
        user.setContactNo(request.getContactNo());
        user.setBloodType(request.getBloodType());
        user.setAddressNo(request.getAddressNo());
        user.setStreet(request.getStreet());
        user.setCity(request.getCity());
        user.setBirthday(DateConverter.convertBirthday(request.getBirthday().toString()));
        user.setOrganizationType(request.getOrganizationType());
        user.setOrganization(request.getOrganization());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        user.setRole(request.getRole());


        user = repository.save(user);

        String jwt = jwtService.generateToken(user);


        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

//        revokeAllTokenByUser(user);
//        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");

    }
//    private void revokeAllTokenByUser(User user) {
//        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
//        if(validTokens.isEmpty()) {
//            return;
//        }
//
//        validTokens.forEach(t-> {
//            t.setLoggedOut(true);
//        });
//
//        tokenRepository.saveAll(validTokens);
//    }
//    private void saveUserToken(String jwt, User user) {
//        Token token = new Token();
//        token.setToken(jwt);
//        token.setLoggedOut(false);
//        token.setUser(user);
//        tokenRepository.save(token);
//    }
}
