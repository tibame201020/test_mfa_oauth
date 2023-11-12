package com.example.test_mfa_oauth_auth_client.filter;

import com.example.test_mfa_oauth_auth_client.models.security.TokenInfo;
import com.example.test_mfa_oauth_auth_client.models.security.ValidToken;
import com.example.test_mfa_oauth_auth_client.utils.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.example.test_mfa_oauth_auth_client.constants.SecurityConstants.PASS_URLS;

/**
 * 由spring security filter chain定義在UsernamePasswordAuthenticationFilter之前
 * 檢查有無token, 驗證token, 主動添加authenticationToken至SecurityContextHolder Context
 */
@Component
@RequiredArgsConstructor
public class PreSecurityCheckJsonWebToken extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final Log log = LogFactory.getLog(PreSecurityCheckJsonWebToken.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("attempt api is = " + request.getServletPath());
        if (isUrlCanPass(request)) {
            log.info("api is white list");
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
            log.info("api is not white list & don't have token");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());
        ValidToken validToken = jwtProvider.validToken(token);
        log.info("start to valid token");

        if (validToken.isExpired()) {
            log.info("token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        if (validToken.isDecodeException()) {
            log.info("token is isDecodeException");
            filterChain.doFilter(request, response);
            return;
        }

        if (validToken.isOtherException()) {
            log.info("token is have other error, can't decode");
            filterChain.doFilter(request, response);
            return;
        }

        TokenInfo tokenInfo = validToken.getTokenInfo();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        tokenInfo.getCustomUserDTO(),
                        null,
                        tokenInfo.getRoleList()
                                .stream()
                                .map(SimpleGrantedAuthority::new).toList()
                );

        authenticationToken.setDetails(tokenInfo.getCustomUserDTO());
        log.info("token is valid");
        log.info("user info = " + authenticationToken.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean isUrlCanPass(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return Arrays.asList(PASS_URLS).contains(servletPath);
    }
}
