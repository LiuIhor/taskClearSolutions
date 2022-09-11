package com.example.testassignmentforjuniorjava.repositories;

import com.example.testassignmentforjuniorjava.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method to get all users whose birthday is between from and to
     *
     * @param from Start position
     * @param to   Finish position
     * @return List of Users
     */
    List<User> findAllByBirthdayBetween(Date from, Date to);
}