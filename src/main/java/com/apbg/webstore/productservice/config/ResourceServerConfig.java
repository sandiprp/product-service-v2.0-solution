package com.apbg.webstore.productservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.context.request.RequestContextListener;

@Slf4j
@Configuration
@EnableOAuth2Client
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private TokenStore tokenStore = new InMemoryTokenStore();

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()
                .regexMatchers(HttpMethod.PUT, "/products/.*").authenticated()
                .regexMatchers(HttpMethod.POST, "/products").authenticated()
                .regexMatchers(HttpMethod.DELETE, "/products/.*").authenticated()
                .regexMatchers(HttpMethod.PUT, "/carts/.*/empty").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable().httpBasic().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("webstore").tokenStore(tokenStore);
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
