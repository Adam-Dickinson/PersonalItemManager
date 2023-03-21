package destiny.manager.destiny.AccessToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import destiny.manager.destiny.Repositorys.AccessTokenRepository;

@Service
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository){
        this.accessTokenRepository = accessTokenRepository;
    }

    public String getToken(){
        AccessToken token = accessTokenRepository.findAll().get(0);
        return token.getToken();
    }
    
}
