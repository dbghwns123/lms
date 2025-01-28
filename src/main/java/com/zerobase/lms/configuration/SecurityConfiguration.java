package com.zerobase.lms.configuration;

import com.zerobase.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static java.rmi.server.RMISocketFactory.getFailureHandler;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final MemberService memberService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/member/register", "/member/email-auth").permitAll() // 특정 URL 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                // 로그인 설정
                .formLogin(login -> login
                        .loginPage("/member/login") // 로그인 페이지 경로
                        .failureHandler(getFailureHandler()) // 로그인 실패 핸들러 적용
                        .permitAll()
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 요청 경로
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트 경로
                        .invalidateHttpSession(true) // 세션 초기화
                );

                // 예외 처리
//                .exceptionHandling(exception -> exception
//                        .accessDeniedPage("/error/denied") // 접근 권한 없는 경우 리다이렉트 경로
//                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(memberService); // MemberService 사용
        authProvider.setPasswordEncoder(getPasswordEncoder()); // 패스워드 인코더 설정
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

/**
 * 현재는 지원되지 않는 방식
 */

/*
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/"
                        , "/member/register"
                        , "/member/email-auth"
                        , "/member/find-password"
                )
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN");

        http.formLogin()
                .loginPage("/member/login")
                .failureHandler(getFailureHandler())
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                .accessDeniedPage("/error/denied");

        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());

        super.configure(auth);
    }
}
 */
