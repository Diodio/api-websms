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
import sn.kiwi.apiwebsms.dtos.GroupListDto;
import sn.kiwi.apiwebsms.dtos.MessageDetailDto;
import sn.kiwi.apiwebsms.models.GroupAddModel;
import sn.kiwi.apiwebsms.models.GroupDeleteModel;
import sn.kiwi.apiwebsms.models.GroupListModel;

@RestController
public class GroupController {

    Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private PathsProperties pathsProperties;
    Common common=new Common();


    @GetMapping(value = "groups")
    @Operation(
            tags = {"Groups"},
            operationId = "Groups",
            summary = "List groups",
            description = "Show the list of groups",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List groups.",
                    content = @Content(schema = @Schema(implementation = GroupListModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Found the list of groups",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupListDto.class))})}
    )

    public ResponseEntity<?> getAllGroups(@RequestBody GroupListModel groupListModel) throws Exception {
        logger.trace("************************** Start to get all groups ************************************");
        logger.trace("file: GroupController.java, getAllGroups, userId:"+groupListModel.getUser_id()+", login: "+groupListModel.getLogin()+"," +
                "costumerId: "+groupListModel.getCustomer_id()+" clientId: orangesn, urlBackend: /group/GroupController.php, ACTION: LIST");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/group/GroupController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, groupListModel.getLogin(), groupListModel.getPassword(), groupListModel.getPartnerId());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+ groupListModel.getUser_id());
        map.add("customerId", ""+ groupListModel.getCustomer_id());
        map.add("sharing", "0");
        map.add("ACTION", "LIST");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        logger.trace("response: "+response);

        if(response==null || response.equals("") || response.getStatusCode().value()!=200) {
            logger.trace("Error while processing your request. Please contact your administrator.");
            logger.trace("************************** End to get all groups ************************************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while processing your request. Please contact your administrator.");

        }else {
            GroupListDto[] groups = mapper.readValue(response.getBody().toString(), GroupListDto[].class);
            logger.trace("list Groups: "+ response);
            logger.trace("groups: "+groups);
            logger.trace("************************** End to get all groups ************************************");
            return ResponseEntity.ok(groups);
        }

    }

    @PostMapping(value = "groups")
    @Operation(
            tags = {"Groups"},
            operationId = "Groups",
            summary = "Create a new group",
            description = "Create a new group for a user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new group.",
                    content = @Content(schema = @Schema(implementation = GroupAddModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Group successfully created")}
    )
    public ResponseEntity<?> createGroup(@RequestBody GroupAddModel groupAddModel) throws Exception {
        logger.trace("************************** Start to create group ************************************");
        logger.trace("file: GroupController.java, createGroup, userId:"+groupAddModel.getUser_id()+", login: "+groupAddModel.getLogin()+"," +
                "costumerId: "+groupAddModel.getPartnerId()+" clientId: orangesn, urlBackend: /group/GroupController.php, ACTION: INSERT");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/group/GroupController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, groupAddModel.getLogin(), groupAddModel.getPassword(), groupAddModel.getPartnerId());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+groupAddModel.getUser_id());
        map.add("groupName", ""+groupAddModel.getName());
        map.add("description", ""+groupAddModel.getDescription());
        map.add("ACTION", "INSERT");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        logger.trace("response: "+response.getBody());
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("Group already exists");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Group already exists", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
            logger.trace("New group created: Id: "+groupAddModel.getUser_id()+" groupName: "+groupAddModel.getName());
            return ResponseEntity.ok(new ApiDtoResponse(true, "A new group is successfully created."));
    }

    @PutMapping(value = "groups")
    @Operation(
            tags = {"Groups"},
            operationId = "Groups",
            summary = "Update a group",
            description = "Update a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update a group.",
                    content = @Content(schema = @Schema(implementation = GroupAddModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Group successfully updated")}
    )
    public ResponseEntity<?> updateGroup(@RequestBody GroupAddModel groupAddModel) throws Exception {
        logger.trace("************************** Start to update group ************************************");
        logger.trace("file: GroupController.java, updateGroup, userId:"+groupAddModel.getUser_id()+", login: "+groupAddModel.getLogin()+"," +
                "costumerId: "+groupAddModel.getPartnerId()+" clientId: orangesn, urlBackend: /group/GroupController.php, ACTION: UPDATE");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/group/GroupController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, groupAddModel.getLogin(), groupAddModel.getPassword(), groupAddModel.getPartnerId());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+groupAddModel.getUser_id());
        map.add("groupName", ""+groupAddModel.getName());
        map.add("description", ""+groupAddModel.getDescription());
        map.add("groupId", ""+groupAddModel.getId());
        map.add("ACTION", "UPDATE");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("response: "+response.getBody());
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("************************** Group already exists ************************************");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Group already exists", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
            logger.trace("Group, groupeId: "+groupAddModel.getId()+" was successfully updated");
            logger.trace("************************** Start to update group ************************************");
        return ResponseEntity.ok(new ApiDtoResponse(true, "Group is successfully updated."));

    }

    @DeleteMapping(value = "groups")
    @Operation(
            tags = {"Groups"},
            operationId = "Groups",
            summary = "Delete a group",
            description = "Delete a group",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Delete a group.",
                    content = @Content(schema = @Schema(implementation = GroupDeleteModel.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Group successfully deleted")}
    )
    public ResponseEntity<?> deleteGroup(@RequestBody GroupDeleteModel groupDeleteModel) throws Exception {
        logger.trace("************************** Start to delete group ************************************");
        logger.trace("file: GroupController.java, deleteGroup, userId:"+groupDeleteModel.getUser_id()+", login: "+groupDeleteModel.getLogin()+"," +
                "costumerId: "+groupDeleteModel.getPartnerId()+" clientId: orangesn, urlBackend: /group/GroupController.php, ACTION: UPDATE");
        ObjectMapper mapper = new ObjectMapper();
        String backendUrl = pathsProperties.getPathValue("backend.url") + "/group/GroupController.php";
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders requestHeaders = common.setUserCookies(pathsProperties, groupDeleteModel.getLogin(), groupDeleteModel.getPassword(), groupDeleteModel.getPartnerId());
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("userId", ""+groupDeleteModel.getUser_id());
        map.add("groupId", ""+groupDeleteModel.getGroup_id());
        map.add("ACTION", "REMOVE");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(backendUrl, HttpMethod.POST, request, String.class);
        System.out.println("response: "+response.getBody());
        int rc = (new JSONObject(response.getBody().toString())).getInt("rc");
        if (rc != 0) {
            logger.trace("Result: Unable to delete this group");
            return new ResponseEntity<>(new ApiDtoResponse(false, "Unable to delete this group", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        logger.trace("Group, groupId: "+groupDeleteModel.getGroup_id()+" was successfully deleted");
        logger.trace("************************** Start to delete group ************************************");
        return ResponseEntity.ok(new ApiDtoResponse(false, "Group is successfully deleted."));
    }
}
