package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebConfig {

  @Bean
  public InternalResourceViewResolver internalResourceViewResolver() {
    final InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setPrefix("/WEB-INF/views/");
    internalResourceViewResolver.setSuffix(".jsp");
    return internalResourceViewResolver;
  }

//  @Bean
//  public ObjectMapper objectMapper() {
//    return new ObjectMapper();
//  }
//
//  @Bean
//  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//    converter.setObjectMapper(objectMapper());
//    return converter;
//  }

//  @Bean
//  public MockMvc mockMvc() {
//    return new MockMvc(new TestDispatcherServlet(new XmlWebApplicationContext()));
//  }
}
