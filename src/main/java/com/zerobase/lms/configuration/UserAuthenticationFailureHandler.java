package com.zerobase.lms.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String msg = "로그인에 실패하였습니다";

        // 이메일 인증을 받지 않았을때 나오는 오류를 잡음 -> msg가 이메일 인증 이후에 로그인을 해주세요. 로 바뀜
        if (exception instanceof InternalAuthenticationServiceException) {
            msg = exception.getMessage();
        }

        // 로그인 페이지로 리다이렉트하면서 에러 메시지를 쿼리 파라미터로 전달
        response.sendRedirect("/member/login?error=true&message=" + URLEncoder.encode(msg, "UTF-8"));

        System.out.println("로그인에 실패하였습니다.");

//        setUseForward(true);
//        setDefaultFailureUrl("/member/login?error=true");
//        request.setAttribute("errorMessage", msg);
//
//        System.out.println("로그인에 실패하였습니다.");
//
//        super.onAuthenticationFailure(request, response, exception);
    }
}
