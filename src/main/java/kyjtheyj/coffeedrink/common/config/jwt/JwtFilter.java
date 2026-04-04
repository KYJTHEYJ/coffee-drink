package kyjtheyj.coffeedrink.common.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyjtheyj.coffeedrink.common.config.security.UserDetailServiceImpl;
import kyjtheyj.coffeedrink.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final ObjectMapper objectMapper;

    private static final PathPatternParser patternParser = new PathPatternParser();

    // 통과 API 패턴 기록 (Path Pattern 사용)
    private static final List<PathPattern> EXCLUDE_PATTERNS = List.of(
            patternParser.parse("/v1/api/**")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthFilter IN");
        String Authorization = request.getHeader("Authorization");
        String token;

        // Header 의 Authorization 확인
        if (Authorization != null && Authorization.startsWith("Bearer ")) {
            token = Authorization.substring("Bearer ".length());
        } else {
            BaseResponse<Void> baseResponse = BaseResponse.fail(HttpStatus.UNAUTHORIZED.name(), "인증 정보가 없습니다");

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
            return;
        }

        if(jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractSubject(token);
            UserDetails user = userDetailServiceImpl.loadUserByUsername(email);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user
                            , null
                            , user.getAuthorities())
            );
        }

        filterChain.doFilter(request, response);

        log.info("JwtAuthFilter OUT");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        PathContainer path = PathContainer.parsePath(request.getRequestURI());
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matches(path));
    }
}
