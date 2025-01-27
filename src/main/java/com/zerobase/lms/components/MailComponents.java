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

    // build.gradle에서 mail 관련 라이브러리 추가, yml 설정 후 간단한 이메일 보내보기(테스트용) -> 성공
    public void sendMailTest() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("hojoon1011@gmail.com");
        msg.setSubject("안녕하세요.");
        msg.setText("안녕하세요. 테스트 중입니다.");

        javaMailSender.send(msg);
    }

    public boolean sendMail(String mail, String subject, String text) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
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
