package com.evotek.iam.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String street;

    private String ward;

    private String district;

    private String city;

    private int yearsOfExperience;
}
