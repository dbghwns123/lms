package com.zerobase.lms.components;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponents {

    private final JavaMailSender javaMailSender;

    // build.gradle 에서 mail 관련 라이브러리 추가, yml 설정 후 간단한 이메일 보내보기(테스트용) -> 성공
    public void sendMailTest() {

        SimpleMailMessage msg = new SimpleMailMessage();
        // 메일을 보낼 대상
        msg.setTo("hojoon1011@gmail.com");
        // 메일 제목
        msg.setSubject("안녕하세요.");
        // 메일 내용
        msg.setText("안녕하세요. 테스트 중입니다.");

        javaMailSender.send(msg);
    }

    public boolean sendMail(String mail, String subject, String text) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                // 메일을 보낼 대상
                mimeMessageHelper.setTo(mail);
                // 메일 제목
                mimeMessageHelper.setSubject(subject);
                // 메일 내용 (내용과, html 설정)
                mimeMessageHelper.setText(text, true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }



}
