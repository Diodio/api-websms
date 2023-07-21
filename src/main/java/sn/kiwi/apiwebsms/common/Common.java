package sn.kiwi.apiwebsms.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sn.kiwi.apiwebsms.config.PathsProperties;

public class Common {

    public HttpHeaders setUserCookies(PathsProperties pathsProperties, String login, String password, long partnerId) throws Exception {
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/UserController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpHeaders requestPartnerHeaders = this.setPartnerCookies(pathsProperties);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("partner", "" + partnerId);
        map.add("login", login);
        map.add("password", password);
        map.add("captcha:", "0");
        map.add("ACTION", "SIGNIN");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("SIGNIN RESPONSE" + response);
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc == 0)
            throw new Exception("Login ou mot de passe incorrecte");
        JSONObject obj = (new JSONObject(response.getBody().toString())).getJSONObject("infos");
        requestHeaders.add("Cookie", "customerId=" + obj.get("customer"));
        requestHeaders.add("Cookie", "userId=" + obj.get("id"));
        requestHeaders.add("Cookie", "userLogin=" + obj.get("login"));
        requestHeaders.add("Cookie", "userContactName=" + obj.get("contactName"));
        requestHeaders.add("Cookie", "userProfil=" + obj.get("profil"));
        requestHeaders.add("Cookie", "userLanguage=" + obj.get("code"));
        requestHeaders.add("Cookie", "productName=" + obj.get("productName"));
        requestHeaders.add("Cookie", "codeP=" + obj.get("codeP"));
        requestHeaders.add("Cookie", "isPack=" + obj.get("isPack"));
        requestHeaders.add("Cookie", "abonnement=" + obj.get("abonnement"));
        requestPartnerHeaders.addAll(requestHeaders);
        return requestPartnerHeaders;
    }


    public HttpHeaders setPartnerCookies(PathsProperties pathsProperties) throws Exception {
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/common/DomainController.php";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("domainName", ""+ pathsProperties.getPathValue("backend.domain-name"));
        map.add("ACTION", "VIEW");
        System.out.println(map);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());
        JSONObject obj = (new JSONObject(response.getBody().toString())).getJSONObject("infos");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "partnerId=" + obj.get("id"));
        requestHeaders.add("Cookie", "partnerCode=" + obj.get("code"));
        requestHeaders.add("Cookie", "partnerName=" + obj.get("name"));
        requestHeaders.add("Cookie", "partnerTrademark=" + obj.get("trademark"));
        requestHeaders.add("Cookie", "partnerCellular=" + obj.get("cellular"));
        requestHeaders.add("Cookie", "partnerTemplate=" + obj.get("template"));
        requestHeaders.add("Cookie", "partnerEmail=" + obj.get("email"));
        requestHeaders.add("Cookie", "partnerLanguage=" + obj.get("lgeCode"));
        requestHeaders.add("Cookie", "partnerDebit=" + obj.get("debit"));
        requestHeaders.add("Cookie", "codeCountry=" + obj.get("codeCountry"));
        requestHeaders.add("Cookie", "domain=" + pathsProperties.getPathValue("backend.domain-name"));
        requestHeaders.add("Cookie", "provider=PORTAIL");
        requestHeaders.add("Cookie", "cap_code=e80321");
        return requestHeaders;
    }


}
