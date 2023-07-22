package sn.kiwi.apiwebsms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sn.kiwi.apiwebsms.common.Common;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.dtos.AccountUserDto;
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.ProfilDto;
import sn.kiwi.apiwebsms.models.CustomerInfoReceivedModel;
import sn.kiwi.apiwebsms.models.GroupListModel;
import sn.kiwi.apiwebsms.models.PackModel;

@RestController
public class CustomerController {

    @Autowired
    private PathsProperties pathsProperties;
    Logger logger = LoggerFactory.getLogger(ContactController.class);
    Common common=new Common();
    @GetMapping(value = "account")
    @Operation(
            tags = {"Account"},
            operationId = "Account",
            summary = "Infos user account",
            description = "Get account of user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Info user account .",
                    content = @Content(schema = @Schema(implementation = CustomerInfoReceivedModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the account of user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountUserDto.class))})}
    )

    public ResponseEntity<?> getCustomerAccount(@RequestBody CustomerInfoReceivedModel customerInfoReceivedModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+customerInfoReceivedModel.getUser_id()+", customerId: "+customerInfoReceivedModel.getCustomer_id()+
                ", customerId: "+customerInfoReceivedModel.getCustomer_id()+ ", partnarId: "+customerInfoReceivedModel.getPartner_id()+", login: "+customerInfoReceivedModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: /customer/CustomerController.php, ACTION: GET_CUSTOMER_ACCOUNT" );
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/CustomerController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
        System.out.println("requestHeaders: " +requestHeaders);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("customerId", ""+ customerInfoReceivedModel.getCustomer_id());
        map.add("ACTION", "GET_CUSTOMER_ACCOUNT");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        if(response.getStatusCodeValue() != 200){
            logger.trace("Unable to get account");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get account", HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
        }
        JSONObject jsonResponse = new JSONObject(response.getBody().toString());
        JSONObject jsonResponseMapper = new JSONObject();
        jsonResponseMapper.put("remainingNumberOfSms", jsonResponse.get("remainingNumberOfSms"));
        jsonResponseMapper.put("expirationDate", jsonResponse.get("expirationDate"));
        AccountUserDto myAccount = mapper.readValue(jsonResponseMapper.toString(), AccountUserDto.class);
        logger.trace("myAccount: "+myAccount);
        return ResponseEntity.ok(myAccount);
    }


    @GetMapping(value = "profil", produces = "application/json")
    @Operation(
            tags = {"Profil"},
            operationId = "Profils",
            summary = "Get the Info Profil",
            description = "Get the Info Profil",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Info Profil.",
                    content = @Content(schema = @Schema(implementation = CustomerInfoReceivedModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the info user profil",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfilDto.class))})}
    )
    public ResponseEntity<?> getInfoProfil(@RequestBody CustomerInfoReceivedModel customerInfoReceivedModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+customerInfoReceivedModel.getUser_id()+", customerId: "+customerInfoReceivedModel.getCustomer_id()+
                ", customerId: "+customerInfoReceivedModel.getCustomer_id()+ ", partnarId: "+customerInfoReceivedModel.getPartner_id()+", login: "+customerInfoReceivedModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: /customer/CustomerController.php, ACTION: GET_CUSTOMER_ACCOUNT" );
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/UserController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ customerInfoReceivedModel.getUser_id());
        map.add("ACTION", "VIEW");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        if(response.getStatusCodeValue() != 200)
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get info profil ", HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);

        System.out.println("contactName: " +response.getBody());
        JSONObject jsonResponse = new JSONObject(response.getBody().toString());
        JSONObject jsonResponseMapper = new JSONObject();
        jsonResponseMapper.put("contactName", jsonResponse.get("contactName"));
        jsonResponseMapper.put("subscriptionDate", jsonResponse.get("createdDate"));
        //jsonResponseMapper.put("subscriptionType", requestHeaders.get("codeP").toString());
        System.out.println("prod: " +requestHeaders.get("Cookie").get(0));
        jsonResponseMapper.put("language", jsonResponse.get("language"));
        ProfilDto profil = mapper.readValue(jsonResponseMapper.toString(), ProfilDto.class);
        return ResponseEntity.ok(profil);
    }
}
