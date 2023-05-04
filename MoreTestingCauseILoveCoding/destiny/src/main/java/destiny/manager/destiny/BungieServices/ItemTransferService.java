package destiny.manager.destiny.BungieServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import destiny.manager.destiny.Repositorys.AuthTokenRepository;
import destiny.manager.destiny.Response.AccessTokenResponse;

@Service
public class ItemTransferService {

    @Value("${bungie.apiKey}")
    private String apiKey;

    @Autowired
    private AuthTokenRepository authTokenRepository;

  public boolean transferItem(String itemHash, String itemInstanceId, String selectedCharacterId, String sendToCharacterId) {
    AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
    System.out.println(accessTokenResponse);
    // Bungie API endpoint for item transfer
    String transferUrl = "https://www.bungie.net/Platform/Destiny2/Actions/Items/TransferItem/";

    // Prepare the request body
    String requestBody = "{ \"itemReferenceHash\": " + itemHash + ", \"stackSize\": 1, \"transferToVault\": false, \"itemId\": " + itemInstanceId + ", \"characterId\": " + selectedCharacterId + ", \"membershipType\": 2, \"itemInstanceId\": " + itemInstanceId + ", \"characterIdToTransferTo\": " + sendToCharacterId + " }";

    // Set the request headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-Key", apiKey);
    headers.set("Authorization", "Bearer " + accessTokenResponse.getAccessToken());

    // Create the request entity
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

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
