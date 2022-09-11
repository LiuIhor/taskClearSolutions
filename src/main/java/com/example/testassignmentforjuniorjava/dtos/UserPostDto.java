package com.example.testassignmentforjuniorjava.dtos;

import com.example.testassignmentforjuniorjava.validators.DataIsValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostDto {

    @Email(regexp = ".+[@].+[\\.].+", message = "You must use a valid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Firstname is mandatory")
    private String firstName;

    @NotBlank(message = "Lastname is mandatory")
    private String lastName;

    @NotNull(message = "Birthday is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Value must be earlier than current date")
    @DataIsValid(message = "It allows to register users who are more than [18] years old.")
    private Date birthday;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^(?:\\+38)?(0\\d{9})$", message = "You must use a valid phone format")
    private String phone;
}