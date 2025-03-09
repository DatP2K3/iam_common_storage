package com.evotek.iam.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.evo.common.entity.AuditEntity;

import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserEntity extends AuditEntity {
    @Id
    @Column(name = "self_user_id")
    private UUID selfUserID;

    @Column(name = "provider_id", unique = true)
    private String providerId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_file_id")
    private UUID avatarFileId;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "street")
    private String street;

    @Column(name = "ward")
    private String ward;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @Column(name = "password")
    private String password;

    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    @Column(name = "provider")
    private String provider;
}
