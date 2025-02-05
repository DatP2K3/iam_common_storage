package com.evotek.iam.service.common;

import com.evotek.iam.dto.request.UserSearchRequest;
import com.evotek.iam.dto.response.UserResponse;
import com.evotek.iam.exception.AppException;
import com.evotek.iam.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jxls.builder.JxlsOutput;
import org.jxls.builder.JxlsOutputFile;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserExportService { //convert date sang string de hien thi, template cũng đễ string
    private final UserService userService;
    public void exportUserListToExcel(UserSearchRequest userSearchRequest) {
        List<UserResponse> users = userService.search(userSearchRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        try (InputStream inputStream = new ClassPathResource("templates/user_template.xlsx").getInputStream()) {
            JxlsOutput output = new JxlsOutputFile(new File("exports/output_user_data.xlsx"));
            JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(inputStream)
                    .build()
                    .fill(data, output);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
