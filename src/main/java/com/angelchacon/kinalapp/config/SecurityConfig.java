package com.angelchacon.kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // Permitimos registro y recursos estáticos
                        .requestMatchers("/login", "/registro/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Nos manda al index.html que está en la raíz
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        // Consultas personalizadas para tu tabla 'usuarios' y columna 'rol'
        users.setUsersByUsernameQuery("SELECT username, password, 'true' as enabled FROM usuarios WHERE username = ?");
        users.setAuthoritiesByUsernameQuery("SELECT username, rol FROM usuarios WHERE username = ?");

        return users;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Seguimos con texto plano para que no te compliques con BCrypt por ahora
        return NoOpPasswordEncoder.getInstance();
    }
}