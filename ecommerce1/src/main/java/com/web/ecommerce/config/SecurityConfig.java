package com.web.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.web.ecommerce.security.JwtRequestFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                // Allow OpenAPI (Swagger) endpoints
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // Allow authentication and registration
                .antMatchers(HttpMethod.POST, "/authenticate", "/seller/register", "/buyer/register").permitAll()

                // Seller role-based permissions
                .antMatchers(HttpMethod.POST, "/seller/products/{id}", "/seller/products").hasRole("3")
                .antMatchers(HttpMethod.DELETE, "/seller/products/{id}").hasRole("3")
                .antMatchers(HttpMethod.GET, "/seller/productsview").hasRole("3")

                // Buyer role-based permissions
                .antMatchers(HttpMethod.GET, "/buyer/products", "/carts/update", "/userId").hasRole("2")
                .antMatchers(HttpMethod.PUT, "/buyer/products", "/carts/update").hasRole("2")
                .antMatchers(HttpMethod.POST, "/orders/place", "/cart/add").hasRole("2")

                // Admin role-based permissions
                .antMatchers(HttpMethod.GET, "/admin/products").hasRole("1")

                // All other requests require authentication
                .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors(); // Enable CORS

        // Add JWT filter before authentication
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
