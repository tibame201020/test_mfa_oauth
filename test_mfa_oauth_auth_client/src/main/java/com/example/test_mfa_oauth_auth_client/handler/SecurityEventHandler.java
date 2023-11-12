package com.example.test_mfa_oauth_auth_client.handler;

import com.example.test_mfa_oauth_auth_client.models.CustomUserDTO;
import com.example.test_mfa_oauth_auth_client.models.security.Auth;
import com.example.test_mfa_oauth_auth_client.utils.JwtProvider;
import com.example.test_mfa_oauth_auth_client.utils.SecurityContextHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 處理spring-security 觸發事件
 * onAuthenticationSuccess: login success
 * onAuthenticationFailure: login fail
 * handle: 經過驗證但無對應權限
 * commence: 沒有經過驗證訪問需驗證資源
 */
@Component
@RequiredArgsConstructor
public class SecurityEventHandler implements
        AuthenticationSuccessHandler,
        AuthenticationFailureHandler,
        AccessDeniedHandler,
        AuthenticationEntryPoint {
    private final JwtProvider jwtProvider;
    private final Log log = LogFactory.getLog(SecurityEventHandler.class);

    private final Gson gson = new Gson();
    private final ObjectMapper objectMapper;

    /**
     * login success handle
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("login success");
        Auth auth = (Auth) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(request.getContextPath(), auth);

        System.out.println(auth);
        System.out.println(auth.getCustomUser());


        String ACCESS_TOKEN = "access_token";
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ACCESS_TOKEN);
        response.addHeader(ACCESS_TOKEN, accessToken);

        CustomUserDTO customUserDTO = gson.fromJson(gson.toJson(auth.getCustomUser()), CustomUserDTO.class);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), customUserDTO);

    }

    /**
     * login fail handle
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("login fail");
        log.error(exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        log.info("get user = " + SecurityContextHandler.getUserInfo());
    }

    /**
     * 處理已有Authentication用戶(已視為登入者)
     * 訪問資源無對應role時觸發
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("auth user access deny");
        log.error(accessDeniedException.getMessage());
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());

        log.info("get user = " + SecurityContextHandler.getUserInfo());
    }

    /**
     * 處理Anonymous用戶(未登入者)
     * 訪問需要授權資源時觸發
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("un auth user deny");
        log.error(authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.info("get user = " + SecurityContextHandler.getUserInfo());
    }
}
