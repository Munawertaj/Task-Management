package com.bs23.taskmanagement.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class JwtCookieUtil {

    private static final int EXPIRATION = 60 * 60 * 2; // 2 hour

    public static void addJwtCookie(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(EXPIRATION);
        response.addCookie(jwtCookie);
    }
}
