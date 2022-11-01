package com.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	 @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	                .inMemoryAuthentication()
	                .withUser("o'vt").password(passwordEncoder().encode("1111")).roles("DIRECTOR")
	                .and()
	                .withUser("vchd-6").password(passwordEncoder().encode("789")).roles("SAM")
	                .and()
	                .withUser("vchd-3").password(passwordEncoder().encode("123")).roles("HAV")
	                .and()
	                .withUser("vchd-5").password(passwordEncoder().encode("456")).roles("ANDJ")
	                .and()
	                .withUser("vchd-6m").password(passwordEncoder().encode("789")).roles("SAMMETROLOGIYA")
	                .and()
	                .withUser("vchd-3m").password(passwordEncoder().encode("123")).roles("HAVMETROLOGIYA")
	                .and()
	                .withUser("vchd-5m").password(passwordEncoder().encode("456")).roles("ANDJMETROLOGIYA");

	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	                .csrf().disable()  
	                .authorizeRequests()
	                .antMatchers(HttpMethod.GET,"/vagons").hasAnyRole("SAM", "DIRECTOR","HAV","ANDJ")
	                .antMatchers("/vagons/**").hasAnyRole("SAM", "DIRECTOR","HAV","ANDJ")
	                .antMatchers("/metrologiya/**").hasAnyRole("DIRECTOR", "SAMMETROLOGIYA","HAVMETROLOGIYA","ANDJMETROLOGIYA")
	                .anyRequest()
	                .authenticated()
	                .and()
	                .httpBasic();  
	    }

	    @Bean
	    PasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
}
