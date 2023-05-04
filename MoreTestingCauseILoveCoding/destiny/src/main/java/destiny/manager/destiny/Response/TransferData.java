package destiny.manager.destiny.Response;

public class TransferData {
    private String itemHash;
    private String itemInstanceId;
    private String selectedCharacterId;
    private String sendToCharacterId;
   
    public TransferData(String itemHash, String itemInstanceId, String selectedCharacterId, String sendToCharacterId) {
        this.itemHash = itemHash;
        this.itemInstanceId = itemInstanceId;
        this.selectedCharacterId = selectedCharacterId;
        this.sendToCharacterId = sendToCharacterId;
    }
    
    public String getItemHash() {
        return itemHash;
    }
    public String getItemInstanceId() {
        return itemInstanceId;
    }
    public String getSelectedCharacterId() {
        return selectedCharacterId;
    }
    public String getSendToCharacterId() {
        return sendToCharacterId;
    }
  
    // Constructors, getters, and setters
  }
