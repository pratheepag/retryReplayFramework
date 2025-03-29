package com.example.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((auth) -> {
			auth.requestMatchers("/dashboard").hasAnyRole("ADMIN", "USER"); // Both ADMIN and USER can access dashboard
			auth.anyRequest().authenticated();
		});

		http.formLogin((form) -> form.successHandler(authenticationSuccessHandler()));
		http.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(Environment environment) {
		String adminRoles = environment.getProperty("spring.security.user.admin.roles", "ADMIN");
		String userRoles = environment.getProperty("spring.security.user.user.roles", "USER");

		return new InMemoryUserDetailsManager(
				User.withUsername(environment.getProperty("spring.security.user.admin.username"))
						.password(passwordEncoder()
								.encode(environment.getProperty("spring.security.user.admin.password")))
						.roles(adminRoles.split(",")).build(),
				User.withUsername(environment.getProperty("spring.security.user.user.username"))
						.password(
								passwordEncoder().encode(environment.getProperty("spring.security.user.user.password")))
						.roles(userRoles.split(",")).build());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {
			response.sendRedirect("/dashboard"); // Redirect both ADMIN and USER to the dashboard
		};
	}
}