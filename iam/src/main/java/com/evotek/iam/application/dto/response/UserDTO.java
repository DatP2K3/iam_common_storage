package com.evotek.iam.application.dto.response;

import java.util.UUID;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID selfUserID;
    private String provider;
    private UUID providerId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String dob;
    private String street;
    private String ward;
    private String district;
    private String city;
    private int yearsOfExperience;
    private UUID avatarFileId;
}
