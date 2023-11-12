package com.example.test_mfa_oauth_auth_client.constants;

/**
 * 跨域常數
 */
public class CorsConstants {
    public static final String API_PREFIX = "/api/**";
    public static final String LOCAL_5173 = "http://localhost:5173/";
    public static final String _5173 = "http://127.0.0.1:5173/";

    public static final String[] ALLOW_CORS_URLS = new String[]{LOCAL_5173, _5173};
}
