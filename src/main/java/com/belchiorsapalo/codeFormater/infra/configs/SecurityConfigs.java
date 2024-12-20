package com.belchiorsapalo.codeFormater.infra.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigs {

    private SecurityFilter securityFilter;

    @Autowired
    public SecurityConfigs(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/competitor/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/competitor/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/competitor/info").authenticated()
                                .requestMatchers(HttpMethod.GET, "/competitor/results").authenticated()
                                .requestMatchers(HttpMethod.POST, "/code/process").authenticated()
                                .requestMatchers(HttpMethod.POST, "/code/test").authenticated()
                                .requestMatchers(HttpMethod.POST, "/problems/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/problems").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/problems/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/problems/first").authenticated()
                                .requestMatchers(HttpMethod.GET, "/problems/{id}/time-left").authenticated()
                                .requestMatchers(HttpMethod.GET, "/problems/next").authenticated()
                                .requestMatchers(HttpMethod.GET, "/problems/verify/{sequence}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/problems/delete/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/problems/delete/testCases/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/problems/update/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/problems/updateProblem/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/problems/finish").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/problems/finished").authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
