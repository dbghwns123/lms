package com.zerobase.lms.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Member {

    @Id
    private String userId;

    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDt;

    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;
    private String emailAuthKey;

    // 비밀번호 초기화 할 때 새로 생성되는 키 값
    private String resetPasswordKey;
    // 비밀번호 초기화를 한 이후 유효기간
    private LocalDateTime resetPasswordLimitDt;

}
