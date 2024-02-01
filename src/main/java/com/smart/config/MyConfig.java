package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/**").permitAll()

        )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/dologin")
                        // .usernameParameter("username")
                        // .passwordParameter("password")
                        .defaultSuccessUrl("/user/index", true)

                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("AbcdEfghIjkl..."))

                .logout(logout -> logout
                        .logoutUrl("/logout") // The default logout URL is /logout
                        .logoutSuccessUrl("/login") // Redirect to login page after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                // .logoutUrl("/signin")
                // .logoutSuccessUrl("/signin")
                // .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
