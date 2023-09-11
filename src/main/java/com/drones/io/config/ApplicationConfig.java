package com.drones.io.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/** Application Wide Configurations */
@Configuration
public class ApplicationConfig {

  /**
   * Setup {@link ModelMapper} Use to Map Entities with DTOs and vice versa.
   *
   * @return {@link ModelMapper}
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper
        .getConfiguration()
        .setSkipNullEnabled(true)
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    return mapper;
  }

  /** Setup {@link MessageSource} Generates error messages from {@link messages} */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages/errors");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
