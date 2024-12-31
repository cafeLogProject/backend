package cafeLogProject.cafeLog.common.config;


import cafeLogProject.cafeLog.common.auth.jwt.JWTFilter;
import cafeLogProject.cafeLog.common.auth.jwt.JWTLoginHandler;
import cafeLogProject.cafeLog.common.auth.jwt.JWTLogoutFilter;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final JWTLoginHandler loginHandler;
    private final JWTUtil jwtUtil;
    private final JWTTokenService tokenService;
    private final String[] whiteList = {
            "/api/auth/login",
            "/api/auth/check"
    };



    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, tokenService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //cors 설정
//        http
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration configuration = new CorsConfiguration();
//
//                        configuration.setAllowedOrigins(Collections.singletonList("*"));
//                        configuration.setAllowedMethods(Collections.singletonList("*"));
//                        configuration.setAllowCredentials(true);
//                        configuration.setMaxAge(3600L);
//
//                        return configuration;
//                    }
//                }));

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .oauth2Login((auth) -> auth
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService))
                        .successHandler(loginHandler));

        http
                .addFilterAfter(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new JWTLogoutFilter(tokenService), LogoutFilter.class);

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/**", "/logout").authenticated()
                        .anyRequest().denyAll());

        http
                .sessionManagement((auth) -> auth
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(whiteList);
    }

}
