package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  /*
    이 메서드는 모든 기본 컨버터가 초기화되고 커스텀만 사용하고자 할 때 씀...
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new MappingJackson2HttpMessageConverter());
    converters.add(createXmlHttpMessageConverter());
    System.out.println("===== 메시지 컨버터 ======");
    converters.forEach(System.out::println);
  }

  /*
    커스텀 메시지 컨버터 등 추가
      - 기본 메시지 + 추가 @
   */
//  @Override
//  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//    converters.add(createXmlHttpMessageConverter());
//    System.out.println("===== 메시지 컨버터 ======");
//    converters.forEach(System.out::println);
//
//  }

  private HttpMessageConverter<Object> createXmlHttpMessageConverter() {
    MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();

    // Spring object-xml-mapping 사용 (spring-oxm, xstream 라이브러리)
    XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
    xmlConverter.setMarshaller(xStreamMarshaller);
    xmlConverter.setUnmarshaller(xStreamMarshaller);

    return xmlConverter;
  }
}
