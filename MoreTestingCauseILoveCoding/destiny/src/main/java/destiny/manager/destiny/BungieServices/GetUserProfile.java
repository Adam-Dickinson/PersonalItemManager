package destiny.manager.destiny.BungieServices;

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
public class GetUserProfile {
    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private BungieUserRepository bungieUserRepository;

    @Value("${bungie.apiKey}")
    private String apiKey;

    public ResponseEntity<String> getLinkedProfiles() throws Exception{
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();
        BungieUserInfo bungieUserInfo = bungieUserRepository.findFirstByOrderByIdDesc();

        String uri = "https://bungie.net/Platform/Destiny2/" + bungieUserInfo.getMembershipType() + "/Profile/" + accessTokenResponse.getMembershipId() + "/LinkedProfiles/?getAllMemberships=true";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        System.out.println(response);
        return response;
    }
}
