package com.silalahi.valentinus.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenprovider jwtTokenprovider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		super.configure(http);
		//disable csrf
		http.csrf().disable();
		
		// no session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		//entry points
		http.authorizeRequests()
		.antMatchers("/users/signin").permitAll()
		.antMatchers("/users/signup").permitAll()
		.antMatchers("/h2-console/**/**").permitAll()
		//disallow everything else
		.anyRequest().authenticated();
		
		//if a user try to access a resource without having enough permissions
		http.exceptionHandling().accessDeniedPage("/login");
		
		//Apply JWT
		http.apply(new JwtTokenFilterConfigurer(jwtTokenprovider));
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		web.ignoring().antMatchers("/v2/api-docs")
		.antMatchers("/swagger-resources/**")
		.antMatchers("/swagger-ui.html")
		.antMatchers("configuration/**")
		.antMatchers("/webjars/**")
		.antMatchers("/public")
		.and()
		.ignoring()
		.antMatchers("/h2-console/**/**");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	

}
