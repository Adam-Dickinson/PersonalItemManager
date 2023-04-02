package destiny.manager.destiny.BungieControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@RestController
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;
    
    // @GetMapping("/character-inventory")
    // public ResponseEntity<Map<String, List<Long>>> getCharacterInventory() throws Exception{
    //     return getCharacterInventory.getCharacterInventory();
    // }

    // @GetMapping("/characters")
    // public String displayCharacters(Model model) throws Exception {
    //     List<JSONObject> characters = getCharacterInventory.getCharacterInfo();
    //     model.addAttribute("characters", characters);
    //     return "bungie-data";
    // }

    @GetMapping("/characters-info")
    public List<String> getCharactersInfo() throws Exception {
        List<String> characterIds = getCharacterInventory.getCharacterIds();
        List<String> characterInfoResponses = getCharacterInventory.getCharacterInfo(characterIds);
        return characterInfoResponses;
    }
}