package com.example.test_mfa_oauth_auth_client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.test_mfa_oauth_auth_client.constants.CorsConstants.ALLOW_CORS_URLS;
import static com.example.test_mfa_oauth_auth_client.constants.CorsConstants.API_PREFIX;

/**
 * 跨域設定
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(API_PREFIX)
                .allowedOrigins(ALLOW_CORS_URLS)
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name())
                .allowCredentials(true);
    }

}
