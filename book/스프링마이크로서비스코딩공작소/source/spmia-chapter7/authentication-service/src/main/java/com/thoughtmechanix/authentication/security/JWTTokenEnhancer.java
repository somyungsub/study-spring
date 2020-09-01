package com.thoughtmechanix.authentication.security;

import com.thoughtmechanix.authentication.model.UserOrganization;
import com.thoughtmechanix.authentication.repository.OrgUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenEnhancer implements TokenEnhancer {  // 토큰 기능 확장 할 때 쓰는 Interface (TokenEnhancer)

//  @Override
//  public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//    return null;
//  }

  @Autowired
  private OrgUserRepository orgUserRepo;

  private String getOrgId(String userName){
    UserOrganization orgUser = orgUserRepo.findByUserName( userName );  // 조직 정보 조회
    return orgUser.getOrganizationId();                                 // 조직 ID 리턴
  }

  /*
    핵심
    - 확장할 내용 코딩
    - 현재는 organizationId : value 삽입하여 응답하기 위해 필드 추가
   */
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo = new HashMap<>();
    String orgId =  getOrgId(authentication.getName());

    additionalInfo.put("organizationId", orgId);  // 커스텀 필드 -> organizationId : orgId(value)

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);  // 토큰 정보에 -> 커스텀 필드 내용 추가
    return accessToken;
  }
}
