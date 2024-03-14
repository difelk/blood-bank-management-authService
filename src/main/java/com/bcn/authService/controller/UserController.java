package com.bcn.authService.controller;

import com.bcn.authService.data.AuthenticationResponse;
import com.bcn.authService.data.User;
import com.bcn.authService.data.UserResponse;
import com.bcn.authService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers(){
    return userService.getAllUsers();
    }

    @DeleteMapping("/{nic}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> deleteUserByNic(@PathVariable String nic) {
        return ResponseEntity.ok(userService.deleteUserByNic(nic));
    }

}
