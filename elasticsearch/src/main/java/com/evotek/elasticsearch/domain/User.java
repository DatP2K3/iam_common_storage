package com.evotek.elasticsearch.domain;

import com.evotek.elasticsearch.domain.command.SyncUserCmd;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class User {
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

    public User(SyncUserCmd syncUserCmd) {
        this.selfUserID = syncUserCmd.getSelfUserID();
        this.providerId = syncUserCmd.getProviderId();
        this.username = syncUserCmd.getUsername();
        this.email = syncUserCmd.getEmail();
        this.firstName = syncUserCmd.getFirstName();
        this.lastName = syncUserCmd.getLastName();
        this.avatarFileId = syncUserCmd.getAvatarFileId();
        this.dob = syncUserCmd.getDob();
        this.street = syncUserCmd.getStreet();
        this.ward = syncUserCmd.getWard();
        this.district = syncUserCmd.getDistrict();
        this.city = syncUserCmd.getCity();
        this.yearsOfExperience = syncUserCmd.getYearsOfExperience();
        this.password = syncUserCmd.getPassword();
        this.locked = syncUserCmd.isLocked();
        this.provider = syncUserCmd.getProvider();
        this.roleId = syncUserCmd.getRoleId();
    }

    public void update(SyncUserCmd syncUserCmd) {
        if(syncUserCmd.getEmail() != null) {
            this.email = syncUserCmd.getEmail();
        }
        if(syncUserCmd.getFirstName() != null) {
            this.firstName = syncUserCmd.getFirstName();
        }
        if(syncUserCmd.getLastName() != null) {
            this.lastName = syncUserCmd.getLastName();
        }
        if(syncUserCmd.getAvatarFileId() != null) {
            this.avatarFileId = syncUserCmd.getAvatarFileId();
        }
        if(syncUserCmd.getDob() != null) {
            this.dob = syncUserCmd.getDob();
        }
        if(syncUserCmd.getStreet() != null) {
            this.street = syncUserCmd.getStreet();
        }
        if(syncUserCmd.getWard() != null) {
            this.ward = syncUserCmd.getWard();
        }
        if(syncUserCmd.getDistrict() != null) {
            this.district = syncUserCmd.getDistrict();
        }
        if(syncUserCmd.getCity() != null) {
            this.city = syncUserCmd.getCity();
        }
        if(syncUserCmd.getYearsOfExperience() != 0) {
            this.yearsOfExperience = syncUserCmd.getYearsOfExperience();
        }
        if(syncUserCmd.getPassword() != null) {
            this.password = syncUserCmd.getPassword();
        }
        if(syncUserCmd.isLocked() != false) {
            this.locked = syncUserCmd.isLocked();
        }
        if(syncUserCmd.getRoleId() != null) {
            this.roleId = syncUserCmd.getRoleId();
        }
    }
}
