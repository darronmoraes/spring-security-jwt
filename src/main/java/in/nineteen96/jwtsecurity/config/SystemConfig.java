package in.nineteen96.jwtsecurity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SystemConfig {

    private static final Logger log = LoggerFactory.getLogger(SystemConfig.class);

    @Bean
    public UserDetailsService userDetailsService() {
        log.info("CREATING USERS FOR AUTH");
        UserDetails dev = User.builder()
                .username("dev")
                .password(passwordEncoder().encode("dev@123"))
                .roles("DEV")
                .build();

        UserDetails test = User.builder()
                .username("test")
                .password(passwordEncoder().encode("test@123"))
                .roles("TEST")
                .build();

        log.info("ADDING USERS TO IN MEMORY DB");
        return new InMemoryUserDetailsManager(dev, test);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
