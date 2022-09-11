package com.example.testassignmentforjuniorjava.mapper;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.dtos.UserPostDto;
import com.example.testassignmentforjuniorjava.entities.User;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserMapperUtil {

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Method to convert UserPostDto to User
     *
     * @param userPostDto An instance of type UserPostDto that we want to convert
     * @return Converted instance to User
     */
    public User convertFromPostDtoToEntity(UserPostDto userPostDto) {
        return modelMapper.map(userPostDto, User.class);
    }

    /**
     * Method to convert UserGetDto to User
     *
     * @param userGetDto An instance of type UserGetDto that we want to convert
     * @return Converted instance to User
     */
    public User convertFromGetDtoToEntity(UserGetDto userGetDto) {
        return modelMapper.map(userGetDto, User.class);
    }

    /**
     * Method to convert User to UserPostDto
     *
     * @param user An instance of type User that we want to convert
     * @return Converted instance to UserPostDto
     */
    public UserPostDto convertToPostDto(User user) {
        return modelMapper.map(user, UserPostDto.class);
    }

    /**
     * Method to convert User to UserGetDto
     *
     * @param user An instance of type User that we want to convert
     * @return Converted instance to UserGetDto
     */
    public UserGetDto convertToGetDto(User user) {
        return modelMapper.map(user, UserGetDto.class);
    }
}