package com.example.modu.security;

import com.example.modu.service.CorsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j(topic = "SecurityConfig")
@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsService corsService;


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT"));//DELETE 가 없어요.
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Access-Control-Allow-Credentials 값을 true로 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(req ->
                req.requestMatchers("/**").permitAll());

        // CorsConfigurationSource를 사용하여 CORS 설정 구성
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

}
