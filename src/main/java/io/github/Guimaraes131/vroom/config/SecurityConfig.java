package io.github.Guimaraes131.vroom.config;

import io.github.Guimaraes131.vroom.security.CustomUserDetailsService;
import io.github.Guimaraes131.vroom.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login/**").permitAll();
                    auth.requestMatchers("/register/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/users/**").permitAll();

                    auth.requestMatchers(HttpMethod.DELETE, "/motorcycle/**").hasRole("MANAGER");

                    auth.anyRequest().authenticated();
                })
                .formLogin(configurer -> {
                    configurer
                            .loginPage("/login")
                            .defaultSuccessUrl("/tags", true)
                            .permitAll();
                })
                .logout( logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-success")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }
}
