package destiny.manager.destiny.BungieControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@Controller
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;

    @GetMapping("/characters")
    public String displayCharacters(Model model) throws Exception {
        List<String> characterIds = getCharacterInventory.getCharacterIds();
        List<String> charactersResponse = getCharacterInventory.getCharacterInfo(characterIds);
        List<Map<String, Object>> characterInfo = new ArrayList<>();
        for (String character : charactersResponse) {
            JSONObject characterJson = new JSONObject(character);
            String characterId = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("membershipId");
            Integer light = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("light");
            Integer raceType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("raceType");
            Integer classType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("classType");

            Map<String, Object> characterData = new HashMap<>();
            characterData.put("characterId", characterId);
            characterData.put("light", light);
            characterData.put("raceType", raceType);
            characterData.put("classType", classType);
            characterInfo.add(characterData);
        }

        model.addAttribute("characterInfo", characterInfo);
        return "character";
    }
}
