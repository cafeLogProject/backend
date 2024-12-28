package cafeLogProject.cafeLog.config;


import cafeLogProject.cafeLog.auth.jwt.*;
import cafeLogProject.cafeLog.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.auth.oauth2.CustomOAuth2UserService;
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
