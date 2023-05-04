package destiny.manager.destiny.BungieControllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import destiny.manager.destiny.BungieServices.GetCurrentUserInfo;
import destiny.manager.destiny.BungieServices.GetUserProfile;
import destiny.manager.destiny.Repositorys.BungieUserCharacterRepository;
import destiny.manager.destiny.Repositorys.BungieUserProfileRepository;
import destiny.manager.destiny.Repositorys.BungieUserRepository;
import destiny.manager.destiny.Response.BungieUserCharacterInfo;
import destiny.manager.destiny.Response.BungieUserInfo;
import destiny.manager.destiny.Response.BungieUserLinkedProfiles;

@Controller
public class GetUserDataController {

    @Autowired
    private BungieUserRepository bungieUserRepository;

    @Autowired
    private GetCurrentUserInfo getCurrentUserInfo;

    @Autowired
    private GetUserProfile getUserProfile;

    @Autowired
    private BungieUserProfileRepository bungieUserProfileRepository;

    @Autowired
    private BungieUserCharacterRepository bungieUserCharacterRepository;

    @RequestMapping("/display-user")
    public String userData(Model model, RedirectAttributes redirectAttributes) throws Exception {
        ResponseEntity<String> response = getCurrentUserInfo.getCurrentBungieUser();
        JSONObject json = new JSONObject(response.getBody());
        JSONObject responseJson = json.getJSONObject("Response");
        JSONArray membershipsJson = responseJson.getJSONArray("destinyMemberships");
        JSONObject bungieNetUserJson = responseJson.getJSONObject("bungieNetUser");
        BungieUserInfo bungieUserInfo = new BungieUserInfo();
        boolean firstMembershipId = true;
        for(int i = 0; i < membershipsJson.length(); i++){
            JSONObject membershipJson = membershipsJson.getJSONObject(i);
            Integer membershipType = membershipJson.getInt("membershipType");
            String membershipId = membershipJson.getString("membershipId");
            String uniqueName = bungieNetUserJson.getString("uniqueName");
            bungieUserInfo.setDisplayName(uniqueName);
            bungieUserInfo.setMembershipType(membershipType);
            if (firstMembershipId) {
                bungieUserInfo.setMembershipId(membershipId);
                bungieUserRepository.save(bungieUserInfo);
                firstMembershipId = false;
            }
        }
        return "redirect:/view-users";
    }

    @RequestMapping("/view-users")
    public String userProfile(Model model) throws Exception {
        ResponseEntity<String> response = getUserProfile.getLinkedProfiles();
        JSONObject json = new JSONObject(response.getBody());
        JSONObject responseJson = json.getJSONObject("Response");
        
        JSONObject bnetMembership = responseJson.getJSONObject("bnetMembership");
        for (int i = 0; i < bnetMembership.length(); i++) {
            int membershipType = bnetMembership.getInt("membershipType");
            String membershipId = bnetMembership.getString("membershipId");
    
            BungieUserLinkedProfiles bungieUserProfile = new BungieUserLinkedProfiles();
            bungieUserProfile.setMembershipType(membershipType);
            bungieUserProfile.setMembershipId(membershipId);
            bungieUserProfileRepository.save(bungieUserProfile);

        }
        return "redirect:/profile-info";
    }

    @RequestMapping("/profile-info")
    public String userCharacterInfo(Model model) throws Exception {
        ResponseEntity<String> response = getUserProfile.getDestinyProfile();
        JSONObject json = new JSONObject(response.getBody());
        JSONObject responseJson = json.getJSONObject("Response");
        
        JSONArray characterIdsJson = responseJson.getJSONObject("profile")
                .getJSONObject("data")
                .getJSONArray("characterIds");
        List<String> characterIds = new ArrayList<>();
        for (int i = 0; i < characterIdsJson.length(); i++) {
            characterIds.add(characterIdsJson.getString(i));
        }

        Integer guardianRank = responseJson.getJSONObject("profile")
                .getJSONObject("data")
                .getInt("currentGuardianRank");

        model.addAttribute("characterIds", characterIds);
        model.addAttribute("guardianRank", guardianRank);
        
        BungieUserCharacterInfo bungieUserCharacterInfo = new BungieUserCharacterInfo();
        bungieUserCharacterInfo.setCurrentGuardianRank(guardianRank);
        bungieUserCharacterInfo.setCharacterIds(characterIds);
        bungieUserCharacterRepository.save(bungieUserCharacterInfo);

        return "redirect:/characters";
    }
}
