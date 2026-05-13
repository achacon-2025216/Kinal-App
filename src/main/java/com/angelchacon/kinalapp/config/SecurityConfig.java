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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        // 1. Público
                        .requestMatchers("/login", "/registro/**", "/css/**", "/js/**").permitAll()

                        // 2. Acceso para USER y ADMIN (Listar, Buscar, Agregar)
                        // Agregué /usuarios/listar para que el User también pueda ver esa ventana
                        .requestMatchers(
                                "/",
                                "/productos/listar", "/productos/buscar", "/productos/agregar", "/productos/guardar",
                                "/clientes/listar", "/clientes/buscar", "/clientes/agregar", "/clientes/guardar",
                                "/ventas/listar", "/ventas/buscar", "/ventas/agregar", "/ventas/guardar",
                                "/usuarios/listar", "/usuarios/buscar"
                        ).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // 3. Exclusivo de ADMIN (Editar, Eliminar y Gestión total de usuarios)
                        .requestMatchers(
                                "/productos/eliminar/**", "/productos/editar/**",
                                "/clientes/eliminar/**", "/clientes/editar/**",
                                "/ventas/eliminar/**", "/ventas/editar/**",
                                "/usuarios/eliminar/**", "/usuarios/editar/**", "/usuarios/agregar"
                        ).hasAuthority("ROLE_ADMIN")

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

        // Consulta de autenticación
        users.setUsersByUsernameQuery("SELECT username, password, 'true' as enabled FROM usuarios WHERE username = ?");

        // Consulta de Autoridades: Extrae el rol (ROLE_USER/ROLE_ADMIN) directamente de tu tabla
        users.setAuthoritiesByUsernameQuery("SELECT username, rol FROM usuarios WHERE username = ?");

        return users;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Mantenemos texto plano para que coincida con tus credenciales actuales
        return NoOpPasswordEncoder.getInstance();
    }
}