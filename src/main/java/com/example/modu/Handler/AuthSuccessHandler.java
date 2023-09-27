package com.example.modu.Handler;

import com.example.modu.entity.UserRoleEnum;
import com.example.modu.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("login Success");


//        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//        //UserRoleEnum role = ((UserDetails) authentication.getPrincipal()).getUser().getRole();
//
//        String token = jwtUtil.createToken(username, UserRoleEnum.USER);
//        jwtUtil.addJwtToCookie(token, response);
//        //response.addHeader();

        // ---------- 도메인이 다르면 쿠키 전달 이 안됨
    }
}
