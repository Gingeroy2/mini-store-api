package com.gingeroy.ministoreapi.filters;

import com.gingeroy.ministoreapi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // 看观众手上有票吗？
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);
        // 验票
        if (jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 到这里意味着 token 是有效的
        // 告诉 Spring，这个用户已通过身份验证，并允许其访问受保护的资源
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(), // 1. Principal（主体）：你是谁？
                null, // 2. Credentials（凭证）：密码是多少？
                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole())) // 3. Authorities（权限）：你能干嘛？
        ); // 通行证
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // SecurityContextHolder 是 Spring Security 的“全局通行证登记处”
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
