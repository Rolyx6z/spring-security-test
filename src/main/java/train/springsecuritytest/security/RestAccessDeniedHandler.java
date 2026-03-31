package train.springsecuritytest.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
// AccessDeniedHandlerを実装することで権限拒否(403)が発生したときに呼ばれるクラスであることを示す
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(RestAccessDeniedHandler.class);

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException ex) throws IOException {
        // 調査用のキーを生成する ログと対応している
        final String traceId = UUID.randomUUID().toString();
        // ログへの記録
        log.warn("[403] path={} reason={} traceId={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), traceId);

        // ユーザーへのレスポンス作成
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
                    {
                      "timestamp":"%s",
                      "status":403,
                      "error":"Forbidden",
                      "code":"ACCESS_DENIED",
                      "message":"認可エラー：必要なロール・権限がありません",
                      "path":"%s",
                      "traceId":"%s"
                    }
                    """.formatted(Instant.now().toString(), request.getRequestURI(), traceId));
    }
}
