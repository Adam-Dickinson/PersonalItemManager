package destiny.manager.destiny.Response;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile_data")
public class BungieUserCharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id", referencedColumnName = "id")
    private BungieUserCharacterInfo bungieUserCharacterInfo;

    @Column(name = "date_last_played")
    private String dateLastPlayed;

    @Column(name = "versions_owned")
    private int versionsOwned;

    @ElementCollection
    @CollectionTable(name = "character_ids")
    @Column(name = "character_id")
    private List<String> characterIds;

    @ElementCollection
    @CollectionTable(name = "season_hashes")
    @Column(name = "season_hash")
    private List<Integer> seasonHashes;

    @ElementCollection
    @CollectionTable(name = "event_card_hashes_owned")
    @Column(name = "event_card_hash_owned")
    private List<Integer> eventCardHashesOwned;

    @Column(name = "current_season_hash")
    private int currentSeasonHash;

    @Column(name = "current_season_reward_power_cap")
    private int currentSeasonRewardPowerCap;

    @Column(name = "current_guardian_rank")
    private int currentGuardianRank;

    @Column(name = "lifetime_highest_guardian_rank")
    private int lifetimeHighestGuardianRank;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BungieUserCharacterInfo getUserInfo() {
        return bungieUserCharacterInfo;
    }

    public void setUserInfo(BungieUserCharacterInfo bungieUserCharacterInfo) {
        this.bungieUserCharacterInfo = bungieUserCharacterInfo;
    }

    public String getDateLastPlayed() {
        return dateLastPlayed;
    }

    public void setDateLastPlayed(String dateLastPlayed) {
        this.dateLastPlayed = dateLastPlayed;
    }

    public int getVersionsOwned() {
        return versionsOwned;
    }

    public void setVersionsOwned(int versionsOwned) {
        this.versionsOwned = versionsOwned;
    }

    public List<String> getCharacterIds() {
        return characterIds;
    }

    public void setCharacterIds(List<String> characterIds) {
        this.characterIds = characterIds;
    }

    public List<Integer> getSeasonHashes() {
        return seasonHashes;
    }

    public void setSeasonHashes(List<Integer> seasonHashes) {
        this.seasonHashes = seasonHashes;
    }

    public List<Integer> getEventCardHashesOwned() {
        return eventCardHashesOwned;
    }

    public void setEventCardHashesOwned(List<Integer> eventCardHashesOwned) {
        this.eventCardHashesOwned = eventCardHashesOwned;
    }

    public int getCurrentSeasonHash() {
        return currentSeasonHash;
    }

    public void setCurrentSeasonHash(int currentSeasonHash) {
        this.currentSeasonHash = currentSeasonHash;
    }

    public int getCurrentSeasonRewardPowerCap() {
        return currentSeasonRewardPowerCap;
    }

    public void setCurrentSeasonRewardPowerCap(int currentSeasonRewardPowerCap) {
        this.currentSeasonRewardPowerCap = currentSeasonRewardPowerCap;
    }

    public int getCurrentGuardianRank() {
        return currentGuardianRank;
    }

    public void setCurrentGuardianRank(int currentGuardianRank) {
        this.currentGuardianRank = currentGuardianRank;
    }

    public int getLifetimeHighestGuardianRank() {
        return lifetimeHighestGuardianRank;
    }

    public void setLifetimeHighestGuardianRank(int lifetimeHighestGuardianRank) {
        this.lifetimeHighestGuardianRank = lifetimeHighestGuardianRank;
    }
}
