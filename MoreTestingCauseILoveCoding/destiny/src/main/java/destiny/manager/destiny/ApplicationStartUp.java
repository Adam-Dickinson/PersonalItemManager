package destiny.manager.destiny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import destiny.manager.destiny.Repositorys.AccessTokenRepository;
import destiny.manager.destiny.Repositorys.AuthTokenRepository;
import destiny.manager.destiny.Repositorys.BungieUserCharacterRepository;
import destiny.manager.destiny.Repositorys.BungieUserProfileRepository;
import destiny.manager.destiny.Repositorys.BungieUserRepository;

@Component
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent>{
    
    private final AccessTokenRepository accessTokenRepository;

    private final AuthTokenRepository authTokenRepository;

    private final BungieUserRepository bungieUserRepository;

    private final BungieUserProfileRepository bungieUserProfileRepository;

    private final BungieUserCharacterRepository bungieUserCharacterRepository;

    @Autowired
    public ApplicationStartUp(AccessTokenRepository accessTokenRepository, AuthTokenRepository authTokenRepository, BungieUserRepository bungieUserRepository, BungieUserProfileRepository bungieUserProfileRepository, BungieUserCharacterRepository bungieUserCharacterRepository){
        this.accessTokenRepository = accessTokenRepository;
        this.authTokenRepository = authTokenRepository;
        this.bungieUserRepository = bungieUserRepository;
        this.bungieUserProfileRepository = bungieUserProfileRepository;
        this.bungieUserCharacterRepository = bungieUserCharacterRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        accessTokenRepository.deleteAll();
        authTokenRepository.deleteAll();
        bungieUserRepository.deleteAll();
        bungieUserProfileRepository.deleteAll();
        bungieUserCharacterRepository.deleteAll();
    }

}
