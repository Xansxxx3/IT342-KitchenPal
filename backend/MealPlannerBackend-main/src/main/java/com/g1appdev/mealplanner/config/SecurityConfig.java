package com.g1appdev.mealplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import com.g1appdev.mealplanner.authenticator.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final jwtAuth jwtAuthFilter;
        private final OAuth2LoginSuccessHandler oauthLogin;
        private final AuthenticationProvider authenticationProvider;

        public SecurityConfig(jwtAuth jwtAuthFilter,OAuth2LoginSuccessHandler oauthLogin, AuthenticationProvider authenticationProvider) {
                this.jwtAuthFilter = jwtAuthFilter;
                this.oauthLogin = oauthLogin;
                this.authenticationProvider = authenticationProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        UrlBasedCorsConfigurationSource corsConfigurationSource) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))        
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/api/recipe/**").permitAll()
                                                .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
                                                .requestMatchers("/api/dishes/**").permitAll()
                                                .requestMatchers("/api/meal-plans/**").permitAll()
                                                .requestMatchers("/api/v1/user/**, /api/v1/user/print").permitAll()
                                                .anyRequest().permitAll())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .authenticationProvider(authenticationProvider)
                                .oauth2Login(oauth2 -> oauth2
                .successHandler(oauthLogin)     
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
        @Bean
        public OidcUserService oidcUserService() {
            return new OidcUserService();
        }
}
