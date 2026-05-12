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
                        // 1. Recursos públicos y de acceso libre
                        .requestMatchers("/login", "/registro/**", "/css/**", "/js/**").permitAll()

                        // 2. Permisos para el rol USER (Listar, Buscar, Agregar)
                        // El usuario común puede ver y crear, pero no alterar lo existente
                        .requestMatchers("/productos/listar", "/productos/buscar", "/productos/agregar").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/clientes/listar", "/clientes/buscar", "/clientes/agregar").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ventas/listar", "/ventas/buscar", "/ventas/agregar").hasAnyRole("USER", "ADMIN")

                        // 3. Permisos EXCLUSIVOS para ADMIN (Editar y Eliminar)
                        // Rutas específicas para evitar conflictos de mapeo en el ApplicationContext
                        .requestMatchers("/productos/eliminar/**", "/productos/editar/**").hasRole("ADMIN")
                        .requestMatchers("/clientes/eliminar/**", "/clientes/editar/**").hasRole("ADMIN")
                        .requestMatchers("/ventas/eliminar/**", "/ventas/editar/**").hasRole("ADMIN")

                        // 4. Cualquier otra ruta requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
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