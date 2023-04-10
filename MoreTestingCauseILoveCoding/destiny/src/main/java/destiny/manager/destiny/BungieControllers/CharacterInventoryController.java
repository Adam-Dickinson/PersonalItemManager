package destiny.manager.destiny.BungieControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@Controller
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;

    // @GetMapping("/characters")
    // public String displayCharacters(Model model) throws Exception {
    //     List<String> characterIds = getCharacterInventory.getCharacterIds();
    //     List<String> charactersResponse = getCharacterInventory.getCharacterInfo(characterIds);
    //     List<Map<String, Object>> characterInfo = new ArrayList<>();
    //     ResponseEntity<Map<String, List<JSONObject>>> itemDataList = getCharacterInventory.getCharacterInventory();

    //     Map<String, List<JSONObject>> itemsJsonMap = itemDataList.getBody();
    //     Map<String, List<Map<String, String>>> itemsInfo = new HashMap<>();

    //     for (String characterId : itemsJsonMap.keySet()) {
    //         List<JSONObject> itemsJsonList = itemsJsonMap.get(characterId);
    //         List<Map<String, String>> itemList = new ArrayList<>();

    //         for (JSONObject item : itemsJsonList) {
    //             Map<String, String> itemData = new HashMap<>();
    //             itemData.put("name", item.getString("name"));
    //             itemData.put("icon", item.getString("icon"));
    //             itemList.add(itemData);
    //         }
    //         itemsInfo.put(characterId, itemList);
    //     }
        
    //     for (String character : charactersResponse) {
    //         JSONObject characterJson = new JSONObject(character);
    //         String characterId = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("membershipId");
    //         Integer light = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("light");
    //         Integer raceType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("raceType");
    //         Integer classType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("classType");
    //         String emblemBackgroundPath = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("emblemBackgroundPath");

    //         Map<String, Object> characterData = new HashMap<>();
    //         characterData.put("characterId", characterId);
    //         characterData.put("light", light);
    //         characterData.put("raceType", raceType);
    //         characterData.put("classType", classType);
    //         characterData.put("emblemBackgroundPath", emblemBackgroundPath);
    //         characterInfo.add(characterData);
    //     }

    //     model.addAttribute("characterInfo", characterInfo);
    //     model.addAttribute("itemsInfo", itemsInfo);
    //     return "characterData";
    // }

    @GetMapping("/characters")
    public String displayCharacters(Model model) throws Exception {
        List<String> characterIds = getCharacterInventory.getCharacterIds();
        List<String> charactersResponse = getCharacterInventory.getCharacterInfo(characterIds);
        List<Map<String, Object>> characterInfo = new ArrayList<>();
        ResponseEntity<Map<String, List<JSONObject>>> itemDataList = getCharacterInventory.getCharacterInventory();

        Map<String, List<JSONObject>> itemsJsonMap = itemDataList.getBody();
        Map<String, List<Map<String, String>>> itemsInfo = new HashMap<>();

        for (String characterId : itemsJsonMap.keySet()) {
            List<JSONObject> itemsJsonList = itemsJsonMap.get(characterId);
            List<Map<String, String>> itemList = new ArrayList<>();

            for (JSONObject item : itemsJsonList) {
                Map<String, String> itemData = new HashMap<>();
                itemData.put("name", item.getString("name"));
                itemData.put("icon", item.getString("icon"));
                itemList.add(itemData);
                System.out.println("item: " + item);
            }
            itemsInfo.put(characterId, itemList);
        }

        for (String character : charactersResponse) {
            JSONObject characterJson = new JSONObject(character);
            String membershipId = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("membershipId");
            String characterId = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("characterId");
            Integer light = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("light");
            Integer raceType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("raceType");
            Integer classType = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("classType");
            String emblemBackgroundPath = characterJson.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("emblemBackgroundPath");

            Map<String, Object> characterData = new HashMap<>();
            characterData.put("membershipId", membershipId);
            characterData.put("characterId", characterId);
            characterData.put("light", light);
            characterData.put("raceType", raceType);
            characterData.put("classType", classType);
            characterData.put("emblemBackgroundPath", emblemBackgroundPath);
            characterInfo.add(characterData);
        }

        model.addAttribute("characterInfo", characterInfo);
        model.addAttribute("itemsInfo", itemsInfo);
        return "characterData";
    }





    // @GetMapping("/character-items")
    // public ResponseEntity<Map<String, List<JSONObject>>> getInventory() throws Exception {
    //     return getCharacterInventory.getCharacterInventory();
    // }
}
