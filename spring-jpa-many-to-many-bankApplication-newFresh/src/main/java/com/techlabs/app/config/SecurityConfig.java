package com.techlabs.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techlabs.app.security.JwtAuthenticationEntryPoint;
import com.techlabs.app.security.JwtAuthenticationFilter;

@Configuration
//@EnableMethodSecurity
public class SecurityConfig {

	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	private JwtAuthenticationFilter authenticationFilter;

	public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
			JwtAuthenticationFilter authenticationFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
	}

	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf((csrf) -> csrf.disable())
              .authorizeHttpRequests((authorize) ->
                      authorize
//                      
                      //.requestMatchers(HttpMethod.POST,"/api/accounts/**").hasRole("ADMIN")
                      //.requestMatchers(HttpMethod.GET,"/api/accounts/customer/{customerId}").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.POST,"/api/accounts/{customerId}").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.GET,"/api/accounts").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.GET,"/api/accounts/{id}").hasRole("ADMIN")
//                      
//                      .requestMatchers(HttpMethod.POST,"/api/customers/**").hasRole("USER")
//                      .requestMatchers(HttpMethod.POST,"/api/customers/{accountId}/deposit").hasRole("USER")
//                      .requestMatchers(HttpMethod.POST,"/api/customers/{accountId}/withdraw").hasRole("USER")
//                      .requestMatchers(HttpMethod.POST,"/api/customers/{fromAccountId}/transfer/{toAccountId}").hasRole("USER")
//                      //.requestMatchers(HttpMethod.GET,"/api/customers").hasRole("ADMIN")
//                      //.requestMatchers(HttpMethod.GET,"/api/customers/{id}").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.GET,"/api/customers/{accountId}/balance").hasRole("USER")
//                      .requestMatchers(HttpMethod.DELETE,"/api/customers/{id}").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.GET,"/api/transactions/account/{accountId}").hasRole("ADMIN")
//                     // .requestMatchers(HttpMethod.GET,"/api/accounts/customer/{customerId}").hasRole("ADMIN")
////                      .requestMatchers(HttpMethod.PUT,"/api/customers/{id}").hasRole("USER")
//                     // .requestMatchers(HttpMethod.POST,"/api/accounts/{customerId}").hasRole("ADMIN")
//                     // .requestMatchers(HttpMethod.GET,"/api/accounts").hasRole("ADMIN")
//                      .requestMatchers(HttpMethod.GET,"/api/customers/{customerId}/totalBalance").hasRole("USER")
//                      .requestMatchers(HttpMethod.GET,"/api/transactions").hasRole("ADMIN")
//                      .requestMatchers("/api/auth/**").permitAll()
                      .anyRequest().permitAll()
                      //.anyRequest().authenticated()

              ).exceptionHandling( exception -> exception
                      .authenticationEntryPoint(authenticationEntryPoint)
              ).sessionManagement( session -> session
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              );

      http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
  }

}