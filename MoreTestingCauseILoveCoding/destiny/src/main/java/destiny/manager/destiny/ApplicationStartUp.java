package destiny.manager.destiny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import destiny.manager.destiny.Repositorys.AccessTokenRepository;

@Component
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent>{
    
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public ApplicationStartUp(AccessTokenRepository accessTokenRepository){
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        accessTokenRepository.deleteAll();
    }
}
