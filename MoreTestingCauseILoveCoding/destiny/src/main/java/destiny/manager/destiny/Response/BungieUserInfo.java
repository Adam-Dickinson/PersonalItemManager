package destiny.manager.destiny.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bungie-user")
public class BungieUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "membership_type")
    private Integer membershipType;

    @Column(name = "membership_id")
    private String membershipId;

    @Column(name = "displayName")
    private String displayName;

    public BungieUserInfo() {}

    public BungieUserInfo(Integer membershipType, String membershipId, String displayName) {
        this.membershipType = membershipType;
        this.membershipId = membershipId;
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(Integer membershipType) {
        this.membershipType = membershipType;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
