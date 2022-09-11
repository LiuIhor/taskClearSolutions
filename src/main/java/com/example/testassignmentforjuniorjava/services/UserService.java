package com.example.testassignmentforjuniorjava.services;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.dtos.UserPostDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * Method to get all users whose birthday is between from and to
     *
     * @param from Start position
     * @param to   Finish position
     * @return List of Users
     */
    List<UserGetDto> getAllUsers(Date from, Date to);

    /**
     * Method to add new user in db
     *
     * @param userPostDto The user with the fields we want to add to the database
     * @return Saved user
     */
    UserGetDto saveUser(UserPostDto userPostDto);

    /**
     * Method to delete user by id
     *
     * @param userId ID of the user we want to delete
     */
    void deleteUserById(Long userId);

    /**
     * Method to change all fields of the user
     *
     * @param userPostDto User with new data
     * @param userId      ID of the user we want to change
     * @return Changed user
     */
    UserGetDto replaceUserById(UserPostDto userPostDto, Long userId);

    /**
     * Method to change some fields of the user
     *
     * @param userId ID of the user we want to change
     * @param fields Map of fields, where key - name field, value - value of field.
     * @return Changed user
     */
    UserGetDto changeUserById(Long userId, Map<String, Object> fields);
}