package cr.ac.una.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Define request matchers for Swagger endpoints
        AntPathRequestMatcher[] swaggerMatchers = new AntPathRequestMatcher[]{
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/api/clima/**"),
                new AntPathRequestMatcher("/api/dependencia/**"),
                new AntPathRequestMatcher("/api/plan/**"),
                new AntPathRequestMatcher("/api/tarea/**")
        };

        // Configure HttpSecurity to permit access to Swagger endpoints and disable CSRF
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(swaggerMatchers).permitAll()  // Allow access to Swagger
                        .anyRequest().authenticated()  // Protect other endpoints
                )
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .httpBasic(withDefaults());  // Enable basic authentication for testing

        return http.build();
    }
}
