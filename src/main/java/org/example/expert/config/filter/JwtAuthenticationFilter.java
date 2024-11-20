package org.example.expert.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/signin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            SigninRequest requestDto = new ObjectMapper().readValue(req.getInputStream(), SigninRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getId();
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole userRole = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserRole();
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getNickname();

        String token = jwtUtil.createToken(userId, username, userRole, nickname);
//        res.addHeader("Authorization", token);
        jwtUtil.addJwtToCookie(token, res);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            AuthenticationException authException) throws IOException, ServletException {
        log.info("로그인 실패");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
