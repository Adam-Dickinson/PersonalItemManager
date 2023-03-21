package destiny.manager.destiny.BungieServices;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import destiny.manager.destiny.Response.AccessTokenResponse;

@Service
public class GetAccessTokenService {
    
    @Value("${bungie.apiKey}")
    private String apiKey;
    @Value("${security.oauth2.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client-id}")
    private String clientID;
    @Value("${security.oauth2.redirect.uri}")
    private String redirectUri;

    private static final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String oauthToken) {
        // Set up headers and request body for token endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientID, clientSecret);
    
        String requestBody = String.format("grant_type=%s&code=%s&client_id=%s&redirect_uri=%s",
                "authorization_code", oauthToken, clientID, redirectUri);
    
        // Send request to token endpoint
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://www.bungie.net/platform/app/oauth/token/", HttpMethod.POST, request, String.class);
    
        // Check if response was successful and parse access token
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println(responseBody); // Add this line to print the response body
            try {
                AccessTokenResponse tokenResponse = AccessTokenResponse.fromJson(responseBody);
                String accessToken = tokenResponse.getAccessToken();
                return accessToken;
            } catch (IOException e) {
            }
        }
        return null;
    }
}
