package com.apbg.webstore.productservice.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers("/profile").fullyAuthenticated()
                .antMatchers("/prices").fullyAuthenticated()
                .antMatchers("/kill").fullyAuthenticated()
                .regexMatchers(HttpMethod.POST, "/products/.*").fullyAuthenticated()
                .regexMatchers(HttpMethod.POST, "/products").fullyAuthenticated()
                .regexMatchers(HttpMethod.DELETE, "/products/.*").fullyAuthenticated()
                .anyRequest().fullyAuthenticated()
                .and().csrf().disable().httpBasic().disable();
    }
}
