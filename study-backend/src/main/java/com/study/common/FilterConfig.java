package com.study.common;

import com.study.auth.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  private final JwtFilter jwtFilter;

  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
    FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(jwtFilter);
    bean.addUrlPatterns("/api/*");
    bean.setOrder(2);
    return bean;
  }
}
