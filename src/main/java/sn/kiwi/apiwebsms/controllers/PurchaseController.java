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
import sn.kiwi.apiwebsms.dtos.*;
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
            summary = "Get the list of sms and voice packs",
            description = "Get the list of sms and voice packs",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of sms and voice packs.",
                    content = @Content(schema = @Schema(implementation = PackListModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of sms and voice packs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacksListDto.class))})}
    )
    public ResponseEntity<?> getAllPack(@RequestBody PackListModel packListModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
        RestTemplate restTemplate=new RestTemplate();
        try{
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, packListModel.getLogin(), packListModel.getPassword(), packListModel.getPartner_id());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("customerId", ""+ packListModel.getCustomer_id());
        map.add("customerId", ""+ packListModel.getCustomer_id());
        map.add("typeMSG", ""+packListModel.getType_msg());
        map.add("ACTION", "LIST_PURCHASES");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        if(response.getStatusCodeValue() != 200) {
            logger.warn("Unable to get the pack list ");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the pack sms and voice list ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

            System.out.println("response: " +response.getBody());
            PacksListDto[] packs = mapper.readValue(response.getBody().toString(), PacksListDto[].class);
            return ResponseEntity.ok(packs);

           // System.out.println("typemsg: " +packListModel.getType_msg());
        /*if(!packListModel.getType_msg().equals("VOICE")) {
            System.out.println("test 1");
            PacksListDto[] packs = mapper.readValue(response.getBody().toSt ring(), PacksListDto[].class);
            return ResponseEntity.ok(packs);
        }
        else {
            System.out.println("test 2");
            PacksVoiceListDto[] packsV = mapper.readValue(response.getBody().toString(), PacksVoiceListDto[].class);
            return ResponseEntity.ok(packsV);
        }
*/


        /*    JSONArray packData = new JSONArray(response.getBody().toString());
            logger.trace("response packData: "+packData);
            JSONArray allPack = new JSONArray();
            packData.forEach(item -> {
                JSONObject packJson = new JSONObject();
                logger.trace("item: "+item);
                JSONObject it = (JSONObject) item;
                logger.trace("it: "+it);
                packJson.put("id", (String) it.get("id"));
                packJson.put("name", (String) it.get("name"));
                packJson.put("duration", (String) it.get("duration"));
                packJson.put("description", (String) it.get("description"));

                System.out.println("typeMSG: " +it.get("typeMSG"));
                //if(it.get("typeMSG")=="VOICE"){
                packJson.put("typeMSG", (String) it.get("typeMSG"));
                packJson.put("voiceNumberOfSecond", (String) it.get("voiceNumberOfSecond"));
                //}
                //else
                packJson.put("numberOfCredits", (String) it.get("numberOfCredits"));
                allPack.put(packJson);
            });

            System.out.println("response: " +response.getBody());
            PacksListDto[] packs = mapper.readValue(allPack.toString(), PacksListDto[].class);
            return ResponseEntity.ok(packs);*/
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

    public ResponseEntity<?> purchaseOMQRCODE(@RequestBody PackModel packModel) throws Exception {
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
        map.add("customerId", ""+packModel.getCustomer_id());
        map.add("refCommand", ""+packModel.getRefCommand());
        map.add("ACTION", "GET_PURCHASE_OM_QRCODE");
        //map.add("ACTION", "GET_PARAM_PURCHASE_OM");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);

        System.out.println("RefCommand: "+packModel.getRefCommand());
        System.out.println("request: "+request);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("reponse: " +response);
        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.error("Error while processing your request. Please contact your administrator.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }else {
            System.out.println("Body: " + response.getBody());
            logger.trace("Body: " + response.getBody());

            /*String amount = (String) (new JSONObject(response.getBody().toString())).get("amount");
            System.out.println("amount: " + amount);
            String message = (String) (new JSONObject(response.getBody().toString())).get("message");
            JSONObject obj = new JSONObject();
            obj.put("amount", amount);
            obj.put("message", message);
            logger.trace("omInfos: " + obj);
            System.out.println("omInfos: " + obj);*/

            PacksOMDto packs = mapper.readValue(response.getBody().toString(), PacksOMDto.class);
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
   /* @GetMapping(value = "packs/audio", produces = "application/json")
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
          //  PacksListDto[] packs = mapper.readValue(response.getBody().toString(), PacksListDto[].class);
            return ResponseEntity.ok(packs);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }*/


    //Promotion
    @GetMapping(value = "promo", produces = "application/json")
    @Operation(
            tags = {"Promo"},
            operationId = "Promo",
            summary = "Get the list of promotions",
            description = "Get the list of promotions",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of promotion.",
                    content = @Content(schema = @Schema(implementation = PromoModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of promotions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacksListDto.class))})}
    )
    public ResponseEntity<?> getAllPromo(@RequestBody PromoModel promoModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
        RestTemplate restTemplate=new RestTemplate();
        String imageurl = pathsProperties.getPathValue("backend.link-promo-pub") +"/promopub/images/" ;
        String audiourl = pathsProperties.getPathValue("backend.link-promo-pub") +"/promopub/audios/" ;
        try{
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, promoModel.getLogin(), promoModel.getPassword(), promoModel.getPartner_id());
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("partnerId", ""+ promoModel.getPartner_id());
            map.add("ACTION", "RETRIEVE_ALL_PROMO");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("body: " +response.getBody());
            if(response.getBody()==null) {
                logger.warn("Unable to get the promotion list ");
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the promotion list", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
            else {
                System.out.println("response: " +response.getBody());
                JSONArray promoData = new JSONArray(response.getBody().toString());
                logger.trace("response promoData: "+promoData);
                JSONArray allPromo = new JSONArray();
                promoData.forEach(item -> {
                    JSONObject promoJson = new JSONObject();
                    logger.trace("item: "+item);
                    JSONObject it = (JSONObject) item;
                    logger.trace("it: "+it);
                    promoJson.put("id", (String) it.get("id"));
                    promoJson.put("audio", audiourl+(String) it.get("audio"));
                    promoJson.put("image", imageurl+(String) it.get("image"));
                    logger.trace("image: "+imageurl+(String) it.get("image"));
                    promoJson.put("createdDate", (String) it.get("createdDate"));
                    promoJson.put("startDate", (String) it.get("startDate"));
                    promoJson.put("endDate", (String) it.get("endDate"));
                    allPromo.put(promoJson);
                });
                PromoListDto[] pub = mapper.readValue(allPromo.toString(), PromoListDto[].class);
                return ResponseEntity.ok(pub);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }



    //Publicité
    @GetMapping(value = "pub", produces = "application/json")
    @Operation(
            tags = {"Pub"},
            operationId = "Pub",
            summary = "Get the list of publicity",
            description = "Get the list of publicity",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of publicity.",
                    content = @Content(schema = @Schema(implementation = PubModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of publicity",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacksListDto.class))})}
    )
    public ResponseEntity<?> getAllPub(@RequestBody PubModel pubModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
        RestTemplate restTemplate=new RestTemplate();
        String imageurl = pathsProperties.getPathValue("backend.link-promo-pub") +"/promopub/images/" ;
        logger.trace("imageurl: " +imageurl);
        System.out.println("imageurl: " +imageurl);
        try{
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, pubModel.getLogin(), pubModel.getPassword(), pubModel.getPartner_id());
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("partnerId", ""+ pubModel.getPartner_id());
            map.add("ACTION", "RETRIEVE_ALL_PUB");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("body: " +response.getBody());
            if(response.getBody()==null) {
                logger.warn("Unable to get the publicity list ");
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the publicity list", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
            else {

                System.out.println("imageurl: " +imageurl);
                System.out.println("response: " +response.getBody());
                JSONArray pubData = new JSONArray(response.getBody().toString());
                logger.trace("response 2 pubData: "+pubData);
                JSONArray allPub = new JSONArray();
                pubData.forEach(item -> {
                    JSONObject pubJson = new JSONObject();
                    logger.trace("item: "+item);
                    JSONObject it = (JSONObject) item;
                    logger.trace("it: "+it);
                    pubJson.put("id", (String) it.get("id"));
                    pubJson.put("image", imageurl+(String) it.get("image"));
                    logger.trace("image: "+imageurl+(String) it.get("image"));
                    pubJson.put("createdDate", (String) it.get("createdDate"));
                    pubJson.put("startDate", (String) it.get("startDate"));
                    pubJson.put("endDate", (String) it.get("endDate"));
                    allPub.put(pubJson);
                });
                //PubListDto[] contacts = mapper.readValue(allPromo.toString(), PubListDto[].class);
                    PubListDto[] pub = mapper.readValue(allPub.toString(), PubListDto[].class);
                return ResponseEntity.ok(pub);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }
}