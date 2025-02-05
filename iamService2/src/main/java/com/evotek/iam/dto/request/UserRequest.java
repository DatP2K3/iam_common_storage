package com.evotek.iam.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "UserName cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&-+=()!?\"]).{8,128}$", message = "Password must be at least 8 characters long and contain at least one letter and one number one special character and one uppercase letter")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String firstName;

    private String lastName;

    @Past(message = "Date of birth must be a past date.")
    private LocalDate dob;

    private String street;

    private String ward;

    private String district;

    private String city;

    private int yearsOfExperience;

    private String role;

}