package sn.kiwi.apiwebsms.controllers;

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
import sn.kiwi.apiwebsms.constants.JsonAudioMessage;
import sn.kiwi.apiwebsms.constants.JsonStats;
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.MessageDetailDto;
import sn.kiwi.apiwebsms.dtos.MessageListDto;
import sn.kiwi.apiwebsms.dtos.MessagesStatsDto;
import sn.kiwi.apiwebsms.models.*;

import java.io.File;
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
            summary = "Get the list of SMS and VOICE messages",
            description = "Get the list of SMS and VOICE messages sent by the customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of SMS and VOICE messages.",
                    content = @Content(schema = @Schema(implementation = CustomerModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of messages",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageListDto.class))})}
    )

    public ResponseEntity<?> getAllMessages(@RequestBody MessagesListModel messagesListModel) throws Exception {
        logger.trace("************************** Start to get all messages ************************************");
        logger.trace("file: CampaignController.java, getAllMessages, userId:" + messagesListModel.getUser_id() + ", login: " + messagesListModel.getLogin() + "," +
                "costumerId: " + messagesListModel.getCustomer_id() + " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: LIST");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messagesListModel.getLogin(), messagesListModel.getPassword(), messagesListModel.getPartnerId());
            //logger.trace("requestHeaders: " + requestHeaders);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("userId", "" + messagesListModel.getUser_id());
            map.add("customerId", "" + messagesListModel.getCustomer_id());
            map.add("application_id", "WS");
            map.add("ACTION", "LIST_DETAILS");
            map.add("partnerCode", "290573");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response 1: " + response);
            logger.trace("status: " + response.getStatusCode());

            JSONArray jsonRep = new JSONArray(response.getBody().toString());
            System.out.println("result: "+jsonRep);

            if ((response.getStatusCode().value() != 200) || (response.getStatusCode().value()==200 && jsonRep.isEmpty())) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to get all messages ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data available.");
            } else {
                logger.trace("body: " + response.getBody());
                MessageListDto[] messageDto = mapper.readValue(response.getBody().toString(), MessageListDto[].class);
                logger.trace("************************** End to get all messages ************************************");
                return ResponseEntity.ok(messageDto);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
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
        logger.trace("file: MessageController, function: sendVoice, userId:" + messageModel.getUser_id() + ", customerId: " + messageModel.getCustomer_id() + "," +
                "customerId: " + messageModel.getCustomer_id() + ", clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: SEND," +
                "SignatureId:" + messageModel.getSignature());
        try {
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
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
            map.add("sourceType", "C");
            map.add("recipients", String.valueOf(messageModel.getRecipients()));
            map.add("groups", String.valueOf(messageModel.getGroups()));
            map.add("content", String.valueOf(messageModel.getContent()));
            map.add("sendingType", String.valueOf(messageModel.getSendingType()));
            map.add("sendingDate", String.valueOf(messageModel.getSendingDate()));
            map.add("hour", String.valueOf(messageModel.getHour()));
            map.add("minute", String.valueOf(messageModel.getMinute()));
            map.add("sendingTime", String.valueOf(messageModel.getSendingTime()));
           // map.add("ACTION", "SEND");
            map.add("sendingType", "I");
            map.add("notSendBefore", "08:00");
            map.add("notSendAfter", "20:00");
            map.add("nbrLines", "");
            map.add("numberMessage", String.valueOf(messageModel.getNumberMessage()));
            map.add("messageLength", String.valueOf(messageModel.getMessageLength()));
            map.add("partnerCode", String.valueOf(messageModel.getPartnerCode()));
            map.add("appId", "WS");
            map.add("messageId", "0");
            map.add("ACTION", "START");
            HttpEntity<MultiValueMap<String, String>> requestHeader =
                    new HttpEntity<>(map, headers);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestHeader, String.class);
            System.out.println(response.getBody());
            logger.trace("Body: "+response.getBody());
            int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
            logger.trace("rc: "+rc);
            if(rc == 0) {
                return ResponseEntity.status(HttpStatus.OK).body("Message successfully sent.");
            }
            else {
                return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to send message", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            logger.trace("************************** END SEND MESSSAGE ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catch Error while processing your request. Please contact your administrator.");
        }
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
        logger.trace("file: MessageController, function: sendVoice, userId:" + voiceModel.getUser_id() + ", customerId: " + voiceModel.getCustomer_id() + "," +
                "customerId: " + voiceModel.getCustomer_id() + ", clientId: orangesn");
        CommonVoice commonVoice = new CommonVoice();
        String callId = "";
        String accessToken = commonVoice.getAPIVoiceToken();
        commonVoice.saveUploadedFile(pathsProperties, voiceModel.getFile());
        //File file = new File("C:\\Users\\MOI\\Documents\\uploadVoice\\" + voiceModel.getFile().getOriginalFilename());
        File file = new File(pathsProperties.getPathValue("backend.files-directory") + voiceModel.getFile().getOriginalFilename());
        String mediaUri = commonVoice.uploadFile(accessToken, "Kiwi6", file);
        callId = commonVoice.sendVoice(accessToken, mediaUri, voiceModel.getRecipient());
        logger.trace("Info voice:");
        logger.trace("file: " + file + ", tocken: " + accessToken + ", function: commonVoice(saveUploadedFile, uploadFile, sendVoice), callId: " + callId +
                "mediaName: " + mediaUri + ", recipientDest: " + voiceModel.getRecipient());
        logger.trace("callId: " + callId);
        System.out.println("callId: " + callId);
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
                            schema = @Schema(implementation = MessageListDto.class))})}
    )
    public ResponseEntity<?> getDetailMessage(@RequestBody InfoMessageDetailModel infoMessageDetailModel) throws Exception {
        logger.trace("************************** Start to get message details ************************************");
        logger.trace("file: MessageController, function: getDetailMessage, userId:" + infoMessageDetailModel.getUser_id() + ", login: " + infoMessageDetailModel.getLogin() + "," +
                "partnerId: " + infoMessageDetailModel.getPartner_id() + ", clientId: orangesn");
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

        logger.trace("response: " + response);
        if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
            logger.error("Error while processing your request. Please contact your administrator");
            logger.trace("************************** End to get message details ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator");
        } else {
            MessageDetailDto messageDetailDto = mapper.readValue(response.getBody().toString(), MessageDetailDto.class);
            logger.trace("messageDetailDto: " + messageDetailDto);
            logger.trace("************************** End to get message details ************************************");
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
        logger.trace("************************** Start to get call details ************************************");
        logger.trace("API Voice, function: getDetailsCallById, callId:" + callBackModel.getCall_id());
        //String callId="7bc4efa5-c871-4482-a740-80c14e3a93e7";
        String callId = callBackModel.getCall_id();
        System.out.println("OK");
        CommonVoice commonVoice = new CommonVoice();
        try {
            String accessToken = commonVoice.getAPIVoiceToken();
            System.out.println("Call_id: " + callBackModel.getCall_id().toString());
            logger.trace("Call_id: " + callBackModel.getCall_id().toString());
            Map<String, String> infoSessionCall = commonVoice.getStatusByCallId(callId, accessToken);
            System.out.println("infoSessionCall 1: " + infoSessionCall);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.convertValue(infoSessionCall, JsonNode.class);
            System.out.println("json infoSessionCall: " + json);
            logger.trace("json infoSessionCall: " + json);
            logger.trace("************************** End to get Details Call By Id ************************************");
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.trace("************************** End to get Details Call By Id ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }

    // STOP
    @PostMapping(value = "message/stop")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Stop sending message",
            description = "Allows you to stop sending message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Allows you to stop sending message.",
                    content = @Content(schema = @Schema(implementation = MessageActionModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Stopped sending sms",
                    content = {@Content(mediaType = "application/json")})}
    )
    public ResponseEntity<?> stopCampaign(@RequestBody MessageActionModel messageActionModel) throws Exception {
        logger.trace("************************** Start to stop campaign ************************************");
        logger.trace("file: CampaignController.java, stopCampaign, userId:" + messageActionModel.getUser_id() + ", login: " + messageActionModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: STOP");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messageActionModel.getLogin(), messageActionModel.getPassword(), messageActionModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("messageId", "" + messageActionModel.getMessage_id());
            map.add("ACTION", "STOP");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: " + response.getBody());

            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.error("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to stop campaign ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else {
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    logger.trace("Unable to stop campaign");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to stop campaign", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("Campaign stopped Id: " + messageActionModel.getMessage_id());
                logger.trace("************************** End to stop message ************************************");
                return ResponseEntity.ok(new ApiDtoResponse(true, "message successfully stopped."));
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }
    // PAUSE
    @PostMapping(value = "message/pause")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Pause sending message",
            description = "Allows you to pause sending message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Allows you to pause sending message.",
                    content = @Content(schema = @Schema(implementation = MessageActionModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Sending sms paused",
                    content = {@Content(mediaType = "application/json")})}
    )
    public ResponseEntity<?> pauseCampaign(@RequestBody MessageActionModel messageActionModel) throws Exception {
        logger.trace("************************** Start to stop campaign ************************************");
        logger.trace("file: CampaignController.java, stopCampaign, userId:" + messageActionModel.getUser_id() + ", login: " + messageActionModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: PAUSE");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messageActionModel.getLogin(), messageActionModel.getPassword(), messageActionModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("messageId", "" + messageActionModel.getMessage_id());
            map.add("ACTION", "PAUSE");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: " + response.getBody());

            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to pause campaign ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else {
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    logger.trace("Unable to pause campaign");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to pause campaign", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("Campaign paused Id: " + messageActionModel.getMessage_id());
                logger.trace("************************** End to pause message ************************************");
                return ResponseEntity.ok(new ApiDtoResponse(true, "message successfully paused."));
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }

    // REMOVE
    @PostMapping(value = "message/remove")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Remove sending message",
            description = "Allows you to pause sending message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Allows you to remove sending message.",
                    content = @Content(schema = @Schema(implementation = MessageActionModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Sending sms removed",
                    content = {@Content(mediaType = "application/json")})}
    )
    public ResponseEntity<?> removeCampaign(@RequestBody MessageActionModel messageActionModel) throws Exception {
        logger.trace("************************** Start to remove campaign ************************************");
        logger.trace("file: CampaignController.java, removeCampaign, userId:" + messageActionModel.getUser_id() + ", login: " + messageActionModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: REMOVE");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messageActionModel.getLogin(), messageActionModel.getPassword(), messageActionModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("messageId", "" + messageActionModel.getMessage_id());
            map.add("ACTION", "REMOVE");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: " + response.getBody());

            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to remove campaign ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else {
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    logger.trace("Unable to remove campaign because your compaign status is not INIT or PLAN ");
                    logger.trace("************************** End to remove message ************************************");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to remove campaign ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("Campaign removed Id: " + messageActionModel.getMessage_id());
                logger.trace("************************** End to remove message ************************************");
                return ResponseEntity.ok(new ApiDtoResponse(true, "message successfully removed."));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.trace("************************** End to remove message ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }

    // PAUSE
    @PostMapping(value = "message/start")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Start sending message",
            description = "Allows you to start sending campaign",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Allows you to start sending campaign.",
                    content = @Content(schema = @Schema(implementation = MessageActionModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "start campaign",
                    content = {@Content(mediaType = "application/json")})}
    )
    public ResponseEntity<?> startCampaign(@RequestBody MessageActionModel messageActionModel) throws Exception {
        logger.trace("************************** Start to start campaign ************************************");
        logger.trace("file: CampaignController.java, startCampaign, userId:" + messageActionModel.getUser_id() + ", login: " + messageActionModel.getLogin() + "," +
                " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: START");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messageActionModel.getLogin(), messageActionModel.getPassword(), messageActionModel.getPartner_id());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("messageId", "" + messageActionModel.getMessage_id());
            map.add("ACTION", "START");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("body: " + response.getBody());

            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to start campaign ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else {
                int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if (rc != 0) {
                    logger.trace("Unable to start campaign");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to start campaign", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                logger.trace("Campaign paused Id: " + messageActionModel.getMessage_id());
                logger.trace("************************** End to start message ************************************");
                return ResponseEntity.ok(new ApiDtoResponse(true, "message successfully start."));
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }

    @GetMapping(value = "messages/stats", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Get Stats by period of messages",
            description = "DashBoard: get stats by period of messages",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DashBoard: get stats by period of messages.",
                    content = @Content(schema = @Schema(implementation = CustomerModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "DashBoard: get stats by period of messages.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessagesStatsDto.class))})}
    )

    //DashBoard
    public ResponseEntity<?> getStatsByPeriod(@RequestBody MessagesStatsModel messagesStatsModel) throws Exception {
        logger.trace("************************** Start to get stats by period ************************************");
        logger.trace("file: SimpleMessageController.java, getAllMessages, userId:" + messagesStatsModel.getUser_id() + ", login: " + messagesStatsModel.getLogin() + "," +
                "costumerId: " + messagesStatsModel.getCustomer_id() + " clientId: orangesn, urlBackend: /message/SimpleMessageController.php, ACTION: STAT_BY_PERIOD");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/SimpleMessageController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messagesStatsModel.getLogin(), messagesStatsModel.getPassword(), messagesStatsModel.getPartnerId());
            logger.trace("requestHeaders: " + requestHeaders);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("userId", "" + messagesStatsModel.getUser_id());
            map.add("customerId", "" + messagesStatsModel.getCustomer_id());
            map.add("messageId", "" + messagesStatsModel.getMessageId());
            map.add("application_id", "WS");
            map.add("ACTION", "STAT_BY_PERIOD");
            map.add("partnerCode", "290573");
            map.add("period", "" + messagesStatsModel.getPeriod());
            map.add("startDate", "" + messagesStatsModel.getStartDate());
            map.add("endDate", "" + messagesStatsModel.getEndDate());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("body before test: " + response.getBody());
            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.trace("Error while processing your request. Please contact your administrator.");
                logger.trace("************************** End to to get stats by period ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else { logger.trace("Body after test: " + response.getBody());
               // int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
                if(response.getBody()==null) {
                    logger.error("Impossible to get this campaign statistic");
                    return new ResponseEntity<>(new ApiDtoResponse(false, "Impossible to get this campaign statistic ", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
               // MessagesStatsDto[] messageDto = mapper.readValue(JsonStats.JSON_STATS_BY_PERIOD, MessagesStatsDto[].class);
                MessagesStatsDto[] messageDto = mapper.readValue(response.getBody().toString(), MessagesStatsDto[].class);
                logger.trace("************************** End to get stats by period by campaign ************************************");
                return ResponseEntity.ok(messageDto);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catch: Error while processing your request. Please contact your administrator.");
        }
    }


    /*@GetMapping(value = "messages/audio", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Get the list of audio messages",
            description = "Get the list of audio messages sent by the customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The list of audio messages.",
                    content = @Content(schema = @Schema(implementation = CustomerModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of audio messages",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageListDto.class))})}
    )

    public ResponseEntity<?> getAllAudioMessages(@RequestBody MessagesListModel messagesListModel) throws Exception {
        logger.trace("************************** Start to get all audio messages ************************************");
        logger.trace("file: SimpleMessageController.java, getAllAudioMessages, userId:" + messagesListModel.getUser_id() + ", login: " + messagesListModel.getLogin() + "," +
                "costumerId: " + messagesListModel.getCustomer_id() + " clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: LIST");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, messagesListModel.getLogin(), messagesListModel.getPassword(), messagesListModel.getPartnerId());
            //logger.trace("requestHeaders: " + requestHeaders);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("userId", "" + messagesListModel.getUser_id());
            map.add("customerId", "" + messagesListModel.getCustomer_id());
            map.add("application_id", "WS");
            map.add("ACTION", "LIST_DETAILS");
            map.add("partnerCode", "290573");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
            ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
            logger.trace("response: " + response.getBody());
            if (response == null || response.equals("") || response.getStatusCode().value() != 200) {
                logger.error("Error while processing your request. Please contact your administrator.");
                logger.error("************************** End to get all audio messages ************************************");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

            } else {

                //MessageListDto[] messageDto = mapper.readValue(response.getBody().toString(), MessageListDto[].class);
                System.out.println("json:" +JsonAudioMessage.JSON_AUDIO_MESSAGES);
                MessageListDto[] messageDto = mapper.readValue(JsonAudioMessage.JSON_AUDIO_MESSAGES, MessageListDto[].class);
                logger.trace("************************** End to get all audio messages ************************************");
                return ResponseEntity.ok(messageDto);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catch: Error while processing your request. Please contact your administrator.");
        }
    }*/


@PostMapping(value = "messages/voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = "application/json")
    @Operation(
            tags = {"Messages"},
            operationId = "Messages",
            summary = "Send voice campaign",
            description = "send voice campaign",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Send voice campaign.",
                    content = @Content(schema = @Schema(implementation = MessageVoiceModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Voice message successfully sent")}
    )

public ResponseEntity<?> sendVoiceCampaign(@ModelAttribute MessageVoiceModel messageVoiceModel) throws Exception {
    System.out.println("Diodio000");
    logger.trace("************************** Start to voice message ************************************");
    logger.trace("file: MessageController, function: sendVoiceCampaign, userId:" + messageVoiceModel.getUser_id() + ", customerId: " + messageVoiceModel.getCustomer_id() + "," +
            "customerId: " + messageVoiceModel.getCustomer_id() + ", clientId: orangesn, urlBackend: /message/CampaignController.php, ACTION: START," +
            "SignatureId:" + messageVoiceModel.getSignature());

    System.out.println("Diodio00");
    System.out.println("login: "+messageVoiceModel.getLogin()+" , passWord: "+ messageVoiceModel.getPassword() );
    try {
        System.out.println("Diodio0");
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/message/CampaignController.php";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = common.setUserCookies(pathsProperties, messageVoiceModel.getLogin(), messageVoiceModel.getPassword(), messageVoiceModel.getPartner_id());
        System.out.println("Diodio11");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        CommonVoice commonVoice = new CommonVoice();
        System.out.println("Diodio112");
        commonVoice.saveUploadedFile(pathsProperties, messageVoiceModel.getContent());
        System.out.println("Diodio113");
        File file = new File(pathsProperties.getPathValue("backend.files-directory") + messageVoiceModel.getContent().getOriginalFilename());
        System.out.println("Diodio3");

        map.add("userId", String.valueOf(messageVoiceModel.getUser_id()));
        map.add("partnerCode", String.valueOf(messageVoiceModel.getPartnerCode()));
        map.add("customerId", String.valueOf(messageVoiceModel.getCustomer_id()));
        map.add("groups", String.valueOf(messageVoiceModel.getGroups()));
        map.add("signatureName", String.valueOf(messageVoiceModel.getSignatureName()));
        map.add("groupName", String.valueOf(messageVoiceModel.getGroupName()));
        //map.add("numberMessage", String.valueOf(messageModel.getNumberMessage()));
        //map.add("messageLength", String.valueOf(messageModel.getMessageLength()));
        map.add("repeatedSMS", "0");
        map.add("numberOrdreEnvoi", "0");
        map.add("subject", String.valueOf(messageVoiceModel.getSubject()));
        map.add("signature", String.valueOf(messageVoiceModel.getSignature()));
        map.add("sourceType", "C");
        map.add("recipients", String.valueOf(messageVoiceModel.getRecipients()));
        map.add("groups", String.valueOf(messageVoiceModel.getGroups()));
        map.add("content", String.valueOf(messageVoiceModel.getContent().getOriginalFilename()));
        map.add("sendingType", String.valueOf(messageVoiceModel.getSendingType()));
        map.add("sendingDate", String.valueOf(messageVoiceModel.getSendingDate()));
        //map.add("hour", String.valueOf(messageVoiceModel.getHour()));
        //map.add("minute", String.valueOf(messageVoiceModel.getMinute()));
        map.add("sendingTime", String.valueOf(messageVoiceModel.getSendingTime()));
        // map.add("ACTION", "SEND");
        map.add("sendingType", "I");
        map.add("notSendBefore", "08:00");
        map.add("notSendAfter", "20:00");
        map.add("nbrLines", "");
        map.add("numberMessage", String.valueOf(messageVoiceModel.getNumberMessage()));
        map.add("messageLength", String.valueOf(messageVoiceModel.getMessageLength()));
        map.add("partnerCode", String.valueOf(messageVoiceModel.getPartnerCode()));
        map.add("appId", "WS");
        map.add("messageId", "0");
        map.add("ACTION", "START");
        map.add("TypeMSG", "VOICE");
        System.out.println("Diodio4");
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity<>(map, headers);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestHeader, String.class);
        System.out.println(response.getBody());
       // int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
       System.out.println("Diodio5");

        logger.trace("response: " + response.getBody());
       // if(response!=null || response.getBody()!=null){
            logger.trace("response body: "+response.getBody());
            int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
            if (rc != 0) {
                System.out.println("Diodio6");
                String error = (new JSONObject(response.getBody().toString())).getString("error");
                logger.error("Error: "+error);
                logger.error("************************** End to Create Signature ************************************");
                return new ResponseEntity<>(new ApiDtoResponse(false, error, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }

           else{
                System.out.println("Diodio7");
                return ResponseEntity.status(HttpStatus.OK).body("Voice message successfully sent.");
            }

       // return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to send voice message", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        //}
    }
    catch (Exception e) {
        logger.error(e.getMessage());
        logger.trace("************************** END SEND VOICE MESSSAGE ************************************");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
    }
}

}