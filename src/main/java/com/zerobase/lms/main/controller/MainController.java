package com.zerobase.lms.main.controller;

import com.zerobase.lms.components.MailComponents;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MailComponents mailComponents;

    @RequestMapping("/")
    public String index() {

        // 메일 보내기 테스트용 코드
//        String email = "hojoon1011@gmail.com";
//        String subject = " 안녕하세요.";
//        String text = "<p> 안녕하세요.</p><p>테스트입니다.</p>";
//
//        mailComponents.sendMail(email, subject, text);

        return "index";
    }

    @RequestMapping("/hello")
    public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter printWriter = response.getWriter();

        String msg = "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "</head>" +
                "<body>" +
                "<p>hello</p> " +
                "<p>fastlms website!!!</p>" +
                "<p> 안녕하세요!!! ===>  </p>" +
                "</body>" +
                "</html>";

        printWriter.write(msg);
        printWriter.close();
    }
}
