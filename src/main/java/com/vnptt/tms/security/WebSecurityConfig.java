package com.vnptt.tms.security;

import com.vnptt.tms.security.jwt.AuthEntryPointJwt;
import com.vnptt.tms.security.jwt.AuthTokenFilter;
import com.vnptt.tms.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter {\

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * filter for TMS . user
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider authenticationProviderTMS() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // Get AuthenticationManager bean
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, let Spring Security use user password encryption
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .cors().and()// Prevent request from another domain
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler) //create error page
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // for server http
                .and().authorizeRequests()
                .antMatchers("/TMS/api/auth/signin").permitAll() //Allow everyone to access addresses
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/TMS/logout").logoutSuccessUrl("/TMS/logoutSuccessful");

        http.authenticationProvider(authenticationProviderTMS());

        // Add a Filter class that checks for jwt
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
