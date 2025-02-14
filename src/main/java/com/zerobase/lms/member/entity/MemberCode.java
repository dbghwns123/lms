package com.zerobase.lms.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public interface MemberCode {

    /**
     * 현재 가입 요청중
     */
    String MEMBER_STATUS_REQ = "REQ";

    /**
     * 현재 이용중인 상태
     */
    String MEMBER_STATUS_ING = "ING";

    /**
     * 현재 정지된 상태
     */
    String MEMBER_STATUS_STOP = "STOP";

    /**
     * 현재 탈퇴된 회원
     */
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";
}
