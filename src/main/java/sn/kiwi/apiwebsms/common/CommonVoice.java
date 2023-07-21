package sn.kiwi.apiwebsms.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sn.kiwi.apiwebsms.config.PathsProperties;
import sn.kiwi.apiwebsms.models.UserModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CommonVoice {
    String ch;

    //String []data;
    public String getAPIVoiceToken() throws Exception {
        String authHeader = "Basic c3FMc0pHcmxWSUhqNWduejB1YldmREZvVkl6QUI5R3M6SlF0bmdBVjZQUTI3T2l0TA==";
        String url = "https://api.orange.com/oauth/v3/token";
        String contentType = "application/x-www-form-urlencoded";
        String grantType = "client_credentials";
        // data = ["grant_type'=>'client_credentials"];
        HttpHeaders requestHeaders = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        requestHeaders.add("Authorization", "" + authHeader);
        requestHeaders.add("Content-Type", "" + contentType);
        requestHeaders.add("Accept", "application/json");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "" + grantType);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String tokenType = (new JSONObject(response.getBody().toString())).getString("token_type");
        String accessToken = (new JSONObject(response.getBody().toString())).getString("access_token");
        System.out.println(accessToken);
        return accessToken;
    }

    public String uploadFile(String accessToken, String mediaName, File mediaFile) throws Exception {

        System.out.println("step 2 " + accessToken);
        String authHeader = "Bearer " + accessToken + "";
        System.out.println("auth " + authHeader);
        String url = "https://api.orange.com/vaas/vaas-osn/api/v1/contexts/ContextTestAPIvoix/medias/" + mediaName;
        String contentType = "multipart/form-data";
        String expect = "";
        HttpHeaders requestHeaders = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        //requestHeaders.add("Authorization", "Bearer ");
        requestHeaders.add("Authorization", "" + authHeader);
        requestHeaders.add("Content-Type", "multipart/form-data");
        requestHeaders.add("Expect", "");
        System.out.println("requestHeaders " + requestHeaders);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("file", new FileSystemResource(mediaFile));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(formData, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        if (response.getStatusCode() == HttpStatus.CREATED)
            return mediaName;
        return null;
    }


    public String sendVoice(String accessToken, String mediaUri, String numDest) throws Exception {
        String authHeader = "Bearer " + accessToken + "";
        //numDest="00221775130346";
        //String url="https://api.orange.com/vaas/vaas-osn/api/v1/contexts/ContextTestAPIkiwi/callSessions/createOneParticipantCall";
        String url = "https://api.orange.com/vaas/vaas-osn/api/v1/contexts/ContextTestAPIvoix/callSessions/createOneParticipantCall";

        String contentType = "application/json";
        String jsonCall = "{\n" +
                "            \"maxCallDuration\": 360,\n" +
                "            \"notificationCallbackUrl\": \"https://www.ksmsplus.net/logApiVoix.php\",\n" +
                "            \"participant\": {\n" +
                "                \"uri\": \"tel:" + numDest + "\",\n" +
                "                \"mediaUri\": \"" + mediaUri + "\",\n" +
                "                \"displayedFromUri\": \"Kiwi tel:786210172\",\n" +
                "                \"ringingTimeout\": \"60\"\n" +
                "            }\n" +
                "        }\n";
        //String directoryFile="/C:/Users/User/Downloads/kiwi.wav";
        HttpHeaders requestHeaders = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        requestHeaders.add("Authorization", "" + authHeader);
        requestHeaders.add("Content-Type", "" + contentType);
        requestHeaders.add("Accept", "application/json");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<String> request = new HttpEntity<String>(jsonCall, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String callId = (new JSONObject(response.getBody().toString())).getString("callId");
        System.out.println("callId " + callId);
        return callId;
    }

    public Map<String, String> getStatusByCallId(String callId, String accessToken) throws Exception {
        String authHeader = "Bearer " + accessToken; //Ne pas oublier, A automatiser

        String url = "https://api.orange.com/vaas/vaas-osn/api/v1/contexts/ContextTestAPIvoix/callSessions/" + callId + "";
        System.out.println(url);
        String contentType = "application/json";
        //String expect= "application/json";
        HttpHeaders requestHeaders = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        requestHeaders.add("Authorization", "" + authHeader);
        requestHeaders.add("Content-Type", "" + contentType);
        //requestHeaders.add("Expect",""+expect );
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, requestHeaders);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        JSONArray participants = (new JSONObject(response.getBody().toString())).getJSONArray("participants");
        System.out.println("participants: "+participants);
        Map<String, String> infoSessionCall = new HashMap<>();
        participants.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            System.out.println("item: "+ item);
            infoSessionCall.put("duration", String.valueOf(obj.getInt("durationInSeconds")));
            infoSessionCall.put("creationDate", String.valueOf(obj.getString("creationDate")));
            infoSessionCall.put("participantId", String.valueOf(obj.getString("participantId")));
            infoSessionCall.put("terminationDate", String.valueOf(obj.get("terminationDate")));
            infoSessionCall.put("terminationCause", String.valueOf(obj.getString("terminationCause")));
            infoSessionCall.put("status", String.valueOf(obj.getString("status")));
            // Ajouter d'autres infos au besoin
        });
        return infoSessionCall;
    }

    public void saveUploadedFile(PathsProperties pathsProperties, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(pathsProperties.getPathValue("backend.files-directory") + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }
}
