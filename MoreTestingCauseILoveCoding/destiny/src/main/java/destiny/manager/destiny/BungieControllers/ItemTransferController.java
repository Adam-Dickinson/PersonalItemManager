package destiny.manager.destiny.BungieControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import destiny.manager.destiny.BungieServices.GetCharacterInventory;
import destiny.manager.destiny.BungieServices.ItemTransferService;
import destiny.manager.destiny.Response.TransferData;

@RestController
@RequestMapping("/api")
public class ItemTransferController {

//   @PostMapping("/transfer-item")
//   public ResponseEntity<String> transferItem(@RequestBody TransferData transferData) {
//     String itemHash = transferData.getItemHash();
//     String itemInstanceId = transferData.getItemInstanceId();
//     String selectedCharacterId = transferData.getSelectedCharacterId();
//     String sendToCharacterId = transferData.getSendToCharacterId();

//     // Log the received data
//     System.out.println("Item Hash: " + itemHash);
//     System.out.println("Item Instance ID: " + itemInstanceId);
//     System.out.println("Selected Character ID: " + selectedCharacterId);
//     System.out.println("Send To Character ID: " + sendToCharacterId);

//     // Perform the necessary logic to transfer the item
//     // Call the Bungie API or perform any other operations
    
//     // Return a response indicating the result of the transfer
//     return ResponseEntity.status(HttpStatus.OK).body("Item transferred successfully");
//   }

    @Autowired
    private GetCharacterInventory getCharacterInventory;

// @PostMapping("/transfer-item")
// public ResponseEntity<String> transferItem(@RequestBody TransferData transferData) {
//   String itemHash = transferData.getItemHash();
//   String itemInstanceId = transferData.getItemInstanceId();
//   String selectedCharacterId = transferData.getSelectedCharacterId();
//   String sendToCharacterId = transferData.getSendToCharacterId();

//   // Log the received data
//   System.out.println("Item Hash: " + itemHash);
//   System.out.println("Item Instance ID: " + itemInstanceId);
//   System.out.println("Selected Character ID: " + selectedCharacterId);
//   System.out.println("Send To Character ID: " + sendToCharacterId);

//   // Perform the necessary logic to transfer the item
// //   ItemTransferService itemTransferService = new ItemTransferService();
//   boolean transferSuccess = getCharacterInventory.transferItem(itemHash, itemInstanceId, selectedCharacterId, sendToCharacterId);

//   if (transferSuccess) {
//     // Return a success response
//     return ResponseEntity.status(HttpStatus.OK).body("Item transferred successfully");
//   } else {
//     // Return an error response
//     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Item transfer failed");
//   }
// }

@PostMapping("/transfer-item")
public ResponseEntity<String> transferItem(@RequestBody TransferData transferData) {
    String itemHash = transferData.getItemHash();
    String itemInstanceId = transferData.getItemInstanceId();
    String selectedCharacterId = transferData.getSelectedCharacterId();
    String sendToCharacterId = transferData.getSendToCharacterId();

    // Log the received data
    System.out.println("Item Hash: " + itemHash);
    System.out.println("Item Instance ID: " + itemInstanceId);
    System.out.println("Selected Character ID: " + selectedCharacterId);
    System.out.println("Send To Character ID: " + sendToCharacterId);

    // Perform the item transfer
    boolean transferSuccess = getCharacterInventory.transferItem(itemHash, itemInstanceId, selectedCharacterId, sendToCharacterId);

    if (transferSuccess) {
        // Return a success response
        return ResponseEntity.status(HttpStatus.OK).body("Item transferred successfully");
    } else {
        // Return an error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Item transfer failed");
    }
}
}
