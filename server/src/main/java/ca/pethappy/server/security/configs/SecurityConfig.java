package ca.pethappy.server.security.configs;

import ca.pethappy.server.security.filters.TokenAuthenticationFilter;
import ca.pethappy.server.security.services.JpaDetailsService;
import ca.pethappy.server.security.services.TokenAuthenticationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JpaDetailsService service;
    private final PasswordEncoder passwordEncoder;
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public SecurityConfig(JpaDetailsService service, PasswordEncoder passwordEncoder,
                          CorsConfigurationSource corsConfigurationSource) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Order(1)
    @Configuration
    public class BasicAuthConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/login")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .cors().configurationSource(corsConfigurationSource)
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(service).passwordEncoder(passwordEncoder);
        }
    }

    @Order(2)
    @Configuration
    public class TokenAuthConfig extends WebSecurityConfigurerAdapter {
        private final TokenAuthenticationUserDetailsService service;

        @Autowired
        public TokenAuthConfig(TokenAuthenticationUserDetailsService service) {
            this.service = service;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
//                    .antMatcher("/api/user").authorizeRequests()
                    .antMatcher("/**").authorizeRequests()
                    .mvcMatchers(HttpMethod.POST, "/api/user").anonymous()
                    .mvcMatchers(HttpMethod.POST, "/api/register").hasRole("GUEST")
                    .anyRequest().authenticated()
                    .and()
                    .cors().configurationSource(corsConfigurationSource).and()
                    .addFilterBefore(authFilter(), RequestHeaderAuthenticationFilter.class)
                    .authenticationProvider(preAuthProvider())
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf();
        }

        @Bean
        public TokenAuthenticationFilter authFilter() {
            return new TokenAuthenticationFilter();
        }

        @Bean
        public AuthenticationProvider preAuthProvider() {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(service);
            return provider;
        }
    }
}
