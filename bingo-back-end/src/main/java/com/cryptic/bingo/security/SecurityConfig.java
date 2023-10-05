package com.cryptic.bingo.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private AuthUserService authUserService;
	private PasswordEncoder encoder;
	private ApplicationContext context;

	@Autowired
	public SecurityConfig(AuthUserService authUserService, PasswordEncoder encoder, ApplicationContext context) {
		super();
		this.authUserService = authUserService;
		this.encoder = encoder;
		this.context = context;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// Configure AuthenticationManagerBuilder
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(authUserService).passwordEncoder(encoder);
		// Get AuthenticationManager
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		http
				// TODO allow for people to get game boards
				.cors().and().csrf().disable().authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
				.requestMatchers(HttpMethod.PUT, SecurityConstants.SIGN_UP_URL).permitAll()
				.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL_REGISTERED).permitAll()
				.requestMatchers("ws").permitAll()
				.requestMatchers("ws/**").permitAll()
				.requestMatchers(HttpMethod.GET, "game/exist/{gamecode}").permitAll()
				.requestMatchers(HttpMethod.GET, "game/loadBoard/{gamecode}").permitAll()
				.anyRequest()
				.authenticated().and().addFilter(getAuthenticationFilter(authenticationManager))
				.addFilter(new AuthorizationFilter(authenticationManager))
				.authenticationManager(authenticationManager)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
		return http.build();
	}

	public AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager);
		filter.setFilterProcessesUrl(SecurityConstants.SIGN_IN_URL);
		return filter;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList(SecurityConstants.ALLOWED_ORIGINS)); // this is the React/frontend
																							// port, not
		// the java port
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("*"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;

	}
}
