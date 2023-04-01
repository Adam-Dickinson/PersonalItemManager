package destiny.manager.destiny.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import destiny.manager.destiny.Response.BungieUserCharacterInfo;

@Repository
public interface BungieUserCharacterRepository extends JpaRepository<BungieUserCharacterInfo, Long>{
    
}
