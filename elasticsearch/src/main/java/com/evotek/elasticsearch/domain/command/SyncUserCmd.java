package com.evotek.elasticsearch.domain.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SyncUserCmd {
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
