package com.example.testassignmentforjuniorjava.services;

import com.example.testassignmentforjuniorjava.dtos.UserGetDto;
import com.example.testassignmentforjuniorjava.dtos.UserPostDto;
import com.example.testassignmentforjuniorjava.entities.User;
import com.example.testassignmentforjuniorjava.exceptions.CustomException;
import com.example.testassignmentforjuniorjava.exceptions.UserNotFoundException;
import com.example.testassignmentforjuniorjava.mapper.UserMapperUtil;
import com.example.testassignmentforjuniorjava.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserGetDto> getAllUsers(Date from, Date to) {
        if (from == null || to == null) {
            return userRepository.findAll()
                    .stream()
                    .map(UserMapperUtil::convertToGetDto)
                    .collect(Collectors.toList());
        } else if (to.before(from)) {
            throw new CustomException("'From' must be less than 'To'!");
        }
        return userRepository.findAllByBirthdayBetween(from, to)
                .stream()
                .map(UserMapperUtil::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserGetDto saveUser(UserPostDto userPostDto) {
        User user = UserMapperUtil.convertFromPostDtoToEntity(userPostDto);
        return UserMapperUtil.convertToGetDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserGetDto replaceUserById(UserPostDto userPostDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id %d does not exists!", userId));
        }
        User user = UserMapperUtil.convertFromPostDtoToEntity(userPostDto);
        user.setId(userId);
        return UserMapperUtil.convertToGetDto(userRepository.save(user));
    }

    @Override
    public UserGetDto changeUserById(Long userId, Map<String, Object> fields) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserPostDto userPostDto = UserMapperUtil.convertToPostDto(userOptional.get());
            fields.forEach((key, value) -> {
                switch (key) {
                    case "firstname":
                        userPostDto.setFirstName((String) value);
                        break;
                    case "lastname":
                        userPostDto.setLastName((String) value);
                        break;
                    case "email":
                        userPostDto.setEmail((String) value);
                        break;
                    case "phone":
                        userPostDto.setPhone((String) value);
                        break;
                    case "birthday":
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = formatter.parse((String) value);
                        } catch (ParseException e) {
                            throw new CustomException(e.getMessage());
                        }
                        userPostDto.setBirthday(date);
                        break;
                }
            });
            User user = UserMapperUtil.convertFromPostDtoToEntity(userPostDto);
            user.setId(userId);
            return UserMapperUtil.convertToGetDto(userRepository.save(user));
        } else {
            throw new UserNotFoundException(String.format("User with id %d does not exist!", userId));
        }
    }
}