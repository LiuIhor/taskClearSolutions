package com.example.testassignmentforjuniorjava.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private String phone;
}