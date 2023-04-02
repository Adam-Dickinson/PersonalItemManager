package destiny.manager.destiny.BungieServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import destiny.manager.destiny.Repositorys.AuthTokenRepository;
import destiny.manager.destiny.Repositorys.BungieUserRepository;
import destiny.manager.destiny.Response.AccessTokenResponse;
import destiny.manager.destiny.Response.BungieUserInfo;

@Service
public class GetCharacterInventory {

    @Value("${bungie.apiKey}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate; 

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private BungieUserRepository bungieUserRepository;

    public List<String> getCharacterIds() throws Exception {
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();

        String Uri = "https://bungie.net//Platform/Destiny2/" + bungieUserInfo.getMembershipType() + "/Profile/" + bungieUserInfo.getMembershipId() + "/?components=100";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(Uri, HttpMethod.GET, entity, String.class);
        System.out.println(response);
        JSONObject json = new JSONObject(response.getBody());
        JSONObject responseJson = json.getJSONObject("Response");
        
        JSONArray characterIdsJson = responseJson.getJSONObject("profile")
                .getJSONObject("data")
                .getJSONArray("characterIds");
        List<String> characterIds = new ArrayList<>();
        for (int i = 0; i < characterIdsJson.length(); i++) {
            characterIds.add(characterIdsJson.getString(i));
        }  
        return characterIds;
    } 

    public List<JSONObject> getCharacters() throws Exception {
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
    
        List<String> characterIds = getCharacterIds();
        List<JSONObject> characters = new ArrayList<>();
    
        for (String characterId : characterIds) {
            String Uri = "https://bungie.net//Platform/Destiny2/" + bungieUserInfo.getMembershipType() + "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterId + "/?components=200";
    
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-Key", apiKey);
            headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
    
            ResponseEntity<String> response = restTemplate.exchange(Uri, HttpMethod.GET, entity, String.class);
            System.out.println(response);
            JSONObject json = new JSONObject(response.getBody());
            JSONObject responseJson = json.getJSONObject("Response");
    
            characters.add(responseJson);
        }   
        return characters;
    }

    public ResponseEntity<Map<String, List<Long>>> getCharacterInventory() throws Exception {
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    
        List<String> characterIds = getCharacterIds();
    
        Map<String, List<Long>> characterInventories = new HashMap<>();
        for (String characterId : characterIds) {
            String endpoint = "https://www.bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() +
                    "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterId + "/?components=205";
    
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-Key", apiKey);
            headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
    
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
            String inventoryResponse = response.getBody();
            
            JSONObject inventoryJson = new JSONObject(inventoryResponse);
            JSONArray itemsArray = inventoryJson.getJSONObject("Response")
                                               .getJSONObject("equipment")
                                               .getJSONObject("data")
                                               .getJSONArray("items");
                                                         
            List<Long> itemHashes = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemJson = itemsArray.getJSONObject(i);
                Long itemHash = itemJson.getLong("itemHash");
                itemHashes.add(itemHash);
            }
            
            characterInventories.put(characterId, itemHashes);
        } 
        return ResponseEntity.ok(characterInventories);
    }
 }
