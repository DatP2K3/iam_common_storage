package com.evo.common.enums;

import lombok.Getter;

@Getter
public enum TemplateCode {
    LOGIN_ALERT("login-alert", "Security Alert - New Login Detected"),
    SIGNIN_ALERT("signin-alert", "Security Alert - Created New Account"),
    REQUEST_CHANGE_PASSWORD("requestChangePassword-alert", "Request Change Password"),
    PASSWORD_CHANGE_ALERT("changePassword-alert", "Password Changed Successfully"),
    LOCK_USER_ALERT("lockUser-alert", "Account Locked"),
    OTP_ALERT("otp-alert", "OTP Alert");

    private final String templateName;
    private final String subject;

    TemplateCode(String templateName, String subject) {
        this.templateName = templateName;
        this.subject = subject;
    }
}
