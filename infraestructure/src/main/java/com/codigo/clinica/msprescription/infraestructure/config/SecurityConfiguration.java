package com.codigo.clinica.msprescription.infraestructure.config;

import com.codigo.clinica.msprescription.infraestructure.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/ms-prescription/medicine/create").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/ms-prescription/prescription/create").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/ms-prescription/prescription/detail/create").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/ms-prescription/prescription/detail/list/create").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/ms-prescription/**").hasAnyAuthority(Role.ADMIN.name(),Role.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
