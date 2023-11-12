package com.example.test_mfa_oauth_auth_client.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test_mfa_oauth_auth_client.models.CustomUserDTO;
import com.example.test_mfa_oauth_auth_client.models.security.Auth;
import com.example.test_mfa_oauth_auth_client.models.security.TokenInfo;
import com.example.test_mfa_oauth_auth_client.models.security.ValidToken;
import com.example.test_mfa_oauth_auth_client.repo.CustomUserRepo;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static com.example.test_mfa_oauth_auth_client.constants.SecurityConstants.*;

/**
 * generate token
 * valid token
 * decode token
 */
@Component
public class JwtProvider {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final Gson gson;

    private final CustomUserRepo customUserRepo;

    public JwtProvider(CustomUserRepo customUserRepo) {
        this.algorithm = Algorithm.HMAC512(JWT_SECRET.getBytes());
        this.verifier = JWT.require(algorithm).build();
        this.customUserRepo = customUserRepo;
        this.gson = new Gson();
    }

    public String generateAccessToken(String issue, Auth auth) {


        CustomUserDTO customUserDTO =
                gson.fromJson(gson.toJson(auth.getCustomUser()), CustomUserDTO.class);

        return JWT.create()
                .withIssuer(issue)
                .withSubject(auth.getUsername())
                .withExpiresAt(ZonedDateTime.now().plusHours(ACCESS_TOKEN_DEFAULT_EXPIRE_HOURS).toInstant())
                .withClaim(JwtClaim.ROLE.name(), auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withClaim(JwtClaim.USER_INFO.name(), gson.toJson(customUserDTO))
                .sign(algorithm);
    }

    public String generateRefreshToken(String issue, Auth auth, String requestIp, String deviceInfo) {
        return JWT.create()
                .withIssuer(issue)
                .withSubject(auth.getUsername())
                .withExpiresAt(ZonedDateTime.now().plusDays(REFRESH_TOKEN_DEFAULT_EXPIRE_DAYS).toInstant())
                .withClaim(JwtClaim.REQUEST_IP.name(), requestIp)
                .withClaim(JwtClaim.DEVICE_INFO.name(), deviceInfo)
                .sign(algorithm);
    }

    public ValidToken validToken(String jwtToken) {
        ValidToken validToken = new ValidToken();

        try {
            validToken.setTokenInfo(decodeJwtToken(jwtToken));
        } catch (TokenExpiredException tokenExpiredException) {
            validToken.setExpired(true);
            validToken.setErrorMessage(tokenExpiredException.getMessage());
        } catch (JWTDecodeException jwtDecodeException) {
            validToken.setDecodeException(true);
            validToken.setErrorMessage(jwtDecodeException.getMessage());
        } catch (Exception e) {
            validToken.setOtherException(true);
            validToken.setErrorMessage(e.getMessage());
        }
        return validToken;
    }

    private TokenInfo decodeJwtToken(String jwtToken) {
        DecodedJWT decodedJWT = verifier.verify(jwtToken);

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setIssue(decodedJWT.getIssuer());
        tokenInfo.setSubject(decodedJWT.getSubject());
        tokenInfo.setRoleList(decodedJWT.getClaim(JwtClaim.ROLE.name()).asList(String.class));
        tokenInfo.setRequestIp(decodedJWT.getClaim(JwtClaim.REQUEST_IP.name()).asString());
        tokenInfo.setDeviceInfo(decodedJWT.getClaim(JwtClaim.DEVICE_INFO.name()).asString());
        CustomUserDTO customUserDTO = gson.fromJson(decodedJWT.getClaim(JwtClaim.USER_INFO.name()).asString(), CustomUserDTO.class);
        if (!validUser(customUserDTO)) {
            throw new RuntimeException(String.format("the user: $1 is not active", customUserDTO.getAccount()));
        }
        tokenInfo.setCustomUserDTO(gson.fromJson(decodedJWT.getClaim(JwtClaim.USER_INFO.name()).asString(), CustomUserDTO.class));
        tokenInfo.setExpireDate(decodedJWT.getExpiresAt());

        return tokenInfo;
    }

    private boolean validUser(CustomUserDTO customUserDTO) {
        String active = customUserRepo.findById(customUserDTO.getAccount())
                .orElseThrow(() -> new RuntimeException("on valid token can't not find user info"))
                .getActive();

        return "2".equals(active);
    }

    enum JwtClaim {
        ROLE, REQUEST_IP, DEVICE_INFO, USER_INFO
    }
}

