package com.example.testassignmentforjuniorjava.services;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.dtos.UserPostDto;
import com.example.testassignmentforjuniorjava.entities.User;
import com.example.testassignmentforjuniorjava.exceptions.CustomException;
import com.example.testassignmentforjuniorjava.exceptions.UserNotFoundException;
import com.example.testassignmentforjuniorjava.mapper.UserMapperUtil;
import com.example.testassignmentforjuniorjava.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getAllUsersIfDbIsNotEmpty() {
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(createUser());
        List<User> users = new ArrayList<>();
        users.add(createUser());
        when(userRepository.findAll()).thenReturn(users);
        List<UserGetDto> actual = userService.getAllUsers();
        List<UserGetDto> expected = List.of(userGetDto);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsersIfDbIsEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<UserGetDto> actual = userService.getAllUsers();
        List<UserGetDto> expected = new ArrayList<>();
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsersIfRequestParamsIsCorrect() throws ParseException {
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(createUser());
        List<User> users = new ArrayList<>();
        users.add(createUser());
        when(userRepository.findAllByBirthdayBetween(any(), any())).thenReturn(users);
        Date from = new SimpleDateFormat("yyyy-MM-dd").parse("1999-05-12");
        Date to = new SimpleDateFormat("yyyy-MM-dd").parse("2001-05-12");
        List<UserGetDto> actual = userService.getAllUsers(from, to);
        List<UserGetDto> expected = List.of(userGetDto);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAllByBirthdayBetween(any(), any());
    }

    @Test
    void getAllUsersIfToBeforeFrom() throws ParseException {
        Date from = new SimpleDateFormat("yyyy-MM-dd").parse("2001-05-12");
        Date to = new SimpleDateFormat("yyyy-MM-dd").parse("1999-05-12");
        Exception exception = assertThrows(CustomException.class, () -> {
            userService.getAllUsers(from, to);
        });
        assertEquals("'From' must be less than 'To'!", exception.getMessage());
        verify(userRepository, times(0)).findAllByBirthdayBetween(any(), any());
    }

    @Test
    void getAllUsersIfToOrFromIsNull() {
        UserGetDto userGetDto = UserMapperUtil.convertToGetDto(createUser());
        List<UserGetDto> expected = List.of(userGetDto);
        when(userRepository.findAll()).thenReturn(List.of(createUser()));
        List<UserGetDto> actual = userService.getAllUsers(null, null);
        assertEquals(expected, actual);
        verify(userRepository, times(0)).findAllByBirthdayBetween(any(), any());
    }

    @Test
    void saveUser() {
        User user = createUser();
        when(userRepository.save(any())).thenReturn(user);
        UserPostDto userPostDto = UserMapperUtil.convertToPostDto(user);
        UserGetDto actual = userService.saveUser(userPostDto);
        UserGetDto expected = UserMapperUtil.convertToGetDto(user);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteUserById() {
        Long id = 1L;
        doNothing().when(userRepository).deleteById(id);
        userService.deleteUserById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void replaceUserByIdIfUserDoesNotExit() {
        UserPostDto userPostDto = UserMapperUtil.convertToPostDto(createUser());
        Long id = 1L;
        when(userRepository.existsById(any())).thenReturn(false);
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.replaceUserById(userPostDto, id);
        });
        assertEquals(String.format("User with id %d does not exists!", id),
                exception.getMessage());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void replaceUserByIdIfUserDoesExit() {
        UserPostDto userPostDto = UserMapperUtil.convertToPostDto(createUser());
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.save(createUser())).thenReturn(createUser());
        UserGetDto actual = userService.replaceUserById(userPostDto, id);
        UserGetDto expected = UserMapperUtil.convertToGetDto(createUser());
        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void changeUserByIdIfUserDoesNotExit() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.changeUserById(id, new HashMap<>());
        });
        assertEquals(String.format("User with id %d does not exist!", id),
                exception.getMessage());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void changeUserByIdIfUserDoesExit() {
        User user = createUser();
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstname", "Changed firstname");
        user.setFirstName("Changed firstname");
        when(userRepository.save(user)).thenReturn(user);
        UserGetDto actual = userService.changeUserById(id, fields);
        UserGetDto expected = UserMapperUtil.convertToGetDto(user);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(any());
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