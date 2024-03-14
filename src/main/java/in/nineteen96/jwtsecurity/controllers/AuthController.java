package in.nineteen96.jwtsecurity.controllers;

import in.nineteen96.jwtsecurity.models.JWTRequest;
import in.nineteen96.jwtsecurity.models.JWTResponse;
import in.nineteen96.jwtsecurity.security.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        log.info("request : {}", request);

        this.authenticate(email, password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String token = this.jwtHelper.generateToken(userDetails);

        JWTResponse response = JWTResponse.builder()
                .token(token)
                .username(userDetails.getUsername()).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String email, String password) {
        log.info("AUTHENTICATING");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        try {
            authManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            log.error("Invalid username or password");
            //throw new RuntimeException("Invalid Username or Password");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
