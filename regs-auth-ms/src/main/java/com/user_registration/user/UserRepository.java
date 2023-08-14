package com.user_registration.user;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    void updatePassword(@Param("userId") int userId, @Param("newPassword") String newPassword);

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId AND u.password = :oldPassword")
    int updatePasswordWithOld(@Param("userId") int userId, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
}

