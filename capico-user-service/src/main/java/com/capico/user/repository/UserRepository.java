package com.capico.user.repository;

import com.capico.user.entity.User;
import com.capico.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndUserIdNot(
            String email,
            Long userId
    );

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByMobileNumberAndUserIdNot(
            String mobileNumber,
            Long userId
    );

    List<User> findAllByStatus(UserStatus status);

    Optional<User> findByUserIdAndStatus(
            Long userId,
            UserStatus status
    );

    Optional<User> findByEmailIgnoreCaseAndStatus(
            String email,
            UserStatus status
    );

    long countByStatus(UserStatus status);
}