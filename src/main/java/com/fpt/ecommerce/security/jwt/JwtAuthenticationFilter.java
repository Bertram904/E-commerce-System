package com.fpt.ecommerce.security.jwt;

import com.fpt.ecommerce.repository.InvalidatedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    JwtUtils jwtUtils;
    UserDetailsService userDetailsService;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = authorizationHeader.substring(7);

            if (jwt != null && jwtUtils.isTokenValid(jwt)) {
                //Check BlackList
                String jti = jwtUtils.extractJwtId(jwt);
                if (invalidatedTokenRepository.existsById(jti)) {
                    log.warn("Request rejected : Token {} is in blacklist", jti);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ");
                    return;
                }

                String username = jwtUtils.extractUsername(jwt);
                String scope = jwtUtils.extractScope(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                log.warn("Invalid JWT Token");
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}" + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

//    private String parseJwt(HttpServletRequest request) {
//        String accessToken =  request.getHeader("Authorization");
//        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
//            return accessToken.substring(7);
//        }
//        return null;
//    }
}