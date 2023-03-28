package destiny.manager.destiny.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import destiny.manager.destiny.Response.AccessTokenResponse;

@Repository
public interface AuthTokenRepository extends JpaRepository<AccessTokenResponse, Long> {
}
