package com.bcn.authService.service;

import com.bcn.authService.data.*;
import com.bcn.authService.util.DateConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.sql.SQLException;


@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        try {
            if (repository.findByUsername(request.getUsername()).isPresent()) {
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
        } catch(DataIntegrityViolationException e) {
        return new AuthenticationResponse(null, "ERROR: Username already exists");
        } catch (InvalidDataAccessApiUsageException e) {
        return new AuthenticationResponse(null, "ERROR: Invalid data access API usage");
        } catch (TransactionSystemException e) {
        return new AuthenticationResponse(null, "ERROR: Transaction system exception");
        } catch (PersistenceException e) {
        return new AuthenticationResponse(null, "ERROR: Persistence exception");
        } catch (Exception e) {
        return new AuthenticationResponse(null, "User registration failed: " + e.getMessage());
        }

    }

    public AuthenticationResponse authenticate(User request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = repository.findByUsername(request.getUsername()).orElseThrow();
            String jwt = jwtService.generateToken(user);
            return new AuthenticationResponse(jwt, "User login was successful");
        } catch (AuthenticationException e) {
            return new AuthenticationResponse(null, "Incorrect username or password. Please try again.");
        } catch (EntityNotFoundException e) {
            return new AuthenticationResponse(null, "User not found.");
        } catch (Exception e) {
            return new AuthenticationResponse(null, "Error during authentication: " + e.getMessage());
        }

    }

    public boolean isValidToken(String token) {
        System.out.println("isValidToken  in auth service: " + token);
        return jwtService.isValidToken(token);
    }
}
