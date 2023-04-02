package destiny.manager.destiny.BungieControllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@Controller
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;
    
    @GetMapping("/character-inventory")
    public ResponseEntity<Map<String, List<Long>>> getCharacterInventory() throws Exception{
        return getCharacterInventory.getCharacterInventory();
    }
}