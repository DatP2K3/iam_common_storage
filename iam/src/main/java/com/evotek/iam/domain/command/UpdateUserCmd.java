package com.evotek.iam.domain.command;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserCmd {
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
