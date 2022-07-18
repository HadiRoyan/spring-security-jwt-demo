package com.hadroy.authapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadroy.authapp.model.response.ResponseError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hadroy.authapp.filter.RequestAuthenticationFilter.SECRET_KEY;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public TokenAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/signup")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Claims claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

                    String username = claimsJws.getSubject();

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    List<String> roles = claimsJws.get("roles", List.class);
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (JwtException exception) {
                    log.error("jwt exception : {}", exception.getMessage());
                    response.setStatus(UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    ResponseError error = new ResponseError(
                            UNAUTHORIZED.value(),
                            UNAUTHORIZED.name(),
                            List.of(exception.getMessage())
                    );
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                } catch (Exception exception) {
                    log.error("error when authorized user : {}", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    ResponseError error = new ResponseError(
                            FORBIDDEN.value(),
                            FORBIDDEN.name(),
                            List.of(exception.getMessage())
                    );
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
