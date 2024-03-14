package in.nineteen96.jwtsecurity.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        log.info("Header : {}", requestHeader);

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);

            try {

                username = this.jwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                log.error("Illegal Argument while Fetching the username!");
            } catch (ExpiredJwtException e) {
                log.error("JWT token has expired!");
            } catch (MalformedJwtException e) {
                log.error("Invalid token!");
            } catch (Exception e) {
                log.error("Something went wrong!");
            }

        } else {
            log.warn("Invalid header value!!");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            log.info("Fetching user detail from username");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);

            log.info("Validating token against user");
            if (validateToken) {
                log.info("Authenticating");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } else {
            log.warn("Validation failed!");
        }

        filterChain.doFilter(request, response);
    }
}
