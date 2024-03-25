package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfigNew implements CommunityConstant {
    @Autowired
    private CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/user/setting",
                                "/user/upload",
                                "/discuss/add",
                                "/comment/add/**",
                                "/letter/**",
                                "/notice/**",
                                "/like",
                                "/follow",
                                "/unfollow"
                        ).hasAnyAuthority(AUTHORITY_USER, AUTHORITY_ADMIN, AUTHORITY_MODERATOR)
                        .requestMatchers(
                                "/discuss/top",
                                "/discuss/wonderful"
                        ).hasAnyAuthority(AUTHORITY_MODERATOR)
                        .requestMatchers(
                                "/discuss/delete",
                                "/data/**",
                                "/actuator/**"
                        ).hasAnyAuthority(AUTHORITY_ADMIN)
                        .requestMatchers("/login", "/resources/**", "/index", "/css/**",
                                "/js/**", "/images/**", "/kaptcha", "/discuss/detail/**", "/logout", "/register",
                                "/user/profile/**","/user/header/**").permitAll() // 确保登录页面和首页不需要认证
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, accessDeniedException) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                                response.setContentType("application/plain;charset=utf-8");
                                PrintWriter writer = response.getWriter();
                                writer.write(CommunityUtil.getJSONString(403, "你还没有登录哦!"));
                            } else {
                                response.sendRedirect(request.getContextPath() + "/login");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                                response.setContentType("application/plain;charset=utf-8");
                                PrintWriter writer = response.getWriter();
                                writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限!"));
                            } else {
                                response.sendRedirect(request.getContextPath() + "/denied");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/securitylogout")
                        // 如果注销成功后不需要特定的跳转，此行可选
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
