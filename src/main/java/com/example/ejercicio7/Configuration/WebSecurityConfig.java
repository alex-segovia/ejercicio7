package com.example.ejercicio7.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {
    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "select email, password, status from users where email = ?";
        String sql2 = "select u.email, r.name from users u inner join roles r on (u.idrol = r.id) where u.email = ?";
        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.formLogin();

        http.logout().logoutSuccessUrl("/personaje/list");

        http.authorizeHttpRequests()
                .requestMatchers("/personaje/list").hasAnyAuthority("USER","ADMIN","EDITOR")
                .requestMatchers("/personaje/new").hasAnyAuthority("ADMIN","EDITOR")
                .requestMatchers("/personaje/edit").hasAnyAuthority("ADMIN","EDITOR")
                .requestMatchers("/personaje/delete").hasAuthority("ADMIN")
                .anyRequest().permitAll();

        return http.build();
    }
}
