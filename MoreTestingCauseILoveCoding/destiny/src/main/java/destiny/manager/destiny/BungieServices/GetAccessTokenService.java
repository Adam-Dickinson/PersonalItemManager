package destiny.manager.destiny.BungieServices;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import destiny.manager.destiny.Repositorys.AuthTokenRepository;
import destiny.manager.destiny.Response.AccessTokenResponse;

@Service
public class GetAccessTokenService {
    
    @Value("${bungie.apiKey}")
    private String apiKey;
    @Value("${security.oauth2.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client-id}")
    private String clientID;
    @Value("${security.oauth2.redirect-uri}")
    private String redirectUri;

    private static final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthTokenRepository authTokenRepository;

    public String getAccessToken(String oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientID, clientSecret);
    
        String requestBody = String.format("grant_type=%s&code=%s&client_id=%s&redirect_uri=%s",
                "authorization_code", oauthToken, clientID, redirectUri);
    
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://www.bungie.net/platform/app/oauth/token/", HttpMethod.POST, request, String.class);
    
        if (response.getStatusCode().is2xxSuccessful()) {
            
            JSONObject json = new JSONObject(response.getBody());
            String accessToken = json.getString("access_token");
            int expiresIn = json.getInt("expires_in");
            String refreshToken = json.getString("refresh_token");
            int refreshExpiresIn = json.getInt("refresh_expires_in");
            String membershipId = json.getString("membership_id");
            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setExpiresIn(expiresIn);
            tokenResponse.setRefreshToken(refreshToken);
            tokenResponse.setRefreshExpiresIn(refreshExpiresIn);
            tokenResponse.setMembershipId(membershipId);
            authTokenRepository.save(tokenResponse);
            return accessToken;
        }
        return null;
    }
}
