package com.example.testassignmentforjuniorjava.controllers;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.dtos.UserPostDto;
import com.example.testassignmentforjuniorjava.exceptions.CustomException;
import com.example.testassignmentforjuniorjava.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to get all users.
     * If you pass parameters 1 and 2, then a subset of users will be returned.
     * Returns all users by default
     *
     * @param from It is RequestParam for searching user in time span. Start position
     * @param to   It is RequestParam for searching user in time span. Finish position
     * @return List of Users
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGetDto> getAllUsers(@RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        if (from == null || to == null) {
            return userService.getAllUsers();
        } else if (to.before(from)) {
            throw new CustomException("'From' must be less than 'To'!");
        } else {
            return userService.getAllUsers(from, to);
        }
    }

    /**
     * Endpoint to add User
     *
     * @param userPostDto The user with the fields we want to add to the database
     * @return Saved user
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDto saveUser(@Valid @RequestBody UserPostDto userPostDto) {
        return userService.saveUser(userPostDto);
    }

    /**
     * Endpoint to delete user by id
     *
     * @param userId ID of the user we want to delete
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    /**
     * Endpoint to change all fields of the user
     *
     * @param userPostDto User with new data
     * @param userId      ID of the user we want to change
     * @return Changed user
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDto replaceUser(@Valid @RequestBody UserPostDto userPostDto,
                                  @PathVariable Long userId) {
        return userService.replaceUserById(userPostDto, userId);
    }

    /**
     * Endpoints to change some fields of the user
     *
     * @param userId ID of the user we want to change
     * @param fields Map of fields, where key - name field, value - value of field.
     * @return Changed user
     */
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDto changeUser(@PathVariable Long userId,
                                 @RequestBody Map<String, Object> fields) {
        return userService.changeUserById(userId, fields);
    }
}
