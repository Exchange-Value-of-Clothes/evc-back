package com.yzgeneration.evc.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.authentication.implement.TokenProvider;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.exception.ErrorResponse;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String accessToken = parseToken(request.getHeader("Authorization"));
            Long memberId = tokenProvider.getMemberId(accessToken);
            MemberPrincipal memberPrincipal = new MemberPrincipal(memberRepository.getById(memberId));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberPrincipal,
                    memberPrincipal.getMember().getId(),
                    memberPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(ErrorCode.TOKEN_EXPIRED)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        String[] excludePath = {"/api/auth", "/api/members/register", "/docs", "/connection", "/health"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith) ||
                path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js");
    }

    private String parseToken(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

}
