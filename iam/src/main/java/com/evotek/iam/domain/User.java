package com.evotek.iam.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.iam.domain.command.CreateUserCmd;
import com.evotek.iam.domain.command.UpdateUserCmd;
import com.evotek.iam.infrastructure.support.IdUtils;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class User extends Auditor {
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
    private UserRole userRole;
    private UserActivityLog userActivityLog;

    public User(CreateUserCmd cmd) {
        this.selfUserID = IdUtils.nextId();
        this.username = cmd.getUsername();
        this.password = cmd.getPassword();
        this.email = cmd.getEmail();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.dob = cmd.getDob();
        this.street = cmd.getStreet();
        this.ward = cmd.getWard();
        this.district = cmd.getDistrict();
        this.city = cmd.getCity();
        this.yearsOfExperience = cmd.getYearsOfExperience();
        this.provider = cmd.getProvider();
        this.locked = false;
        this.providerId = cmd.getProviderId();
        if (cmd.getUserRole() != null) {
            this.userRole = new UserRole(cmd.getUserRole().getRoleId(), this.selfUserID);
        }
    }

    public void update(UpdateUserCmd cmd) {
        if (cmd.getEmail() != null) {
            this.email = cmd.getEmail();
        }
        if (cmd.getFirstName() != null) {
            this.firstName = cmd.getFirstName();
        }
        if (cmd.getLastName() != null) {
            this.lastName = cmd.getLastName();
        }
        if (cmd.getDob() != null) {
            this.dob = cmd.getDob();
        }
        if (cmd.getStreet() != null) {
            this.street = cmd.getStreet();
        }
        if (cmd.getWard() != null) {
            this.ward = cmd.getWard();
        }
        if (cmd.getDistrict() != null) {
            this.district = cmd.getDistrict();
        }
        if (cmd.getCity() != null) {
            this.city = cmd.getCity();
        }
        if (cmd.getYearsOfExperience() != 0) {
            this.yearsOfExperience = cmd.getYearsOfExperience();
        }
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeAvatar(UUID fileId) {
        this.avatarFileId = fileId;
    }
}
