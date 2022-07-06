package nl.oosterhuis.bookapp.config;

import nl.oosterhuis.bookapp.filter.JwtRequestFilter;
import nl.oosterhuis.bookapp.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsServiceImpl customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/v1/authentication").permitAll()

                .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                .antMatchers("/api/v1/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("USER")
                .antMatchers("/api/v1/users/**").hasRole("ADMIN")
                .antMatchers("/api/v1/my-profile").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/my-profile/**").hasAnyRole("USER", "ADMIN")

                .antMatchers(HttpMethod.POST, "/api/v1/books").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/books").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/books/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/my-books").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/my-books/**").hasAnyRole("USER", "ADMIN")

                .antMatchers(HttpMethod.POST, "/api/v1/favorites").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/favorites").hasRole("ADMIN")
                .antMatchers("/api/v1/favorites/**").hasRole("ADMIN")
                .antMatchers("/api/v1/my-favorites").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/my-favorites/**").hasAnyRole("USER", "ADMIN")

                .antMatchers(HttpMethod.POST, "/api/v1/transactions").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/transactions").hasRole("ADMIN")
                .antMatchers("/api/v1/transactions/**").hasRole("ADMIN")
                .antMatchers("/api/v1/my-transactions").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/my-transactions/**").hasAnyRole("USER", "ADMIN")

                .antMatchers("api/v1/uploads/").hasAnyRole("USER", "ADMIN")
                .antMatchers("api/v1/downloads/").hasAnyRole("USER", "ADMIN")

                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}