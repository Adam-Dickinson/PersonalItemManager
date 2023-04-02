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
import destiny.manager.destiny.Response.AccessTokenResponse;

@Service
public class GetCurrentUserInfo {
    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Value("${bungie.apiKey}")
    private String apiKey;

    public ResponseEntity<String> getCurrentBungieUser() throws Exception{
        AccessTokenResponse accessTokenResponse = authTokenRepository.findFirstByOrderByIdDesc();

        String uri = "https://www.bungie.net/Platform//User/GetMembershipsForCurrentUser/";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return response;
    }
}
