package com.ufmg.testedesoftware.animelistofmine.configurer;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AnimeListOfMineWebMvcConfigurer implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
    pageHandler.setFallbackPageable(PageRequest.of(0, 5));
    resolvers.add(pageHandler);
  }
}
