package destiny.manager.destiny.BungieServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
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

    private static final Logger log = LoggerFactory.getLogger(GetCharacterInventory.class);

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

    public List<String> getCharacterInfo(List<String> characterIds) throws Exception {
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
    
        List<String> characterInfoResponses = new ArrayList<>();
        for (String characterId : characterIds) {
            String Uri = "https://bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() + "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterId + "/?components=200";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-Key", apiKey);
            headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
    
            ResponseEntity<String> response = restTemplate.exchange(Uri, HttpMethod.GET, entity, String.class);
            characterInfoResponses.add(response.getBody());
        }   
        return characterInfoResponses;
    }

    public JSONObject getCharacterData(List<String> characterIds) throws Exception {
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
    
        String Uri = "https://bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() + "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterIds + "/?components=200";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
    
        ResponseEntity<String> response = restTemplate.exchange(Uri, HttpMethod.GET, entity, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONObject responseJson = json.getJSONObject("Response");
        JSONObject characterDataJson = responseJson.getJSONObject("character");
        
        return characterDataJson;
    }

    private JSONObject getItemData(Long itemHash, String accessToken) throws Exception {
        String itemEndpoint = "https://www.bungie.net/Platform/Destiny2/Manifest/DestinyInventoryItemDefinition/" + itemHash;
    
        HttpHeaders itemHeaders = new HttpHeaders();
        itemHeaders.add("X-API-Key", apiKey);
        itemHeaders.add("Authorization", "Bearer " + accessToken);
    
        HttpEntity<String> itemEntity = new HttpEntity<>(itemHeaders);
    
        ResponseEntity<String> itemResponse = restTemplate.exchange(itemEndpoint, HttpMethod.GET, itemEntity, String.class);
        String itemDataResponse = itemResponse.getBody();
        System.out.println(itemDataResponse);
    
        JSONObject itemDataJson = new JSONObject(itemDataResponse);
        JSONObject displayProperties = itemDataJson.getJSONObject("Response").getJSONObject("displayProperties");

    
        String itemName = displayProperties.getString("name");
    
        String iconUrl = "";
        if (displayProperties.has("icon")) {
            iconUrl = displayProperties.getString("icon");
        } else {
        }
    
        JSONObject relevantData = new JSONObject();
        relevantData.put("name", itemName);
        if (!iconUrl.isEmpty()) {
            relevantData.put("icon", iconUrl);
        }
        System.out.println(relevantData);
        return relevantData;
    }

    public ResponseEntity<Map<String, List<JSONObject>>> getCharacterEquipment() throws Exception {
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    
        List<String> characterIds = getCharacterIds();
    
        Map<String, List<JSONObject>> characterInventories = new HashMap<>();
        for (String characterId : characterIds) {
            String endpoint = "https://www.bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() +
                    "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterId + "/?components=201";
    
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-Key", apiKey);
            headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
    
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
            String inventoryResponse = response.getBody();
    
            JSONObject inventoryJson = new JSONObject(inventoryResponse);
            JSONArray itemsArray = inventoryJson.getJSONObject("Response")
                    .getJSONObject("inventory")
                    .getJSONObject("data")
                    .getJSONArray("items");
    
            List<JSONObject> itemDataList = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemJson = itemsArray.getJSONObject(i);
                Long itemHash = itemJson.getLong("itemHash");
                JSONObject itemData = getItemData(itemHash, accessTokenResponse.getAccessToken());
                itemDataList.add(itemData);
            }
    
            characterInventories.put(characterId, itemDataList);
        }
        return ResponseEntity.ok(characterInventories);
    }

    private String getBucketData(Long bucketHash, String accessToken) throws Exception {
        String bucketEndpoint = "https://www.bungie.net/Platform/Destiny2/Manifest/DestinyInventoryBucketDefinition/" + bucketHash;
    
        HttpHeaders bucketHeaders = new HttpHeaders();
        bucketHeaders.add("X-API-Key", apiKey);
        bucketHeaders.add("Authorization", "Bearer " + accessToken);
    
        HttpEntity<String> bucketEntity = new HttpEntity<>(bucketHeaders);
    
        ResponseEntity<String> bucketResponse = restTemplate.exchange(bucketEndpoint, HttpMethod.GET, bucketEntity, String.class);
        String bucketDataResponse = bucketResponse.getBody();
    
        JSONObject bucketDataJson = new JSONObject(bucketDataResponse);
    
        JSONObject displayPropertiesJson = bucketDataJson.getJSONObject("Response")
                    .getJSONObject("displayProperties");
    
        String bucketData = "";
    
        if (displayPropertiesJson.has("name")) {
            bucketData = displayPropertiesJson.getString("name");
        } else {
            bucketData = "Name not found";
        }
    
        return bucketData;
    }

    private CompletableFuture<JSONObject> getItemDataAsync(Long itemHash, String accessToken) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getItemData(itemHash, accessToken);
            } catch (ResourceAccessException e) {
                log.error("Failed to fetch item data for itemHash: " + itemHash, e);
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Failed to fetch item data");
                return errorJson;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    private CompletableFuture<String> getBucketDataAsync(Long bucketHash, String accessToken) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            return getBucketData(bucketHash, accessToken);
        } catch (ResourceAccessException e) {
            log.error("Failed to fetch bucket data for bucketHash: " + bucketHash, e);
            return "Unknown";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });
}

    public ResponseEntity<Map<String, List<JSONObject>>> getUnequippedItemsForCharacter() throws Exception {
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    
        List<String> characterIds = getCharacterIds();
    
        Map<String, List<JSONObject>> characterInventories = new HashMap<>();
        for (String characterId : characterIds) {
            String endpoint = "https://www.bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() +
                    "/Profile/" + bungieUserInfo.getMembershipId() + "/Character/" + characterId + "/?components=201";
    
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-Key", apiKey);
            headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
    
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
            String inventoryResponse = response.getBody();
    
            JSONObject inventoryJson = new JSONObject(inventoryResponse);
            
            JSONArray itemsArray = inventoryJson.getJSONObject("Response")
                    .getJSONObject("inventory")
                    .getJSONObject("data")
                    .getJSONArray("items");
    
                    List<CompletableFuture<JSONObject>> itemDataFutures = new ArrayList<>();
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemJson = itemsArray.getJSONObject(i);
                        Long itemHash = itemJson.getLong("itemHash");
                        Long bucketHash = Integer.toUnsignedLong(itemJson.getInt("bucketHash"));
            
                        CompletableFuture<JSONObject> itemDataFuture = getItemDataAsync(itemHash, accessTokenResponse.getAccessToken());
                        CompletableFuture<String> bucketDataFuture = getBucketDataAsync(bucketHash, accessTokenResponse.getAccessToken());
            
                        CompletableFuture<JSONObject> itemDataWithBucketName = itemDataFuture.thenCombine(bucketDataFuture, (itemData, bucketData) -> {
                            itemData.put("bucketName", bucketData);
                            return itemData;
                        });
            
                        itemDataFutures.add(itemDataWithBucketName);
                    }
            
                    CompletableFuture.allOf(itemDataFutures.toArray(new CompletableFuture[0])).join();
                    List<JSONObject> itemDataList = itemDataFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            
            characterInventories.put(characterId, itemDataList);
        }
        return ResponseEntity.ok(characterInventories);
    }

    public ResponseEntity<Map<String, List<JSONObject>>> getCharacterInventory() throws Exception {
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    
        List<String> characterIds = getCharacterIds();
    
        Map<String, List<JSONObject>> characterInventories = new HashMap<>();
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
    
                    List<CompletableFuture<JSONObject>> itemDataFutures = new ArrayList<>();
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemJson = itemsArray.getJSONObject(i);
                        Long itemHash = itemJson.getLong("itemHash");
                        Long bucketHash = Integer.toUnsignedLong(itemJson.getInt("bucketHash"));
            
                        CompletableFuture<JSONObject> itemDataFuture = getItemDataAsync(itemHash, accessTokenResponse.getAccessToken());
                        CompletableFuture<String> bucketDataFuture = getBucketDataAsync(bucketHash, accessTokenResponse.getAccessToken());
            
                        CompletableFuture<JSONObject> itemDataWithBucketName = itemDataFuture.thenCombine(bucketDataFuture, (itemData, bucketData) -> {
                            itemData.put("itemHash", itemHash);
                            itemData.put("itemInstanceId", 
                            itemJson.getLong("itemInstanceId")); // Make sure you're getting the correct itemInstanceId from the API response
                            itemData.put("bucketName", bucketData);
                            System.out.println(itemData);
                            return itemData;                         
                        });
            
                        itemDataFutures.add(itemDataWithBucketName);
                    }
            
                    CompletableFuture.allOf(itemDataFutures.toArray(new CompletableFuture[0])).join();
                    List<JSONObject> itemDataList = itemDataFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            
            characterInventories.put(characterId, itemDataList);
        }
        return ResponseEntity.ok(characterInventories);
    }

    public ResponseEntity<Map<String, List<JSONObject>>> getMemberVault() throws Exception {
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    
        Map<String, List<JSONObject>> characterInventories = new HashMap<>();
        String endpoint = "https://www.bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() +
                "/Profile/" + bungieUserInfo.getMembershipId() + "/?components=102";
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
        String inventoryResponse = response.getBody();
    
        JSONObject inventoryJson = new JSONObject(inventoryResponse);
    
        JSONArray itemsArray = inventoryJson.getJSONObject("Response")
                .getJSONObject("profileInventory")
                .getJSONObject("data")
                .getJSONArray("items");
    
        List<CompletableFuture<JSONObject>> itemDataFutures = new ArrayList<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemJson = itemsArray.getJSONObject(i);
            Long itemHash = itemJson.getLong("itemHash");
    
            CompletableFuture<JSONObject> itemDataFuture = vaultItemAsync(itemHash, accessTokenResponse.getAccessToken());
            itemDataFutures.add(itemDataFuture);
        }
    
        CompletableFuture.allOf(itemDataFutures.toArray(new CompletableFuture[0])).join();
        List<JSONObject> itemDataList = itemDataFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        List<JSONObject> filteredItemDataList = itemDataList.stream()
                                .filter(item -> item.has("name"))
                                .collect(Collectors.toList());

        characterInventories.put(bungieUserInfo.getMembershipId(), filteredItemDataList);
    
        // characterInventories.put(bungieUserInfo.getMembershipId(), itemDataList);
    
        return ResponseEntity.ok(characterInventories);
    }

    private JSONObject getVaultItemData(Long itemHash, String accessToken) throws Exception {
        String itemEndpoint = "https://www.bungie.net/Platform/Destiny2/Manifest/DestinyInventoryItemDefinition/" + itemHash;

        HttpHeaders itemHeaders = new HttpHeaders();
        itemHeaders.add("X-API-Key", apiKey);
        itemHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> itemEntity = new HttpEntity<>(itemHeaders);

        ResponseEntity<String> itemResponse = restTemplate.exchange(itemEndpoint, HttpMethod.GET, itemEntity, String.class);
        String itemDataResponse = itemResponse.getBody();

        JSONObject itemDataJson = new JSONObject(itemDataResponse);
        JSONObject displayProperties = itemDataJson.getJSONObject("Response").getJSONObject("displayProperties");

        String itemName = displayProperties.getString("name");

        String iconUrl = "";
        if (displayProperties.has("icon")) {
            iconUrl = displayProperties.getString("icon");
        } else {
        }

        // Get itemTypeDisplayName from the response
        String itemTypeDisplayName = itemDataJson.getJSONObject("Response").getString("itemTypeDisplayName");

        JSONObject relevantData = new JSONObject();
        relevantData.put("name", itemName);
        if (!iconUrl.isEmpty()) {
            relevantData.put("icon", iconUrl);
        }
        // Add itemTypeDisplayName to the relevantData
        relevantData.put("itemTypeDisplayName", itemTypeDisplayName);
        System.out.println(relevantData);
        return relevantData;
    }

    private CompletableFuture<JSONObject> vaultItemAsync(Long itemHash, String accessToken) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getVaultItemData(itemHash, accessToken);
            } catch (ResourceAccessException e) {
                log.error("Failed to fetch item data for itemHash: " + itemHash, e);
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Failed to fetch item data");
                return errorJson;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // public boolean transferItem(String itemHash, String itemInstanceId, String selectedCharacterId, String sendToCharacterId) {
    //     AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    //     System.out.println(accessTokenResponse);
    //     // Bungie API endpoint for item transfer
    //     String transferUrl = "https://www.bungie.net/Platform/Destiny2/Actions/Items/TransferItem/";
    
    //     // Prepare the request body
    //     String requestBody = "{ \"itemReferenceHash\": " + itemHash + ", \"stackSize\": 1, \"transferToVault\": false, \"itemId\": " + itemInstanceId + ", \"characterId\": " + selectedCharacterId + ", \"membershipType\": 2, \"itemInstanceId\": " + itemInstanceId + ", \"characterIdToTransferTo\": " + sendToCharacterId + " }";
    
    //     // Set the request headers
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     headers.set("X-API-Key", apiKey);
    //     headers.set("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
    //     // Create the request entity
    //     HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    
    //     // Make the API call
    //     RestTemplate restTemplate = new RestTemplate();
    //     ResponseEntity<String> responseEntity = restTemplate.exchange(transferUrl, HttpMethod.POST, requestEntity, String.class);
    
    //     // Check the response status code
    //     if (responseEntity.getStatusCode().is2xxSuccessful()) {
    //       // Item transfer was successful
    //       return true;
    //     } else {
    //       // Item transfer failed
    //       return false;
    //     }
    //   }

    public boolean transferItem(String itemReferenceHash, String itemInstanceId, String selectedCharacterId, String sendToCharacterId) {
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();
        System.out.println(accessTokenResponse);
    
        // Bungie API endpoint for item transfer
        String transferUrl = "https://www.bungie.net/Platform/Destiny2/Actions/Items/TransferItem/";
    
        // Prepare the request body as a JSON object
        JSONObject requestBody = new JSONObject();
        requestBody.put("itemReferenceHash", itemReferenceHash);
        requestBody.put("itemId", 418462209);
        requestBody.put("characterId", selectedCharacterId);
        requestBody.put("membershipType", bungieUserInfo.getMembershipType());
        requestBody.put("transferToVault", false);
        requestBody.put("stackSize", 1);
        requestBody.put("transferToVault", false);
        requestBody.put("itemInstanceId", itemInstanceId);
        requestBody.put("characterIdToTransferTo", sendToCharacterId);
        requestBody.put("transferToVault", false);
        

        
    
        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", apiKey);
        headers.set("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
    
        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
    
        // Make the API call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(transferUrl, HttpMethod.POST, requestEntity, String.class);
    
        // Check the response status code
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Item transfer was successful
            return true;
        } else {
            // Item transfer failed
            return false;
        }
    }
 }