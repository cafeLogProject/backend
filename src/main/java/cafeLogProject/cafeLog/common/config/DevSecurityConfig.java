package cafeLogProject.cafeLog.common.config;


import cafeLogProject.cafeLog.common.auth.jwt.JWTFilter;
import cafeLogProject.cafeLog.common.auth.jwt.JWTLoginHandler;
import cafeLogProject.cafeLog.common.auth.jwt.JWTLogoutHandler;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2UserService;
import cafeLogProject.cafeLog.common.auth.common.CustomOAuth2AuthorizationRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("dev")
public class DevSecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final JWTLoginHandler loginHandler;
    private final JWTUtil jwtUtil;
    private final JWTTokenService tokenService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final JWTLogoutHandler logoutHandler;

    public static final String[] whiteList = {
            "/api/auth/login",
            "/api/auth/check",
            "/login/oauth2/code/**",
            "/login/oauth2/authorization/**",
            "/oauth2/authorization/**",
            "/mock/oauth2/**",
            "/login"
    };

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, tokenService, whiteList);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository,
                        "/oauth2/authorization"
                );
        CustomOAuth2AuthorizationRequestResolver customResolver =
                new CustomOAuth2AuthorizationRequestResolver(defaultResolver);


        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .cors(AbstractHttpConfigurer::disable);

        http
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestResolver(customResolver)
                        )
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(loginHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())));;

        http
                .addFilterAfter(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

//
//        http
//                .exceptionHandling(entrypoint -> entrypoint
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.setContentType("application/json; charset=UTF-8");
//
//                            String json = String.format("{\"status\": %d, \"message\": \"%s\"}",
//                                    HttpServletResponse.SC_UNAUTHORIZED,
//                                    "접근 권한이 없습니다.");
//
//                            response.getWriter().write(json);
//                        }));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(whiteList).permitAll()
                        .anyRequest().authenticated());

        http
                .sessionManagement((auth) -> auth
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
