package com.bcn.authService.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.nic =?1")
    Optional<User> findUserByNic(@Param("nic") String nic);

    @Query("select u from User u where u.username =?1")
    Optional<User> findUserByUsername(@Param("username") String username);

    @Modifying
    @Query("delete from User d where d.nic = :nic")
    void deleteUserByNic(@Param("nic") String nic);


    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.nic = :nic")
    void resetPassword(@Param("nic") String nic, @Param("password") String password);


}
