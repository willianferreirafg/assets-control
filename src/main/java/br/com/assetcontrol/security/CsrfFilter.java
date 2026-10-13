package br.com.assetcontrol.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter("/equipamentos")
public class CsrfFilter implements Filter {

    private static final String TOKEN_ATTRIBUTE = "_csrf";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        String method = request.getMethod();

        if ("GET".equals(method) || "HEAD".equals(method)) {
            HttpSession session = request.getSession();

            String token = (String) session.getAttribute(
                    TOKEN_ATTRIBUTE
            );

            if (token == null) {
                byte[] bytes = new byte[32];
                RANDOM.nextBytes(bytes);

                token = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);

                session.setAttribute(TOKEN_ATTRIBUTE, token);
            }

            request.setAttribute(TOKEN_ATTRIBUTE, token);
            chain.doFilter(request, response);
            return;
        }

        if (!"POST".equals(method)) {
            response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
            );
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Sessão inválida ou expirada."
            );
            return;
        }

        String expected = (String) session.getAttribute(
                TOKEN_ATTRIBUTE
        );

        String received = request.getParameter(TOKEN_ATTRIBUTE);

        if (expected == null
                || received == null
                || !MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                received.getBytes(StandardCharsets.UTF_8)
        )) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Token CSRF inválido ou ausente."
            );
            return;
        }

        chain.doFilter(request, response);
    }
}
