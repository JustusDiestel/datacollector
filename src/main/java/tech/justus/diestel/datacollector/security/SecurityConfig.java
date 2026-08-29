package tech.justus.diestel.datacollector.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;


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
    InMemoryUserDetailsManager users(
            @Value("${APP_ADMIN_USER:admin}") String username,
            @Value("${APP_ADMIN_PASSWORD:change-me}") String password
    ) {

        UserDetails user = User
                .withUsername(username)
                .password("{noop}" + password)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}