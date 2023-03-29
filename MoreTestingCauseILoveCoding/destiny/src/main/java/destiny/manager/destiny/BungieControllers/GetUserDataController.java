package destiny.manager.destiny.BungieControllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import destiny.manager.destiny.BungieServices.GetCurrentUserInfo;
import destiny.manager.destiny.BungieServices.GetUserProfile;
import destiny.manager.destiny.Repositorys.BungieUserRepository;
import destiny.manager.destiny.Response.BungieUserInfo;

@Controller
public class GetUserDataController {

    @Autowired
    private BungieUserRepository bungieUserRepository;

    @Autowired
    private GetCurrentUserInfo getCurrentUserInfo;

    @Autowired
    private GetUserProfile getUserProfile;

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

    @GetMapping("/view-users")
    public ResponseEntity<String> getUserProfileInfo() throws Exception{
        return getUserProfile.getLinkedProfiles();
    }
}
