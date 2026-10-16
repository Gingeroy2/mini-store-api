package com.gingeroy.ministoreapi.config;

import com.gingeroy.ministoreapi.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    注册密码编码器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        /*
        把 BCryptPasswordEncoder 注册为一个 Bean。
            为什么要注册？
            1.你之前 UserService 返回的密码是加密的，比对时需要用这个编码器。
            2.你注册用户时（POST /users），也建议用这个 Bean 去加密明文密码，而不是自己 new。
            3.Spring Security 底层会去容器里找 PasswordEncoder，找到了就会用它来比对。
        */
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        /*另一种写法：
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);*/

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        1. 使用无状态会话（stateless），token-based
//        2. 禁用跨站请求伪造（CSRF）——
//          如果 REST API 是无状态（token-based）的，并且认证凭证不是浏览器自动携带的 Cookie，那么通常不需要传统 CSRF 防护
//        3. 授权，定义哪些端点是公共的，哪些是私有的
        http
            .sessionManagement(c -> c
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 1.告知spring我们不需要在服务器端管理任何状态
            .csrf(c -> c.disable()) // 2.禁用跨站请求伪造（CSRF）
            .authorizeHttpRequests(c -> c
//                            .anyRequest().permitAll() // 将所有端点公开
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/carts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                    .requestMatchers(HttpMethod.POST,"/users").permitAll()
                    .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
