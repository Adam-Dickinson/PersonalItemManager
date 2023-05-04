package destiny.manager.destiny.BungieControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                if (item.has("icon")) {
                    itemData.put("icon", item.getString("icon"));
                }
                itemData.put("bucketName", item.getString("bucketName"));
                itemData.put("itemHash", String.valueOf(item.getLong("itemHash")));
                itemData.put("itemInstanceId", String.valueOf(item.getLong("itemInstanceId")));
                itemList.add(itemData);
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
        return "character";
    }

        @GetMapping("/charactersUnequipt")
        public String displayUniequiptItems(Model model) throws Exception {
            List<String> characterIds = getCharacterInventory.getCharacterIds();
            List<String> charactersResponse = getCharacterInventory.getCharacterInfo(characterIds);
            List<Map<String, Object>> characterInfo = new ArrayList<>();
            ResponseEntity<Map<String, List<JSONObject>>> itemDataList = getCharacterInventory.getUnequippedItemsForCharacter();

            Map<String, List<JSONObject>> itemsJsonMap = itemDataList.getBody();
            Map<String, Map<String, List<Map<String, String>>>> itemsInfo = new HashMap<>();

            for (String characterId : itemsJsonMap.keySet()) {
                List<JSONObject> itemsJsonList = itemsJsonMap.get(characterId);
                Map<String, List<Map<String, String>>> bucketGroups = new HashMap<>();

                for (JSONObject item : itemsJsonList) {
                    Map<String, String> itemData = new HashMap<>();

                    // Check if the 'name' key exists in the JSONObject before trying to retrieve its value
                    if (item.has("name")) {
                        itemData.put("name", item.getString("name"));
                    } else {
                        // Handle the case when the 'name' key is not present, e.g., skip this item or set a default value
                        continue; // This line skips the current item if the 'name' key is not present
                    }

                    if (item.has("icon")) {
                        itemData.put("icon", item.getString("icon"));
                    }
                    String bucketName = item.getString("bucketName");
                    itemData.put("itemHash", String.valueOf(item.getLong("itemHash")));
                    if (item.has("itemInstanceId")) {
                        itemData.put("itemInstanceId", String.valueOf(item.getLong("itemInstanceId")));
                    } else {
                        continue; // This line skips the current item if the 'itemInstanceId' key is not present
                    }

                    if (!bucketGroups.containsKey(bucketName)) {
                        bucketGroups.put(bucketName, new ArrayList<>());
                    }
                    bucketGroups.get(bucketName).add(itemData);
                }
                itemsInfo.put(characterId, bucketGroups);
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
        return "characterUnequipt";
    }

    @GetMapping("/vault")
    public String getMemberVault(Model model) throws Exception {
        List<String> characterIds = getCharacterInventory.getCharacterIds();
        List<String> charactersResponse = getCharacterInventory.getCharacterInfo(characterIds);
        List<Map<String, Object>> characterInfo = new ArrayList<>();
        ResponseEntity<Map<String, List<JSONObject>>> itemDataList = getCharacterInventory.getMemberVault();

        Map<String, List<JSONObject>> itemsJsonMap = itemDataList.getBody();
        // Map<String, List<Map<String, String>>> itemsInfo = new HashMap<>();
        Map<String, Map<String, List<Map<String, String>>>> itemsInfo = new HashMap<>();

        for (String membershipId : itemsJsonMap.keySet()) {
            List<JSONObject> itemsJsonList = itemsJsonMap.get(membershipId);
            List<Map<String, String>> itemTypeDisplayNameGroup = new ArrayList<>();
        
            for (JSONObject item : itemsJsonList) {
                Map<String, String> itemData = new HashMap<>();
                itemData.put("name", item.getString("name"));
                if (item.has("icon")) {
                    itemData.put("icon", item.getString("icon"));
                }
                String itemTypeDisplayName = item.getString("itemTypeDisplayName");
        
                itemData.put("itemTypeDisplayName", itemTypeDisplayName);
                itemTypeDisplayNameGroup.add(itemData);
            }
        
            // Group items by itemTypeDisplayName
            Map<String, List<Map<String, String>>> groupedItems = itemTypeDisplayNameGroup.stream()
                    .collect(Collectors.groupingBy(itemData -> itemData.get("itemTypeDisplayName")));
        
            itemsInfo.put(membershipId, groupedItems);
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
        return "vault";
    }
}