package train.springsecuritytest.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component //インスタンスをDIコンテナに追加
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        final String traceId = UUID.randomUUID().toString();
        // ログへの記録
        log.warn("[401] path={} reason={} traceId={}",
                request.getRequestURI(), authException.getClass().getSimpleName(), traceId);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
                {
                  "timestamp":"%s",
                  "status":401,
                  "error":"Unauthorized",
                  "code":"AUTHENTICATION_REQUIRED",
                  "message":"認証エラー：ログインが必要です",
                  "path":"%s",
                  "traceId":"%s"
                }
                """.formatted(Instant.now().toString(), request.getRequestURI(), traceId));
    }
}
