package com.evotek.elasticsearch.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evotek.elasticsearch.domain.User;
import com.evotek.elasticsearch.infrastructure.persistence.document.UserDocument;

@Mapper(componentModel = "Spring")
public interface UserDocumentMapper extends DocumentMapper<User, UserDocument> {}
