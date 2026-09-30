package com.example.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity // it works on react or els @EnabledWebSecurity.
public class SecurityConfig {

	/*@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
		
		serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll().pathMatchers("/eazybank/accounts/**")
						
						.hasRole("ACCOUNTS").pathMatchers("/eazybank/cards/**").hasRole("CARDS").pathMatchers("/eazybank/loans/**").hasRole("LOANS"))
				.oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
						.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
		serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
		return serverHttpSecurity.build();
	}*/

	// anyExchange = all types of request
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
		
		//serverHttpSecurity.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
		/*serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll() //This will have first priority than below ones, even though authenticated,preference for permitAll
																   .pathMatchers("/jbmicroservice/accounts-1/**").authenticated()
																   .pathMatchers("/jbmicroservice/cards-1/**").authenticated()
																   .pathMatchers("/jbmicroservice/loans-1/**").authenticated())
														  .oauth2ResourceServer(oauth2ResourceServerSpec -> oauth2ResourceServerSpec.jwt(Customizer.withDefaults()));
		
		serverHttpSecurity.csrf(csrfSpec ->  csrfSpec.disable());   //Since no browsers involved, disable this protection. Otherwise all your post(), put(), delete() will fail because not handled the csrf attack.
		return serverHttpSecurity.build();*/

		//With ROLE
		serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll() //This will have first priority than below ones, even though authenticated,preference for permitAll
																   .pathMatchers("/jbmicroservice/accounts-1/**").hasRole("ACCOUNTS")
																   .pathMatchers("/jbmicroservice/cards-1/**").hasRole("CARDS")
																   .pathMatchers("/jbmicroservice/loans-1/**").hasRole("LOANS"))
				          .oauth2ResourceServer(oauth2ResourceServerSpec -> oauth2ResourceServerSpec.jwt( jwtSpec -> jwtSpec.jwtAuthenticationConverter(  grantedAuthoritiesExtractor()  )));

		serverHttpSecurity.csrf(csrfSpec ->  csrfSpec.disable());   //Since no browsers involved, disable this protection. Otherwise all your post(), put(), delete() will fail because not handled the csrf attack.

		return serverHttpSecurity.build();
	}

	private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}
	
	
	/*@Bean
	  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
	      http
	          // ...
	          .authorizeExchange((exchanges) ->
	              exchanges
	                  // any URL that starts with /admin/ requires the role "ROLE_ADMIN"
	                  .pathMatchers("/admin/**").hasRole("ADMIN")
	                  // a POST to /users requires the role "USER_POST"
	                  .pathMatchers(HttpMethod.POST, "/users").hasAuthority("USER_POST")
	                  // a request to /users/{username} requires the current authentication's username
	                  // to be equal to the {username}
	                  .pathMatchers("/users/{username}").access((authentication, context) ->
	                      authentication
	                          .map(Authentication::getName)
	                          .map((username) -> username.equals(context.getVariables().get("username")))
	                          .map(AuthorizationDecision::new)
	                  )
	                  // allows providing a custom matching strategy that requires the role "ROLE_CUSTOM"
	                  .matchers(customMatcher).hasRole("CUSTOM")
	                  // any other request requires the user to be authenticated
	                  .anyExchange().authenticated()
	          );
	      return http.build();
	  }*/
	 
	


}
