package destiny.manager.destiny.Repositorys;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import destiny.manager.destiny.AccessToken.AccessToken;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>{
   AccessToken findTopByOrderByIdDesc();
}