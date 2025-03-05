package com.evo.common.mapper;

import org.springframework.stereotype.Component;

import com.evo.common.enums.TemplateCode;

import lombok.Getter;

@Component
@Getter
public class TemplateCodeMapper {
    public String mapToTemplateName(TemplateCode templateCode) {
        try {
            return templateCode.getTemplateName();
        } catch (IllegalArgumentException e) {
            return "default";
        }
    }

    public String getSubject(TemplateCode templateCode) {
        try {
            return templateCode.getSubject();
        } catch (IllegalArgumentException e) {
            return "Notification";
        }
    }
}
