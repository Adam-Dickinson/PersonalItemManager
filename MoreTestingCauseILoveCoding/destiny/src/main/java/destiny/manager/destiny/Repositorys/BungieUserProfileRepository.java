package destiny.manager.destiny.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import destiny.manager.destiny.Response.BungieUserLinkedProfiles;

public interface BungieUserProfileRepository extends JpaRepository<BungieUserLinkedProfiles, Long>{
    BungieUserLinkedProfiles findFirstByOrderByIdDesc();
}
