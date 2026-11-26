package com.capico.user.repository;

import com.capico.user.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository
        extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findAllByOrderByActivityDateDesc();

    List<UserActivity> findByUserIdOrderByActivityDateDesc(
            Long userId
    );
}