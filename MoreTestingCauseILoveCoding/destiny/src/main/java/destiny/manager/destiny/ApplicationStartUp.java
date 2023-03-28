package destiny.manager.destiny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import destiny.manager.destiny.Repositorys.AccessTokenRepository;
import destiny.manager.destiny.Repositorys.AuthTokenRepository;

@Component
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent>{
    
    private final AccessTokenRepository accessTokenRepository;

    private final AuthTokenRepository authTokenRepository;

    @Autowired
    public ApplicationStartUp(AccessTokenRepository accessTokenRepository, AuthTokenRepository authTokenRepository){
        this.accessTokenRepository = accessTokenRepository;
        this.authTokenRepository = authTokenRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        accessTokenRepository.deleteAll();
        authTokenRepository.deleteAll();
    }

}
