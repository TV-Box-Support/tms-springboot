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
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;


    // Trong Spring Framework, @Bean được sử dụng để chỉ định rằng phương thức được chú thích này sẽ tạo ra một bean và
    // đăng ký nó trong container Spring. Điều này cho phép sử dụng bean này trong các phần khác của ứng dụng
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // DaoAuthenticationProvider là một lớp được Spring Security cung cấp và được sử dụng để xác thực người dùng
    @Bean
    public DaoAuthenticationProvider authenticationProviderTMS() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // AuthenticationManager là một interface trong Spring Security được sử dụng để xác thực yêu cầu xác thực từ người dùng
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

    // sử dụng Spring Security để cấu hình bộ lọc bảo mật (security filter chain) trong ứng dụng web
    // SecurityFilterChain là một interface trong Spring Security được sử dụng để cấu hình và xác định các bộ lọc bảo mật
    // cho các yêu cầu HTTP trong ứng dụng web.
    // filterChain nhận hai tham số: http kiểu HttpSecurity và authManager kiểu AuthenticationManager:
    //      - HttpSecurity là một lớp trong Spring Security được sử dụng để cấu hình các quy tắc bảo mật cho các yêu cầu HTTP.
    //      - AuthenticationManager là một interface trong Spring Security được sử dụng để xác thực yêu cầu xác thực từ người dùng.
    //
    // http.cors().and() được sử dụng để kích hoạt hỗ trợ cho CORS (Cross-Origin Resource Sharing) trong ứng dụng,
    // các yêu cầu từ miền khác sẽ được chấp nhận và truy cập vào các tài nguyên của miền hiện tại.
    //
    // .csrf().disable() là để vô hiệu hóa CSRF (Cross-Site Request Forgery) protection trong ứng dụng.
    //
    // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) được sử dụng để cấu hình chính sách
    //  quản lý phiên (session) của ứng dụng là STATELESS, tức là không lưu trữ trạng thái phiên trên máy chủ.
    //
    // .authorizeRequests() bắt đầu cấu hình các quy tắc cho việc xác thực yêu cầu:
    //
    //      - .antMatchers("/TMS/api/auth/signin").permitAll() cho phép tất cả mọi người truy cập vào địa chỉ /TMS/api/auth/signin
    //         mà không cần xác thực.
    //
    //      - .anyRequest().authenticated() yêu cầu xác thực cho tất cả các yêu cầu khác.
    //
    //      - .logout().logoutUrl("/TMS/logout").logoutSuccessUrl("/TMS/logoutSuccessful") cấu hình đường dẫn URL cho chức năng
    //         đăng xuất (logout) và đường dẫn URL sau khi đăng xuất thành công.
    //
    // http.authenticationProvider(authenticationProviderTMS()) cấu hình AuthenticationProvider cho HttpSecurity để sử dụng
    // trong quá trình xác thực.
    // http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class) được sử dụng để
    // thêm một bộ lọc (filter) trước UsernamePasswordAuthenticationFilter. Bộ lọc này được thực hiện để kiểm tra JWT
    // (JSON Web Token) trong yêu cầu xác thực.
    // Cuối cùng, phương thức trả về đối tượng http đã được cấu hình sẵn để sử dụng trong ứng dụng.
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
