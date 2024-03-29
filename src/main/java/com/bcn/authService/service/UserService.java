package com.bcn.authService.service;
import com.bcn.authService.data.Password;
import com.bcn.authService.data.User;
import com.bcn.authService.data.UserRepository;
import com.bcn.authService.data.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findUserByNic(String nic){
        try {
            Optional<User> user = userRepository.findUserByNic(nic);
            System.out.println("user - " + user);
            if(user.isPresent()) {
                return user.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }

    }

    public User getUserByUsername(String username) {
        try {
            Optional<User> user = userRepository.findUserByUsername(username);
            System.out.println("user - " + user);
            if(user.isPresent()) {
                return user.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }
    }

    public UserResponse updateUser(User data){
        UserResponse userResponse = new UserResponse();
        User existingUser = findUserByNic(data.getNic());
        try {
            if (existingUser != null) {
                existingUser.setFirstName(data.getFirstName());
                existingUser.setLastName(data.getLastName());
                existingUser.setNic(data.getNic());
                existingUser.setUsername(data.getUsername());
                existingUser.setBirthday(data.getBirthday());
                existingUser.setBloodType(data.getBloodType());
                existingUser.setContactNo(data.getContactNo());
                existingUser.setAddressNo(data.getAddressNo());
                existingUser.setStreet(data.getStreet());
                existingUser.setCity(data.getCity());
                existingUser.setOrganization(data.getOrganization());
                existingUser.setOrganizationType(data.getOrganizationType());
                existingUser.setPassword(data.getPassword());
                existingUser.setRole(data.getRole());

                userRepository.save(existingUser);
                userResponse.setMessage("user update successful");
                userResponse.setStatus(200);
            } else {
                userResponse.setMessage("user does not exist");
                userResponse.setStatus(404);
            }
        } catch(Exception e){
            userResponse.setMessage("ERROR: " + e.getMessage());
            userResponse.setStatus(500);
        }
        return userResponse;
    }


    public UserResponse updatePassword(Password request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("inside of service method");
        UserResponse userResponse = new UserResponse();
        System.out.println("request.password = " + request.getNewPassword());
        Password password = new Password();
        password.setNic(request.getNic());
        password.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        try{
            userRepository.resetPassword(password.getNic(), password.getNewPassword());
            userResponse.setMessage("password reset Successful");
            userResponse.setStatus(200);
        }catch (Exception e){
            userResponse.setMessage("password reset Failed, with " + e.getMessage());
            userResponse.setStatus(200);
        }

        return userResponse;


    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            System.out.println("Failed to fetch all users: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public UserResponse deleteUserByNic(String nic) {
        UserResponse userResponse = new UserResponse();
        User user = findUserByNic(nic);
        if (user != null) {
            try {
                userRepository.deleteUserByNic(nic);
                User deletedUser = findUserByNic(nic);
                if (deletedUser == null) {
                    userResponse.setMessage("User delete successful");
                    userResponse.setStatus(200);
                } else {
                    userResponse.setMessage("Something went wrong");
                    userResponse.setStatus(500);
                }
            } catch (Exception e) {
                userResponse.setMessage("Failed to delete user: " + e.getMessage());
                userResponse.setStatus(500);
            }
        } else {
            userResponse.setMessage("User does not exist");
            userResponse.setStatus(404);
        }
        return userResponse;
    }


}
