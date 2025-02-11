package com.zerobase.lms.member.service.impl;

import com.zerobase.lms.admin.dto.MemberDto;
import com.zerobase.lms.admin.mapper.MemberMapper;
import com.zerobase.lms.admin.model.MemberParam;
import com.zerobase.lms.components.MailComponents;
import com.zerobase.lms.member.entity.Member;
import com.zerobase.lms.member.entity.MemberCode;
import com.zerobase.lms.member.exception.MemberNotEmailAuthException;
import com.zerobase.lms.member.exception.MemberStopUserException;
import com.zerobase.lms.member.model.MemberInput;
import com.zerobase.lms.member.model.ResetPasswordInput;
import com.zerobase.lms.member.repository.MemberRepository;
import com.zerobase.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    private final MemberMapper memberMapper;

    @Override
    public boolean register(MemberInput parameter) {

        /**
         * 회원가입
         */
        // 이미 userId 로 가입이 되어있는지 확인
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        // 현재 userId에 해당하는 데이터 존재하면 false 반환
        if (optionalMember.isPresent()) {
            return false;
        }

        // 비밀번호 암호화
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        // email 인증 키를 UUID를 사용하여 랜덤 값으로 생성 -> string 형식으로 변경
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(encPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                // 회원가입을 처음 했을때는 가입 요청중 상태 부여
                .userStatus(Member.MEMBER_STATUS_REQ)
                .build();

        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "lms 사이트 가입을 축하드립니다.";
        // target='_blank' -> 계속해서 새 창으로 열림
        String text = "<p>lms 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'> 가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);


        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 이미 이메일 인증이 완료되었다면 return false
        if (member.isEmailAuthYn()) {
            return false;
        }

        // 이메일 인증을 완료한 후에는 '현재 이용 가능한 상태' 부여
        member.setUserStatus(MemberCode.MEMBER_STATUS_ING);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    // 비밀번호 초기화
    @Override
    public boolean sendResetPassword(ResetPasswordInput parameter) {
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(parameter.getUserId(), parameter.getUserName());
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        // resetPasswordKey 에 들어갈 값 새로 생성(UUID)
        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "[lms] 비밀번호 초기화 메일 입니다. ";
        String text = "<p>lms 비밀번호 초기화 메일 입니다.<p>" + "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요.</p>"+
                "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id=" + uuid + "'> 비밀번호 초기화 링크 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {

        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다");
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 체크 필수
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);
        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 체크 필수
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        return true;
    }

    @Override
    public List<MemberDto> list(MemberParam parameter) {

//        MemberDto parameter = new MemberDto();

        long totalCount = memberMapper.selectListCount(parameter);
        List<MemberDto> list = memberMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
//        return memberRepository.findAll();

    }

    @Override
    public MemberDto detail(String userId) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();

        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setPassword(encPassword);
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 현재 부여된 사용자 상태에 따라서 검증
        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())) {
            throw new MemberNotEmailAuthException("이메일 인증 이후에 로그인을 해주세요.");
        }
        // 정지된 회원일 경우
        if (Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        // 이메일 인증을 받지 않았다면
//        if (!member.isEmailAuthYn()) {
//            throw new MemberNotEmailAuthException("이메일 인증 이후에 로그인을 해주세요.");
//        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 관리자 롤 추가
        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
