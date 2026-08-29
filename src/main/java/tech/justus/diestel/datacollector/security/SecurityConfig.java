package tech.justus.diestel.datacollector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Nur anschauen ist öffentlich
                        .requestMatchers(
                                HttpMethod.GET,
                                "/datasets",
                                "/datasets/**"
                        ).permitAll()

                        // JSON-Daten lesen ebenfalls öffentlich
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/datasets",
                                "/api/datasets/**"
                        ).permitAll()

                        // Alles andere braucht Login
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("/collector-dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/datasets")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager users() {

        UserDetails user = User
                .withUsername("admin")
                .password("{noop}change-me")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}