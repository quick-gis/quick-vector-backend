package com.github.quickvectorbackend.config;

import com.github.quickvectorbackend.utils.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Configuration
public class WebDataConvertConfig {



  @Bean
  public GsonHttpMessageConverter gsonHttpMessageConverter() {
    GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
    converter.setGson(GsonFactory.getGson());
    return converter;
  }




}