package destiny.manager.destiny.BungieControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@RestController
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;

    @GetMapping("/characters-info")
    public List<String> getCharactersInfo() throws Exception {
        List<String> characterIds = getCharacterInventory.getCharacterIds();
        List<String> characterInfoResponses = getCharacterInventory.getCharacterInfo(characterIds);
        return characterInfoResponses;
    }
}