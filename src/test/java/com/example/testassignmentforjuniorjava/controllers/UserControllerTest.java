package com.example.testassignmentforjuniorjava.controllers;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.entities.User;
import com.example.testassignmentforjuniorjava.exceptions.CustomException;
import com.example.testassignmentforjuniorjava.exceptions.UserNotFoundException;
import com.example.testassignmentforjuniorjava.mapper.UserMapperUtil;
import com.example.testassignmentforjuniorjava.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void saveUserIfUserIsCorrect() throws Exception {
        User user = createUser();
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(user);
        when(userService.saveUser(any())).thenReturn(userGetDto);
        String content = "{\"email\":       \"test@gmail.com\",\n" +
                "          \"firstName\":   \"test\",\n" +
                "          \"lastName\":    \"test\",\n" +
                "          \"birthday\":    \"2000-10-10\",\n" +
                "          \"phone\":      \"+380951111111\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveUserIfUserIsNotCorrect() throws Exception {
        User user = createUser();
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(user);
        when(userService.saveUser(any())).thenReturn(userGetDto);
        String content = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllUsersIfParamIncorrect() throws Exception {
        when(userService.getAllUsers(any(), any())).thenThrow(new CustomException("'From' must be less than 'To'!"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("from", "2010-10-10")
                        .param("to", "2000-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllUsersIfParamIsCorrect() throws Exception {
        User user = createUser();
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(user);
        when(userService.getAllUsers(any(), any())).thenReturn(List.of(userGetDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("from", "2000-10-10")
                        .param("to", "2001-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUserById() throws Exception {
        doNothing().when(userService).deleteUserById(any());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void replaceUserByIdIfUserIsExist() throws Exception {
        User user = createUser();
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(user);
        String content = "{\"email\":       \"test@gmail.com\",\n" +
                "          \"firstName\":   \"test\",\n" +
                "          \"lastName\":    \"test\",\n" +
                "          \"birthday\":    \"2000-10-10\",\n" +
                "          \"phone\":      \"+380951111111\"}";
        when(userService.replaceUserById(any(), any())).thenReturn(userGetDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void replaceUserByIdIfUserIsNotExist() throws Exception {
        Long userId = 1L;
        String content = "{\"email\":       \"test@gmail.com\",\n" +
                "          \"firstName\":   \"test\",\n" +
                "          \"lastName\":    \"test\",\n" +
                "          \"birthday\":    \"2000-10-10\",\n" +
                "          \"phone\":      \"+380951111111\"}";
        when(userService.replaceUserById(any(), any())).thenThrow(new UserNotFoundException(String.format("User with id %d does not exists!", userId)));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void changeUserByIdIfUserIsExist() throws Exception {
        User user = createUser();
        Long userId = 1L;
        String content = "{\"email\":       \"test@gmail.com\",\n" +
                "          \"firstName\":   \"test\",\n" +
                "          \"lastName\":    \"test\",\n" +
                "          \"birthday\":    \"2000-10-10\",\n" +
                "          \"phone\":      \"+380951111111\"}";
        when(userService.changeUserById(any(), any())).thenThrow(new UserNotFoundException(String.format("User with id %d does not exists!", userId)));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void changeUserByIdIfUserIsNotExist() throws Exception {
        User user = createUser();
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(user);
        String content = "{\"email\":       \"test@gmail.com\",\n" +
                "          \"firstName\":   \"test\",\n" +
                "          \"lastName\":    \"test\",\n" +
                "          \"birthday\":    \"2000-10-10\",\n" +
                "          \"phone\":      \"+380951111111\"}";
        when(userService.changeUserById(any(), any())).thenReturn(userGetDto);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    User createUser() {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("1999-05-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new User(
                1L,
                "test@gmail.com",
                "test",
                "test",
                date,
                "+380957226489"
        );
    }
}