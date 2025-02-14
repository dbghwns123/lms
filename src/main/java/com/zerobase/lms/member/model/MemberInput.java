package com.zerobase.lms.member.model;

import lombok.Data;

@Data
public class MemberInput {

    private String userId;
    private String userName;
    private String phone;
    private String password;

    private String newPassword;

    private String zipcode;
    private String addr;
    private String addrDetail;

}
