package destiny.manager.destiny.BungieControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;

@Controller
public class CharacterInventoryController {

    @Autowired
    private GetCharacterInventory getCharacterInventory;
    
    @GetMapping("/character-inventory")
    public ResponseEntity<String> getCharacterInventory() throws Exception{
        return getCharacterInventory.getCharacterInventory();
    }
}
