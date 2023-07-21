package sn.kiwi.apiwebsms.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sn.kiwi.apiwebsms.common.Common;
import sn.kiwi.apiwebsms.common.CommonVoice;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.constants.JsonClass;
import sn.kiwi.apiwebsms.dtos.CallBackDto;
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.MessageDetailDto;
import sn.kiwi.apiwebsms.dtos.MessageDto;
import sn.kiwi.apiwebsms.models.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private PathsProperties pathsProperties;

    Common common = new Common();

    @GetMapping(value = "messages", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Get the list of messages",
            description = "Get the list of messages sent by the customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of messages.",
                    content = @Content(schema = @Schema(implementation = CustomerModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of messages",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})}
    )
    public ResponseEntity<?> getAllMessages(@RequestBody MessagesListModel messagesListModel) throws Exception {
        logger.trace("************************** Start to get all groups ************************************");
        logger.trace("file: SimpleMessageController.java, getAllMessages, userId:"+messagesListModel.getUser_id()+", login: "+messagesListModel.getLogin()+"," +
                "costumerId: "+messagesListModel.getCustomer_id()+" clientId: orangesn, urlBackend: /message/SimpleMessageController.php, ACTION: LIST");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/SimpleMessageController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messagesListModel.getLogin(), messagesListModel.getPassword(), messagesListModel.getPartnerId());
        logger.trace("requestHeaders: "+requestHeaders);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ messagesListModel.getUser_id());
        map.add("customerId", ""+ messagesListModel.getCustomer_id());
        map.add("partnerId", ""+ messagesListModel.getPartnerId());
        map.add("ACTION", "LIST");
        map.add("partnerCode", "290573" );
        map.add("application_id", "WS" );
        map.add("sEcho", "1" );
        map.add("iColumns", "4" );
        map.add("sColumns", "" );
        map.add("iDisplayStart", "0" );
        map.add("iDisplayLength", "10" );
        map.add("mDataProp_0", "0" );
        map.add("iDisplayStart", "0" );
        map.add("mDataProp_1", "1" );
        map.add("mDataProp_2", "2" );
        map.add("mDataProp_3", "3" );
        map.add("sSearch", "" );
        map.add("bRegex", "false" );
        map.add("sSearch_0", "" );
        map.add("bRegex_0", "false" );
        map.add("bSearchable_0", "true" );
        map.add("sSearch_1", "" );
        map.add("bRegex_1", "false" );
        map.add("bSearchable_1", "true" );
        map.add("sSearch_2", "false" );
        map.add("bSearchable_2", "true" );
        map.add("bRegex_0", "" );
        map.add("sSearch_3", "false" );
        map.add("iSortCol_0", "0" );
        map.add("sSortDir_0", "asc" );
        map.add("iSortingCols", "1" );
        map.add("bSortable_0", "false" );
        map.add("bSortable_1", "false" );
        map.add("bSortable_2", "true" );
        map.add("bSortable_3", "true" );

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        logger.trace("response: "+response);

        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.trace("Error while processing your request. Please contact your administrator.");
            logger.trace("************************** End to get all groups ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

        }else {
           /* MessageDto[] groups = mapper.readValue(response.getBody().toString(), MessageDto[].class);
            logger.trace("list Groups: "+ response);
            logger.trace("groups: "+groups);
            logger.trace("************************** End to get all groups ************************************");
            return ResponseEntity.ok(groups);*/
            return null;
        }
    }

    @PostMapping(value = "messages/send", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Send sms messages",
            description = "send sms messages",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Send sms messages.",
                    content = @Content(schema = @Schema(implementation = MessageModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Message successfully sent")}
    )
    public ResponseEntity<?> sendSMS(@RequestBody MessageModel messageModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+messageModel.getUser_id()+", customerId: "+messageModel.getCustomer_id()+"," +
                "customerId: "+messageModel.getCustomer_id()+ ", clientId: orangesn, urlBackend: /message/SimpleMessageController.php, ACTION: SEND," +
                "SignatureId:" +messageModel.getSignature());
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/SimpleMessageController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = common.setUserCookies(pathsProperties, messageModel.getLogin(), messageModel.getPassword(), messageModel.getPartner_id());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", String.valueOf(messageModel.getUser_id()));
        map.add("partnerCode", String.valueOf(messageModel.getPartnerCode()));
        map.add("customerId", String.valueOf(messageModel.getCustomer_id()));
        map.add("groups", String.valueOf(messageModel.getGroups()));
        map.add("signatureName", String.valueOf(messageModel.getSignatureName()));
        map.add("groupName", String.valueOf(messageModel.getGroupName()));
        map.add("numberMessage", String.valueOf(messageModel.getNumberMessage()));
        map.add("messageLength", String.valueOf(messageModel.getMessageLength()));
        map.add("repeatedSMS", "0");
        map.add("numberOrdreEnvoi", "0");
        map.add("subject", String.valueOf(messageModel.getSubject()));
        map.add("signature", String.valueOf(messageModel.getSignature()));
        map.add("recipients", String.valueOf(messageModel.getRecipients()));
        map.add("content", String.valueOf(messageModel.getContent()));
        map.add("sendingType", String.valueOf(messageModel.getSendingType()));
        map.add("sendingDate", String.valueOf(messageModel.getSendingDate()));
        map.add("hour", String.valueOf(messageModel.getHour()));
        map.add("minute", String.valueOf(messageModel.getMinute()));
        map.add("sendingTime", String.valueOf(messageModel.getSendingTime()));
        map.add("ACTION", "SEND");
        HttpEntity<MultiValueMap<String, String>> requestHeader =
                new HttpEntity<>(map, headers);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestHeader, String.class);
        System.out.println(response.getBody());

        return ResponseEntity.status(HttpStatus.OK).body("Message successfully sent.");
    }


    @PostMapping(value = "messages/sendVoice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Send voice messages",
            description = "send voice messages",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Send voice messages.",
                    content = @Content(schema = @Schema(implementation = VoiceModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Message successfully sent")}
    )

    public ResponseEntity<?> sendVoice(@ModelAttribute VoiceModel voiceModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+voiceModel.getUser_id()+", customerId: "+voiceModel.getCustomer_id()+"," +
                "customerId: "+voiceModel.getCustomer_id()+ ", clientId: orangesn");
         CommonVoice commonVoice = new CommonVoice();
        String callId = "";
        String accessToken = commonVoice.getAPIVoiceToken();
        commonVoice.saveUploadedFile(pathsProperties, voiceModel.getFile());
        //File file = new File("C:\\Users\\MOI\\Documents\\uploadVoice\\" + voiceModel.getFile().getOriginalFilename());
        File file = new File(pathsProperties.getPathValue("backend.files-directory") + voiceModel.getFile().getOriginalFilename());
        String mediaUri = commonVoice.uploadFile(accessToken, "Kiwi6", file);
        callId = commonVoice.sendVoice(accessToken, mediaUri, voiceModel.getRecipient());
        logger.trace("Info voice:");
        logger.trace("file: "+file+", tocken: "+accessToken+", function: commonVoice(saveUploadedFile, uploadFile, sendVoice), callId: "+callId+
                "mediaName: "+mediaUri+ ", recipientDest: "+voiceModel.getRecipient());
        logger.trace("callId: "+callId);
        System.out.println("callId: "+callId);
        if (!callId.equalsIgnoreCase("")) {
            logger.trace("Voice message sent.");
            logger.trace("************************** End to send message voice ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Voice message sent.");
        } else {
            logger.trace("Voice Message not sent.");
            logger.trace("************************** End to send message voice ************************************");
            return ResponseEntity.status(HttpStatus.OK).body("Voice Message not sent.");
        }
    }


    @GetMapping(value = "message/detail", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Get the detail of the message",
            description = "Get the detail of the message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The detail of message.",
                    content = @Content(schema = @Schema(implementation = InfoMessageDetailModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the detail of message",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})}
    )
    public ResponseEntity<?> getDetailMessage(@RequestBody InfoMessageDetailModel infoMessageDetailModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: getDetailMessage, userId:"+infoMessageDetailModel.getUser_id()+", login: "+infoMessageDetailModel.getLogin()+"," +
                "partnerId: "+infoMessageDetailModel.getPartner_id()+ ", clientId: orangesn");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/SimpleMessageController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = common.setUserCookies(pathsProperties, infoMessageDetailModel.getLogin(), infoMessageDetailModel.getPassword(), infoMessageDetailModel.getPartner_id());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", String.valueOf(infoMessageDetailModel.getUser_id()));
        map.add("messageId", String.valueOf(infoMessageDetailModel.getMessage_Id()));
        map.add("tmsg", "MS");
        map.add("ACTION", "VIEW_DETAILS");
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity<>(map, headers);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestHeader, String.class);

        logger.trace("response: "+response);
        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.trace("Erreur Server");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur Server.");
        }else {
            MessageDetailDto messageDetailDto = mapper.readValue(response.getBody().toString(), MessageDetailDto.class);
            logger.trace("messageDetailDto: "+messageDetailDto);
            return ResponseEntity.ok(messageDetailDto);
        }
    }


    @GetMapping(value = "messages/callbackVoice", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Details call",
            description = "Get details information of the voice message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details CallaBack information.",
                    content = @Content(schema = @Schema(implementation = CustomerModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the details of the voice message",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CallBackModel.class))})}
    )
    public ResponseEntity<?> getDetailsCallById(@RequestBody CallBackModel callBackModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("API Voice, function: getDetailsCallById, callId:"+callBackModel.getCall_id());
        //String callId="7bc4efa5-c871-4482-a740-80c14e3a93e7";
        String callId=callBackModel.getCall_id();
        System.out.println("OK");
        CommonVoice commonVoice = new CommonVoice();
        String accessToken = commonVoice.getAPIVoiceToken();
        System.out.println("Call_id: " +callBackModel.getCall_id().toString());
        logger.trace("Call_id: " +callBackModel.getCall_id().toString());
        Map<String, String> infoSessionCall = commonVoice.getStatusByCallId(callId, accessToken);
        System.out.println("infoSessionCall 1: " +infoSessionCall);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.convertValue(infoSessionCall, JsonNode.class);
        System.out.println("json infoSessionCall: "+json);
        logger.trace("json infoSessionCall: "+json);

        return ResponseEntity.ok(json);
    }

}