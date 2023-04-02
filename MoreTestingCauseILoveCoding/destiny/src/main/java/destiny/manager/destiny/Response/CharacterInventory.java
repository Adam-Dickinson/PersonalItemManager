package destiny.manager.destiny.Response;

public class CharacterInventory {
    private String characterId;
    private String inventoryResponse;

    public CharacterInventory(String characterId, String inventoryResponse) {
        this.characterId = characterId;
        this.inventoryResponse = inventoryResponse;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getInventoryResponse() {
        return inventoryResponse;
    }

    public void setInventoryResponse(String inventoryResponse) {
        this.inventoryResponse = inventoryResponse;
    }
}