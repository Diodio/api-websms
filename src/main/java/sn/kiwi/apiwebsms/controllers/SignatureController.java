package sn.kiwi.apiwebsms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sn.kiwi.apiwebsms.common.Common;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.SignatureDto;
import sn.kiwi.apiwebsms.models.CustomerInfoReceivedModel;
import sn.kiwi.apiwebsms.models.SignatureAddModel;
import sn.kiwi.apiwebsms.models.SignatureDeleteModel;

import javax.validation.Valid;

@RestController
public class SignatureController {

    Logger logger = LoggerFactory.getLogger(SignatureController.class);

    @Autowired
    private PathsProperties pathsProperties;
    Common common = new Common();
    @GetMapping(value = "signatures", produces = "application/json")
    @Operation(
            tags = {"Signatures"},
            operationId = "Signatures",
            summary = "Get the list of validated, pending and blocked signatures",
            description = "Get the list of validated, pending and blocked signatures",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of signatures.",
                    content = @Content(schema = @Schema(implementation = CustomerInfoReceivedModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of signatures",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignatureDto.class))})}
    )
    public ResponseEntity<?> getAllSignatures(@RequestBody CustomerInfoReceivedModel customerInfoReceivedModel) throws Exception {

        logger.trace("************************** Start to Get Signature ************************************");
        logger.trace("file: SignatureController, fonction: getAllSignatures, userId:"+customerInfoReceivedModel.getUser_id()+", customerId: "+customerInfoReceivedModel.getCustomer_id()+"," +
                "login: "+customerInfoReceivedModel.getLogin()+" clientId: orangesn, urlBackend: /customer/SignatureController.php, ACTION: LIST");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/SignatureController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = common.setUserCookies(pathsProperties, customerInfoReceivedModel.getLogin(), customerInfoReceivedModel.getPassword(), customerInfoReceivedModel.getPartner_id());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("customerId", String.valueOf(customerInfoReceivedModel.getCustomer_id()));
        map.add("ACTION", "LIST");
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity<>(map, headers);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestHeader, String.class);
        SignatureDto[] signatures = mapper.readValue(response.getBody().toString(), SignatureDto[].class);
        logger.trace("ListSignature: "+ response);
        logger.trace("************************** End to Get Signature ************************************");
        return ResponseEntity.ok(signatures);
    }

    @PostMapping(value = "signatures")
    @Operation(
            tags = {"Signatures"},
            operationId = "Signatures",
            summary = "Create a new signature",
            description = "Create a new signature for a user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new signature.",
                    content = @Content(schema = @Schema(implementation = SignatureAddModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Signature successfully created")}
    )
    public ResponseEntity<?> createSignature(@RequestBody SignatureAddModel signatureAddModel) throws Exception {
        logger.trace("************************** Start to Create Signature ************************************");
        logger.trace("file: SignatureController, fonction: createSignature, userId:"+signatureAddModel.getUser_id()+", customerId: "+signatureAddModel.getCustomer_id()+"," +
                "login: "+signatureAddModel.getLogin()+" clientId: orangesn, urlBackend: /customer/SignatureController.php, ACTION: INSERT");
        try {

            String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/SignatureController.php";
            RestTemplate restTemplate=new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, signatureAddModel.getLogin(), signatureAddModel.getPassword(), signatureAddModel.getPartnerId());
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("userId", ""+signatureAddModel.getUser_id());
            map.add("customerId", ""+signatureAddModel.getCustomer_id());
            map.add("libellesignature", ""+signatureAddModel.getName());
            map.add("ACTION", "INSERT");
            map.add("libelleSignature", ""+signatureAddModel.getName());
            map.add("app", "mobile");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: "+response);
            if(response!=null || response.getBody()!=null){
                logger.trace("response body: "+response.getBody());
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    String error = (new JSONObject(response.getBody().toString())).getString("error");
                    logger.error("Error: "+error);
                    logger.error("************************** End to Create Signature ************************************");
                    return new ResponseEntity<>(new ApiDtoResponse(false, error, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
            }

            logger.trace("Signature created: " + signatureAddModel.getName());
            logger.trace("************************** End to Create Signature ************************************");
            return ResponseEntity.ok(new ApiDtoResponse(true, "A new signature was successfully created."));
        }
        catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }

    }

    @DeleteMapping(value = "signatures")
    @Operation(
            tags = {"Signatures"},
            operationId = "Signatures",
            summary = "Delete a signature",
            description = "Delete a signature",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Delete a signature.",
                    content = @Content(schema = @Schema(implementation = SignatureDeleteModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Signature was successfully deleted")}
    )
    public ResponseEntity<?> deleteSignature(@RequestBody SignatureDeleteModel signatureDeleteModel) throws Exception {
        logger.trace("************************** Start to delete Signature ************************************");
        logger.trace("file: SignatureController, fonction: deleteSignature, userId:"+signatureDeleteModel.getUser_id()+", login: "+signatureDeleteModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: /customer/SignatureController.php, ACTION: REMOVE");
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/customer/SignatureController.php";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, signatureDeleteModel.getLogin(), signatureDeleteModel.getPassword(), signatureDeleteModel.getPartnerId());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("userId", "" + signatureDeleteModel.getUser_id());
            map.add("signatureId", "" + signatureDeleteModel.getSignatureId());
            logger.trace("signatureId", "" + signatureDeleteModel.getSignatureId());
            map.add("ACTION", "REMOVE");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: "+response);
            if(response!=null || response.getBody()!=null){
                logger.trace("response body: "+response.getBody());
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    String error = (new JSONObject(response.getBody().toString())).getString("error");
                    logger.error("Error: "+error);
                    logger.error("************************** End to Create Signature ************************************");
                    return new ResponseEntity<>(new ApiDtoResponse(false, error, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
            }
                System.out.println("response: " + response.getBody());
                logger.trace("response body: "+response.getBody());
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    String error = (new JSONObject(response.getBody().toString())).getString("error");
                    logger.error("Error: "+error);
                    logger.error("************************** End to Create Signature ************************************");
                    return new ResponseEntity<>(new ApiDtoResponse(false, error, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }

            logger.trace("Signature was successfully deleted");
            logger.trace("************************** End to Delete Signature ************************************");
            return ResponseEntity.ok(new ApiDtoResponse(false, "Signature was successfully deleted."));
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }


}