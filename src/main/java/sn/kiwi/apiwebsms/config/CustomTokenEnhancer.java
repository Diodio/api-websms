package sn.kiwi.apiwebsms.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import sn.kiwi.apiwebsms.models.UserInfo;
import sn.kiwi.apiwebsms.service.UserDetailsServiceImpl;
import sn.kiwi.apiwebsms.service.UserInfoService;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        UserInfo userInfo = userDetailsService.loadUser(user.getUsername());
        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
        additionalInfo.put("partner_id", userInfo.getPartner_id());
        additionalInfo.put("user_id", userInfo.getUser_id());
        additionalInfo.put("customer_id", userInfo.getCustomer_id());
        additionalInfo.put("partner_code", userInfo.getPartner_code());
        additionalInfo.put("login", userInfo.getLogin_websms());
        additionalInfo.put("password", userInfo.getPassword_websms());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}