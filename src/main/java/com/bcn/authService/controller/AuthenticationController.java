package com.bcn.authService.controller;

import com.bcn.authService.data.AuthenticationResponse;
import com.bcn.authService.data.User;
import com.bcn.authService.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//        System.out.println("submited user data from controller  : \nfirstname " + request.getFirstName() + "\nlastname " + request.getLastName() + "\n" +
//                "username " + request.getUsername() + "\npassword " + request.getPassword() + "\nrole " + request.getRole() + "\nnic " + request.getNic()
//                + "\ncontact no " + request.getContactNo() + "\nblood type " + request.getBloodType()
//                + "\nno " + request.getAddressNo() + "\nstreet " + request.getStreet()
//                + "\ncity " + request.getCity() + "\nbirthday " + request.getBirthday()
//                + "\norganization type " + request.getOrganizationType()
//                + "\norganization " + request.getOrganization()
//        );


        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        System.out.println("recived data from frontend");
        System.out.println("data list - " + request.getUsername() + "\n password: " + request.getPassword());
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
