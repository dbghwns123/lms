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
public class Member implements MemberCode{

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

    // 관리자 여부를 지정할 것인지
    // 회원에 따른 ROLE 을 지정할 것인지 정해야함
    // 준회원/정회원/특별회원/관리자
    // ROLE_SEMI_USER, ROLE_USER, ROLE_SPECIAL_USER,ROLE_ADMIN
    // 여기서는 단순히 관리자인지 일반 유저인지에 대해서만 관리
    private boolean adminYn;

    // enum 으로 변경해도 좋음 (MemberCode 인터페이스 상속)
    private String userStatus; // 현재 user의 계정이 이용가능한 상태인지, 정지상태인지

}
