package com.bcn.authService.service;
import com.bcn.authService.data.User;
import com.bcn.authService.data.UserRepository;
import com.bcn.authService.data.UserResponse;
import jakarta.transaction.Transactional;
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
        Optional<User> user = userRepository.findUserByNic(nic);
        System.out.println("user - " + user);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
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
        } catch (Exception e){
            userResponse.setMessage("ERROR: " + e.getMessage());
            userResponse.setStatus(500);
        }
        return userResponse;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserResponse deleteUserByNic(String nic) {
        UserResponse userResponse = new UserResponse();
        User user = findUserByNic(nic);
        if (user != null) {
            userRepository.deleteUserByNic(nic);
            User deletedUser = findUserByNic(nic);
            if (deletedUser == null) {
                userResponse.setMessage("user delete successful");
                userResponse.setStatus(200);
            } else {
                userResponse.setMessage("something went wrong");
                userResponse.setStatus(500);
            }
        } else {
            userResponse.setMessage("user does not exist");
            userResponse.setStatus(404);
        }
        return userResponse;
    }


}
