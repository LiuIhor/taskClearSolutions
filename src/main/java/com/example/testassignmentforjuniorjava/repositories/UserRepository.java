package com.example.testassignmentforjuniorjava.repositories;

import com.example.testassignmentforjuniorjava.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
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