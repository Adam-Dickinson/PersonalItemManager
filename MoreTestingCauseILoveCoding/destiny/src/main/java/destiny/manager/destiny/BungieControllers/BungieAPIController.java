package destiny.manager.destiny.BungieControllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import destiny.manager.destiny.AccessToken.AccessToken;
import destiny.manager.destiny.BungieServices.GetAccessTokenService;
import destiny.manager.destiny.Repositorys.AccessTokenRepository;
import destiny.manager.destiny.Response.AccessTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class BungieAPIController{

    @Value("${bungie.apiKey}")
    private String apiKey;
    @Value("${security.oauth2.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client-id}")
    private String clientID;
    @Value("${security.oauth2.redirect-uri}")
    private String redirectUri;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private GetAccessTokenService getAccessTokenService;

    @GetMapping("/authorize")
    public RedirectView authorize() {
        String uri = "https";
        String bungieAuthUrl = "https://www.bungie.net/en/OAuth/Authorize?client_id=" + clientID + "&response_type=code&redirect_uri=" + uri + "/callback";
        return new RedirectView(bungieAuthUrl);
    }
    
    @GetMapping("/callback")
    public void handleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oauthToken = request.getParameter("code");
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(oauthToken);
        accessTokenRepository.save(accessToken);

        String accessTokenStr = getAccessTokenService.getAccessToken(oauthToken);
        AccessTokenResponse tokenResponse = new AccessTokenResponse();
        tokenResponse.setAccessToken(accessTokenStr);
        response.sendRedirect("/display-user");
    }
}