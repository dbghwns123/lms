package com.zerobase.lms.member.model;

import lombok.Data;

@Data
public class ResetPasswordInput {

    private String userId;
    private String userName;

    private String id;
    private String password;
}
