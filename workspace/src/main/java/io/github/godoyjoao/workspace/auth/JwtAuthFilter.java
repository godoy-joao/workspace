package io.github.godoyjoao.workspace.auth;

import io.github.godoyjoao.workspace.identity.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        try {
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.validateToken(token)) {
                    UUID idUser = jwtService.extractClaim(token, c -> c.get("idUser", UUID.class));
                    List<String> roles = jwtService.extractClaim(token, c -> c.get("roles", List.class));
                    String username = jwtService.extractClaim(token, c -> c.get("username", String.class));

                    CustomUserDetails userDetails = new CustomUserDetails(idUser, username, roles);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "AUTHENTICATION_EXPIRED", "Tempo limite de sessão atingido.");
            return;
        } catch (JwtException e) {
            sendErrorResponse(response, "AUTHENTICATION_INVALID", "Token de autenticação inválido.");
            return;
        } catch (Exception ex) {
            log.error("Error processing JWT authentication: {}", ex.getMessage());
            sendErrorResponse(response, "AUTHENTICATION_ERROR", "Erro ao processar autenticação.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
