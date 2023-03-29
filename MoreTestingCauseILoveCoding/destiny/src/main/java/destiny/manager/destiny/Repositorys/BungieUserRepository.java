package destiny.manager.destiny.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import destiny.manager.destiny.Response.BungieUserInfo;

public interface BungieUserRepository extends JpaRepository<BungieUserInfo, Long> {

    BungieUserInfo findFirstByOrderByIdDesc();
    
}
