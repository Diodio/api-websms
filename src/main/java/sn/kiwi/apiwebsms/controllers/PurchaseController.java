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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sn.kiwi.apiwebsms.common.Common;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.constants.JsonPacksAudio;
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.PacksListDto;
import sn.kiwi.apiwebsms.dtos.PacksOMDto;
import sn.kiwi.apiwebsms.models.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class PurchaseController {

    Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PathsProperties pathsProperties;
    Common common=new Common();

    @GetMapping(value = "packs", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Get the list of packs",
            description = "Get the list of packs",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of packs.",
                    content = @Content(schema = @Schema(implementation = CustomerInfoReceivedModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of packs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacksListDto.class))})}
    )
    public ResponseEntity<?> getAllPack(@RequestBody CustomerInfoReceivedModel customerInfoReceivedModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
        RestTemplate restTemplate=new RestTemplate();
        try{
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("customerId", ""+ customerInfoReceivedModel.getCustomer_id());
        map.add("ACTION", "LIST_PURCHASES");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        if(response.getStatusCodeValue() != 200) {
            logger.warn("Unable to get the pack list ");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the pack list ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
            System.out.println("response: " +response.getBody());
        PacksListDto[] packs = mapper.readValue(response.getBody().toString(), PacksListDto[].class);
        return ResponseEntity.ok(packs);
    } catch (Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
    }
    }

    @GetMapping(value = "purchases/om", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Purchase a orange money pack",
            description = "Purchase a pack to recharge a customer orange money account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Purchase a orange money pack.",
                    content = @Content(schema = @Schema(implementation = PackModel.class))),
            responses = {@ApiResponse(responseCode = "201", description = "orange money Pack successfully purchased")}
    )

    public ResponseEntity<?> purchaseOM(@RequestBody PackModel packModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+packModel.getUser_id()+", customerId: "+packModel.getCustomer_id()+
                ", customerId: "+packModel.getCustomer_id()+ ", partnarId: "+packModel.getPartner_id()+", login: "+packModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: "+pathsProperties.getPathValue("backend.url")+"/product/PurchaseController.php, ACTION: ACTION_GET_PARAM_PURCHASE_OM" );

        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php";
        RestTemplate restTemplate=new RestTemplate();
        try{
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, packModel.getLogin(), packModel.getPassword(), packModel.getPartner_id());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ packModel.getUser_id());
        map.add("packId", ""+ packModel.getPack_id());
        map.add("typeMSG", ""+packModel.getType_msg());
        map.add("ACTION", "GET_PARAM_PURCHASE_OM");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);

        System.out.println("request: "+request);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("reponse: " +response);
        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.error("Error while processing your request. Please contact your administrator.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }else {
            System.out.println("Body: " + response.getBody());
            logger.trace("Body: " + response.getBody());

            String S2M_IDENTIFIANT = (String) (new JSONObject(response.getBody().toString())).get("S2M_IDENTIFIANT");
            String S2M_SITE = (String) (new JSONObject(response.getBody().toString())).get("S2M_SITE");
            System.out.println("S2M_IDENTIFIANT: " + S2M_IDENTIFIANT);
            String S2M_TOTAL = (String) (new JSONObject(response.getBody().toString())).get("S2M_TOTAL");
            String S2M_REF_COMMANDE = (String) (new JSONObject(response.getBody().toString())).get("S2M_REF_COMMANDE");
            String S2M_COMMANDE = (String) (new JSONObject(response.getBody().toString())).get("S2M_COMMANDE");
            String S2M_DATEH = (String) (new JSONObject(response.getBody().toString())).get("S2M_DATEH");
            String S2M_HTYPE = (String) (new JSONObject(response.getBody().toString())).get("S2M_HTYPE");
            String S2M_HMAC = (String) (new JSONObject(response.getBody().toString())).get("S2M_HMAC");
            String S2M_LINK = (String) (new JSONObject(response.getBody().toString())).get("S2M_LINK");
            JSONObject obj = new JSONObject();
            obj.put("idendifiant", S2M_IDENTIFIANT);

            obj.put("site", S2M_SITE);
            obj.put("total", S2M_TOTAL);
            obj.put("refCommand", S2M_REF_COMMANDE);
            obj.put("hashType", S2M_HTYPE);
            obj.put("dateH", S2M_DATEH);
            obj.put("link", S2M_LINK);
            obj.put("command", S2M_COMMANDE);
            obj.put("hashMac", S2M_HMAC);
            logger.trace("omInfos: " + obj);
            System.out.println("omInfos: " + obj);

            PacksOMDto packs = mapper.readValue(obj.toString(), PacksOMDto.class);
            logger.trace("packs: " + packs);
            return ResponseEntity.ok(packs);
        }
    } catch (Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
    }
    }

    //Purchase with phone credit

    //send sms
    @PostMapping(value = "purchases/ct/sendCode", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Send SMS to get code validation ",
            description = "Send SMS to get code validation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Send SMS to get code validation.",
                    content = @Content(schema = @Schema(implementation = PurchaseCTSendSMSModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Validation code is successfully purchased")}
    )

    public ResponseEntity<?> sendCodeValidation(@RequestBody PurchaseCTSendSMSModel purchaseCTSendSMSModel) throws Exception {
        logger.trace("************************** Start to send code validation ************************************");
        logger.trace("file: PurchaseController, function: sendSMS, userId:"+purchaseCTSendSMSModel.getUser_id()+", customerId: "+purchaseCTSendSMSModel.getCustomer_id()+
                ", customerId: "+purchaseCTSendSMSModel.getCustomer_id()+ ", partnarId: "+purchaseCTSendSMSModel.getPartner_id()+", login: "+purchaseCTSendSMSModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: "+pathsProperties.getPathValue("backend.url")+"/product/PurchaseController.php, ACTION: ACTION_GET_PARAM_PURCHASE_OM" );
        try {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/SimpleMessageController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, purchaseCTSendSMSModel.getLogin(), purchaseCTSendSMSModel.getPassword(), purchaseCTSendSMSModel.getPartner_id());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ purchaseCTSendSMSModel.getUser_id());
        map.add("recipient", ""+ purchaseCTSendSMSModel.getRecipient());
        map.add("customerId", ""+ purchaseCTSendSMSModel.getCustomer_id());
      //  map.add("parCode", ""+ requestHeaders.get("code"));
        //map.add("parCode", "646146");
        map.add("ACTION", "SEND_CODE");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);

        System.out.println("request: "+request);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("response1: " +response);
        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.error("Error while processing your request. Please contact your administrator.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }else {
            logger.trace("Body: " + response.getBody());
            int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
            if (rc != 0) {
                logger.warn("Unable to send code validation");
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to send code validation", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Message successfully sent.");
        }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }


    //validate purchases

    @PostMapping(value = "purchases/ct/validation", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Payment phone credit validation ",
            description = "Payment phone credit validation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment phone credit validation.",
                    content = @Content(schema = @Schema(implementation = PurchaseCTValidationModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Pack is successfully purchased")}
    )

    public ResponseEntity<?> doPurchaseCT(@RequestBody PurchaseCTValidationModel purchaseCTValidationModel) throws Exception {
        logger.trace("************************** Start to send code validation ************************************");
        logger.trace("file: PurchaseController, function: doPurchaseCT, userId:"+purchaseCTValidationModel.getUser_id()+", customerId: "+purchaseCTValidationModel.getCustomer_id()+
                ", customerId: "+purchaseCTValidationModel.getCustomer_id()+ ", partnarId: "+purchaseCTValidationModel.getPartner_id()+", login: "+purchaseCTValidationModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: "+pathsProperties.getPathValue("backend.url")+"/product/PurchaseController.php, ACTION: ACTION_GET_PARAM_PURCHASE_OM" );
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php";
            RestTemplate restTemplate=new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, purchaseCTValidationModel.getLogin(), purchaseCTValidationModel.getPassword(), purchaseCTValidationModel.getPartner_id());
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("userId", ""+ purchaseCTValidationModel.getUser_id());
            map.add("customerId", ""+ purchaseCTValidationModel.getCustomer_id());
            map.add("validationCode", ""+ purchaseCTValidationModel.getCodeValidation());
            map.add("packId", ""+ purchaseCTValidationModel.getPack_id());
            map.add("typeMSG", ""+purchaseCTValidationModel.getType_msg());
            map.add("ACTION", "PURCHASE_CT");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            System.out.println("request: "+request);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            System.out.println("reponse: " +response);
            if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
            }else {
                logger.trace("Body: " + response.getBody());
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if(rc != 0) {
                    logger.error("Impossible to make this purchase ");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Impossible to make this purchase", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("A pack is successfully purchased.");
                return  ResponseEntity.ok(new ApiDtoResponse(true, "A pack is successfully purchased."));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }


    //Purchase
    @GetMapping(value = "packs/audio", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Get the list of audio packs",
            description = "Get the list of packs",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of packs.",
                    content = @Content(schema = @Schema(implementation = CustomerInfoReceivedModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of audio packs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacksListDto.class))})}
    )
    public ResponseEntity<?> getAllAudioPacks(@RequestBody CustomerInfoReceivedModel customerInfoReceivedModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
        RestTemplate restTemplate=new RestTemplate();
        try{
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("customerId", ""+ customerInfoReceivedModel.getCustomer_id());
            map.add("ACTION", "LIST_PURCHASES_AUDIO");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            if(response.getStatusCodeValue() != 200) {
                logger.warn("Unable to get the audio packs list ");
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the audio packs list ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
            System.out.println("response: " +response.getBody());
            PacksListDto[] packs = mapper.readValue(JsonPacksAudio.JSON_LIST_PACKS_AUDIO, PacksListDto[].class);
            //PacksListDto[] packs = mapper.readValue(response.getBody().toString(), PacksListDto[].class);
            return ResponseEntity.ok(packs);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }

}