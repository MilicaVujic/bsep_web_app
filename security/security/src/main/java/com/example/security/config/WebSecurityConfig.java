package com.example.security.config;

import com.example.security.auth.RestAuthenticationEntryPoint;
import com.example.security.auth.TokenAuthenticationFilter;
import com.example.security.service.CustomUserDetailsService;
import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Filter tokenFilter = new TokenAuthenticationFilter(tokenUtils, customUserDetailsService);
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .addFilterBefore(tokenFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/notAuthenticated/visiting/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/resetPassword").permitAll()
                .antMatchers("/api/auth/setNewPassword").permitAll()
                .antMatchers("/api/user/block/**").authenticated()
                .antMatchers("/api/user/unblock/**").authenticated()
                .antMatchers("/api/auth/verify").permitAll()
                .antMatchers("/api/auth/passwordlesslogin").permitAll()
                .antMatchers("/api/vpn").permitAll()
                .antMatchers("/api/auth/passwordlessAuth").permitAll()
                .antMatchers("/api/registerRequest").permitAll()
                .antMatchers("/api/auth/regeneratingJwtToken").permitAll()
                .antMatchers("/api/user/activate").permitAll()
                .antMatchers("/api/user/changePassword").permitAll()
                .antMatchers("/api/advertisement/**").authenticated()
                .antMatchers("/api/advertisementRequest/**").authenticated()
                .antMatchers("/api/permission/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/user/employeeProfile/**").authenticated()
                .antMatchers("/api/user/update/**").authenticated()
                .antMatchers("/api/user/adminProfile/**").authenticated()
                .antMatchers("/api/user/clientProfile/**").authenticated()
                .antMatchers("/api/user/creatingUser/**").authenticated()
                .antMatchers("/api/user/employees").authenticated()
                .antMatchers("/api/user/clients").authenticated()
                .antMatchers("/api/user/getAll/**").authenticated()
                .antMatchers("/api/user/all/**").authenticated()
                .antMatchers("/api/registerRequests").authenticated()
                .antMatchers("/api/registrationRequest/**").authenticated()
                .antMatchers("/api/advertisement/visiting/**").permitAll()
                .antMatchers("/api/user/registerAdmin/**").permitAll()
                .antMatchers("/ws").permitAll()
                .antMatchers("/api/user/criticalEvents").authenticated()
                .antMatchers("/ws/**").permitAll()
                .anyRequest().authenticated().and()
                .cors().and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "api/notAuthenticated/visiting/**");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/login");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/setNewPassword");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/resetPassword");
        web.ignoring().antMatchers(HttpMethod.POST, "api/vpn");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/verify");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/passwordlesslogin");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/passwordlessAuth");
        web.ignoring().antMatchers(HttpMethod.POST, "api/auth/regeneratingJwtToken");
        web.ignoring().antMatchers(HttpMethod.GET, "api/user/activate");
        web.ignoring().antMatchers(HttpMethod.PUT, "api/user/changePassword");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js", "/ws/**");
    }
}