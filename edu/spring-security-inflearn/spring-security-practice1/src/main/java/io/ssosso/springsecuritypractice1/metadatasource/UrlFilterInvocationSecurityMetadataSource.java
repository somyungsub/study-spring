package io.ssosso.springsecuritypractice1.metadatasource;

import io.ssosso.springsecuritypractice1.sercurity.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
  DefaultFilterInvocationSecurityMetadataSource 참조
 */
@Slf4j
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  private Map<RequestMatcher, List<ConfigAttribute>> requestMap;
  private SecurityResourceService securityResourceService;

  public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap,
                                                   SecurityResourceService securityResourceService) {
    this.requestMap = requestMap;
    this.securityResourceService = securityResourceService;
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

  public void reload() {
    LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadMap = securityResourceService.getResourceList();
    Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadMap.entrySet().iterator();

    requestMap.clear();

    while (iterator.hasNext()) {
      Map.Entry<RequestMatcher, List<ConfigAttribute>> next = iterator.next();
      requestMap.put(next.getKey(), next.getValue());
    }
  }
}
