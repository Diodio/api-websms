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
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.PacksListDto;
import sn.kiwi.apiwebsms.dtos.PacksOMDto;
import sn.kiwi.apiwebsms.models.CustomerInfoReceivedModel;
import sn.kiwi.apiwebsms.models.CustomerModel;
import sn.kiwi.apiwebsms.models.PackModel;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class PurchaseController {

    Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PathsProperties pathsProperties;
    Common common=new Common();

    @PostMapping(value = "packs", produces = "application/json")
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
        logger.trace("************************** Start to purchage OM ************************************");
        logger.trace("file: PackController, function: sendVoice, userId:" + customerInfoReceivedModel.getUser_id() + ", customerId: " + customerInfoReceivedModel.getCustomer_id() +
                ", customerId: " + customerInfoReceivedModel.getCustomer_id() + ", partnerId: " + customerInfoReceivedModel.getPartner_id() + ", login: " + customerInfoReceivedModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: " + pathsProperties.getPathValue("backend.url") + "/product/PackController.php, ACTION: LIST_PURCHASES");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PackController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("customerId", "" + customerInfoReceivedModel.getCustomer_id());
            map.add("ACTION", "LIST_PURCHASES");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: " + response);
            if (response.getStatusCodeValue() != 200) {
                logger.error("Unable to get the pack list");
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to get the pack list ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            } else {
                System.out.println("response: " + response.getBody());
                PacksListDto[] packs = mapper.readValue(response.getBody().toString(), PacksListDto[].class);

                logger.trace("************************** End to get purchase OM  ************************************");
                return ResponseEntity.ok(packs);
            }
        }
        catch(Exception e) {
            logger.error("************************** End to get purchase OM  ************************************");
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
        logger.trace("************************** Start to purchage OM ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:" + packModel.getUser_id() + ", customerId: " + packModel.getCustomer_id() +
                ", customerId: " + packModel.getCustomer_id() + ", partnarId: " + packModel.getPartner_id() + ", login: " + packModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: " + pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php, ACTION: ACTION_GET_PARAM_PURCHASE_OM");

        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php";
        RestTemplate restTemplate = new RestTemplate();
        try{
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, packModel.getLogin(), packModel.getPassword(), packModel.getPartner_id());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "" + packModel.getPartner_id());
        map.add("partnerId", "" + packModel.getPartner_id());
        map.add("packId", "" + packModel.getPack_id());
        map.add("ACTION", "GET_PARAM_PURCHASE_OM");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);

        System.out.println("request: " + request);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("reponse: " + response);
        if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
            logger.error("Error while processing your request. Please contact your administrator.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        } else {
            System.out.println("Body: " + response.getBody());
            logger.trace("Body: " + response.getBody());
            String S2M_IDENTIFIANT = (new JSONObject(response.getBody().toString())).getString("S2M_IDENTIFIANT");
            String S2M_SITE = (new JSONObject(response.getBody().toString())).getString("S2M_SITE");
            String S2M_TOTAL = (new JSONObject(response.getBody().toString())).getString("S2M_TOTAL");
            String S2M_REF_COMMANDE = (new JSONObject(response.getBody().toString())).getString("S2M_REF_COMMANDE");
            String S2M_COMMANDE = (new JSONObject(response.getBody().toString())).getString("S2M_COMMANDE");
            String S2M_DATEH = (new JSONObject(response.getBody().toString())).getString("S2M_DATEH");
            String S2M_HTYPE = (new JSONObject(response.getBody().toString())).getString("S2M_HTYPE");
            String S2M_LINK = (new JSONObject(response.getBody().toString())).getString("S2M_LINK");
            JSONObject obj = new JSONObject();
            obj.put("idendifiant", S2M_IDENTIFIANT);
            obj.put("site", S2M_SITE);
            obj.put("total", S2M_TOTAL);
            obj.put("refCommand", S2M_REF_COMMANDE);
            obj.put("hashType", S2M_HTYPE);
            obj.put("dateH", S2M_DATEH);
            obj.put("link", S2M_LINK);
            obj.put("command", S2M_COMMANDE);
            PacksOMDto omInfosDto = mapper.readValue(obj.toString(), PacksOMDto.class);
            logger.trace("************************** End to get purchase OM  ************************************");
            return ResponseEntity.ok(omInfosDto);
        }
    }
    catch(Exception e) {
        logger.error("************************** End to get purchase OM  ************************************");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
     }
    }


    @PostMapping(value = "purchases/ct", produces = "application/json")
    @Operation(
            tags = {"Purchases"},
            operationId = "Purchases",
            summary = "Purchase a phone money",
            description = "Purchase a pack to recharge a customer phone credit account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Purchase a orange money pack.",
                    content = @Content(schema = @Schema(implementation = PackModel.class))),
            responses = {@ApiResponse(responseCode = "201", description = "phone credit Pack successfully purchased")}
    )

    public ResponseEntity<?> purchasePhoneCredit(@RequestBody PackModel packModel) throws Exception {
        logger.trace("************************** Start to s voice ************************************");
        logger.trace("file: MessageController, function: purchasePhoneCredit, userId:" + packModel.getUser_id() + ", customerId: " + packModel.getCustomer_id() +
                ", customerId: " + packModel.getCustomer_id() + ", partnerId: " + packModel.getPartner_id() + ", login: " + packModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: " + pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php, ACTION: ACTION_GET_PARAM_PURCHASE_CT" +
                "");

        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/product/PurchaseController.php";
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, packModel.getLogin(), packModel.getPassword(), packModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("userId", "" + packModel.getPartner_id());
            map.add("customerId", "" + packModel.getCustomer_id());
            map.add("partnerId", "" + packModel.getPartner_id());
            map.add("validationCode", "" + packModel.getValidationCode());
            map.add("packId", "" + packModel.getPack_id());
            map.add("ACTION", "GET_PARAM_PURCHASE_CT");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);

            System.out.println("request: " + request);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            System.out.println("reponse: " + response);
            System.out.println("body: " + response.getBody());
            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.error("Error while processing your request. Please contact your administrator.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
            }
            else {
                //System.out.println("Body: " + response.getBody());
                logger.trace("Body: " + response.getBody());
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if(rc != 0) {
                    logger.error("Pack already purchased");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Pack already purchased", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("A pack is successfully purchased.");
                return  ResponseEntity.ok(new ApiDtoResponse(true, "A pack is successfully purchased."));
            }
        }
        catch(Exception e) {
            logger.error("Error while processing your request. Please contact your administrator.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }
}