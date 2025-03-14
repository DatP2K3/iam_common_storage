package com.evo.common.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncUserRequest {
    private UUID selfUserID;
    private UUID providerId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UUID avatarFileId;
    private LocalDate dob;
    private String street;
    private String ward;
    private String district;
    private String city;
    private int yearsOfExperience;
    private String password;
    private boolean locked;
    private String provider;
    private UUID roleId;
}
