package com.example.modu.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Deprecated
public class SimpleCorsConfigurationSource implements CorsConfigurationSource {

    private final CorsConfiguration corsConfiguration;

    public SimpleCorsConfigurationSource(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return corsConfiguration;
    }
}
