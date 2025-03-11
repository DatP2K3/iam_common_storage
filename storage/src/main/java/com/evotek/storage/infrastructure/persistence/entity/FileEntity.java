package com.evotek.storage.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.evo.common.entity.AuditEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "files")
public class FileEntity extends AuditEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "md5_name", nullable = false)
    private String md5Name;

    @Column(name = "file_type", nullable = false)
    private String fileType; // để hõ trợ vết api preview

    @Column(name = "file_height")
    private Integer fileHeight;

    @Column(name = "file_width")
    private Integer fileWidth;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}
