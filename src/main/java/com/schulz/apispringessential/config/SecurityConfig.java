package com.schulz.apispringessential.config;

import com.schulz.apispringessential.services.SchulzUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SchulzUserDetailService schulzUserDetailService;

    /**
     *BasicAuthenticationFilter4
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginGeneratingFilter
     * DefaultLogoutGeneratingFilter
     * FilterSecurityInterceptor
     * Authentication -> Authorization
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and() //Usado para habilitar csrf, onde o sistema gera um token para poder realizar as ações, sem token sem ação
                .authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN") //antMatchers valida roles por url
                .antMatchers("/animes/**").hasRole("USER") //sempre deve vir o mais restritivo antes
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override//usuario configurado em memoria
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded{} ", passwordEncoder.encode("marcola"));

        auth.inMemoryAuthentication()
                .withUser("marcos2")
                .password(passwordEncoder.encode("marcola"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("test")
                .password(passwordEncoder.encode("marcola"))
                .roles("USER");

        auth.userDetailsService(schulzUserDetailService)
                .passwordEncoder(passwordEncoder);

        //spring framwork suporta multiplo provider
    }
}
