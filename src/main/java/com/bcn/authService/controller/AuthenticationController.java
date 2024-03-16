package com.bcn.authService.controller;

import com.bcn.authService.data.AuthenticationResponse;
import com.bcn.authService.data.User;
import com.bcn.authService.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }




    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        System.out.println("post token - " + token);
        boolean isValid = authService.isValidToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateTokenGet(@RequestParam("token") String token) {
        System.out.println("get token - " + token);
        boolean isValid = authService.isValidToken(token);
        return ResponseEntity.ok(isValid);
    }



}
