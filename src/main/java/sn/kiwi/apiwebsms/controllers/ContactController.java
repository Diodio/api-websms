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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sn.kiwi.apiwebsms.common.Common;
import sn.kiwi.apiwebsms.common.CommonVoice;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.dtos.ApiDtoResponse;
import sn.kiwi.apiwebsms.dtos.ContactForListDto;
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.MessageListDto;
import sn.kiwi.apiwebsms.models.*;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class ContactController {
    Logger logger = LoggerFactory.getLogger(ContactController.class);
    Common common = new Common();
    CommonVoice commonVoice = new CommonVoice();

    @Autowired
    private PathsProperties pathsProperties;

    //Creer un contact dans un groupe
    @PostMapping(value = "contacts", produces = "application/json")
    @Operation(
            tags = {"Contacts"},
            operationId = "Contacts",
            summary = "Create a new contact in a group",
            description = "Create a new contact in a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new contact.",
                    content = @Content(schema = @Schema(implementation = ContactAddModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Contact successfully created")}
    )

    public ResponseEntity<?> createContact(@RequestBody ContactAddModel contactAddModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+contactAddModel.getUser_id()+", customerId: "+contactAddModel.getCustomer_id()+"," +"Cellular: "+contactAddModel.getCellular()+
                ", customerId: "+contactAddModel.getCustomer_id()+ ", partnarId: "+contactAddModel.getPartnerId()+", login: "+contactAddModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: /contact/ContactController.php, ACTION: INSERT" );
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/contact/ContactController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, contactAddModel.getLogin(), contactAddModel.getPassword(), contactAddModel.getPartnerId());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "" + contactAddModel.getUser_id());
        map.add("groupId", "" + contactAddModel.getGroup_id());
        map.add("contactId", "-1");
        map.add("indicatif", "221");
        map.add("addChamp", "");
        map.add("firstName", "" + contactAddModel.getFirstName());
        map.add("lastName", "" + contactAddModel.getLastName());
        map.add("cellular", "" + contactAddModel.getCellular());
        map.add("email", "" + contactAddModel.getEmail());
        map.add("BDAY_NAME", "");
        map.add("ACTION", "INSERT");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("response: " + response.getBody());
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("Contact already exists");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Contact already exists", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        logger.trace("A new contact was successfully created.");
        return ResponseEntity.ok(new ApiDtoResponse(true, "A new contact was successfully created."));
    }

    //Creer une liste de  contacts dans un groupe
    @PostMapping(value = "contacts/addList", produces = "application/json")
    @Operation(
            tags = {"Contacts"},
            operationId = "Contacts",
            summary = "Create a list of contacts in a group",
            description = "Create a list of contacts in a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a list of contacts.",
                    content = @Content(schema = @Schema(implementation = ContactAddListModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Contact successfully created")}
    )

    public ResponseEntity<?> createListContact(@RequestBody ContactAddListModel contactAddListModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+contactAddListModel.getUser_id()+", customerId: "+contactAddListModel.getCustomer_id()+"," +
                ", customerId: "+contactAddListModel.getCustomer_id()+ ", partnarId: "+contactAddListModel.getPartnerId()+", login: "+contactAddListModel.getLogin()+"," +
                " clientId: orangesn, urlBackend: /contact/ContactController.php, ACTION: INSERT_LIST_CONTACTS" );
        try{
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/contact/ContactController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, contactAddListModel.getLogin(), contactAddListModel.getPassword(), contactAddListModel.getPartnerId());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        JSONArray JSONArray = new JSONArray(contactAddListModel.getContacts());

        map.add("userId", "" + contactAddListModel.getUser_id());
        map.add("groupId", "" + contactAddListModel.getGroup_id());
        map.add("customerId", "" + contactAddListModel.getCustomer_id());
        map.add("contacts", ""+ JSONArray);
        map.add("ACTION", "INSERT_LIST_CONTACTS");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("response: " + response.getBody());
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("Contacts not inserted");
            return new ResponseEntity<>(new ApiDtoResponse(false, "contacts not inserted", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        logger.trace("Contacts successfully created.");
        return ResponseEntity.ok(new ApiDtoResponse(true, "contacts successfully created."));

        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }
    }


    //Creer une liste de  contacts dans un groupe
    @GetMapping(value = "contacts", produces = "application/json")
    @Operation(
            tags = {"Contacts"},
            operationId = "Contacts",
            summary = "Display the contacts list in a group",
            description = "Display the contacts list in a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Display the contacts list in a group.",
                    content = @Content(schema = @Schema(implementation = ContactAddListModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "list displayed")}
    )
    public ResponseEntity<?> getAllContacts(@RequestBody ContactForListModel contactForListModel) throws Exception {
        logger.trace("************************** Start to get all groups ************************************");
        logger.trace("file: ContactController.java, getAllContact, userId:"+contactForListModel.getUser_id()+", login: "+contactForListModel.getLogin()+"," +
                "costumerId: "+contactForListModel.getCustomer_id()+" clientId: orangesn, urlBackend: /contact/ContactController.php, ACTION: LIST");
        try {
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/contact/ContactController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, contactForListModel.getLogin(), contactForListModel.getPassword(), contactForListModel.getPartnerId());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ contactForListModel.getUser_id());
        map.add("customerId", ""+ contactForListModel.getCustomer_id());
        map.add("groupId", ""+ contactForListModel.getGroup_id());
        map.add("sharing", "0");
        map.add("iDisplayStart", "0");
        map.add("iDisplayLength", "10");
        map.add("ACTION", "LIST");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        logger.trace("response: "+response);
        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.trace("Error while processing your request. Please contact your administrator.");
            logger.trace("************************** End to get all groups ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

        }else {
            JSONArray aoData = (new JSONObject(response.getBody().toString())).getJSONArray("aaData");
            logger.trace("response aoData: "+aoData);
            JSONArray allContacts = new JSONArray();
            aoData.forEach(item -> {
                JSONObject contactJson = new JSONObject();
                JSONArray it = (JSONArray) item;
                contactJson.put("id", (String) it.get(0));
                contactJson.put("firstName", (String) it.get(1));
                contactJson.put("lastName", (String) it.get(2));
                contactJson.put("cellular", (String) it.get(3));
                contactJson.put("email", (String) it.get(4));
                allContacts.put(contactJson);
            });
            ContactForListDto[] contacts = mapper.readValue(allContacts.toString(), ContactForListDto[].class);
            logger.trace("************************** End to get all contacts ************************************");
            return ResponseEntity.ok(contacts);
        }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");
        }

    }


    // import de contact
    @PostMapping(value = "contacts/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            tags = {"Contacts"},
            operationId = "Contacts",
            summary = "import of contacts",
            description = "Import of contacts into a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Import of contacts.",
                    content = @Content(schema = @Schema(implementation = ImportModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Contacts successfully imported.")}
    )

    public ResponseEntity<?> importContact(@ModelAttribute ImportModel importModel) throws Exception {
        logger.trace("************************** Start to send message voice ************************************");
        logger.trace("file: MessageController, function: sendVoice, userId:"+importModel.getUser_id()+", customerId: ," +"login: "+importModel.getLogin()+
                ", partnarId: "+importModel.getPartnerId()+", partnarId: "+importModel.getPartnerId()+"," +
                " clientId: orangesn, urlBackend: /contact/ContactController.php, ACTION: IMPORT," );
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/contact/ContactController.php";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, importModel.getLogin(), importModel.getPassword(), importModel.getPartnerId());
        byte[] bytes = importModel.getFile().getBytes();
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("contactfile")
                .filename(importModel.getFile().getOriginalFilename())
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, fileMap);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);
        body.add("userId", "" + importModel.getUser_id());
        body.add("groupId", "" + importModel.getGroup_id());
        body.add("champs", "" + importModel.getFields());
        body.add("nbrLines", "" + importModel.getNbrLines());
        body.add("aEntete", "" + importModel.isHeader());
        body.add("groups", "");
        body.add("separateur", "" + importModel.getSeparateur());
        body.add("delimit", "n");
        body.add("ACTION", "IMPORT");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(backendUrl, HttpMethod.POST, requestEntity, String.class);

        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("Unable to import contacts");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to import contacts", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        logger.trace("Contacts successfully imported.");
        return ResponseEntity.ok(new ApiDtoResponse(true, "Contacts successfully imported."));

    }
}