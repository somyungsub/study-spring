package io.ssosso.springsecuritypractice1.metadatasource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
  DefaultFilterInvocationSecurityMetadataSource 참조
 */
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  private Map<RequestMatcher, List<ConfigAttribute>> requestMap;

  public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap) {
    this.requestMap = requestMap;
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

    // 요청 정보
    HttpServletRequest request = ((FilterInvocation) object).getRequest();

//    requestMap.put(new AntPathRequestMatcher("/mypage"), Arrays.asList(new SecurityConfig("ROLE_USER")));

    // 권한정보 추출
    if (requestMap != null) {
      Set<Map.Entry<RequestMatcher, List<ConfigAttribute>>> entries = requestMap.entrySet();
      Map.Entry<RequestMatcher, List<ConfigAttribute>> requestMatcherListEntry = entries.stream()
        .filter(entry -> entry.getKey().matches(request))
        .findFirst().orElse(null);
      if (requestMatcherListEntry != null) {
        return requestMatcherListEntry.getValue();
      }
    }

    return null;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    Set<ConfigAttribute> allAttributes = new HashSet<>();
    requestMap.entrySet()
      .forEach(entry -> allAttributes.addAll(entry.getValue()));
    return allAttributes;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }
}
