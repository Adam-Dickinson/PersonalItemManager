const accordions = document.getElementsByClassName("accordion");
for (let i = 0; i < accordions.length; i++) {
    accordions[i].addEventListener("click", function() {
        this.classList.toggle("active");
        const bucketIcons = this.getElementsByClassName("bucket-icons")[0];
        bucketIcons.classList.toggle("show");
    });
}


function searchItems() {
    const input = document.getElementById('search-input');
    const filter = input.value.toLowerCase();
    const characterBlocks = document.getElementsByClassName('character-block');

    for (let i = 0; i < characterBlocks.length; i++) {
        const items = characterBlocks[i].getElementsByTagName('p');
        let hasMatch = false;

        for (let j = 0; j < items.length; j++) {
            const itemName = items[j];

            if (itemName) {
                const textValue = itemName.textContent || itemName.innerText;

                if (textValue.toLowerCase().indexOf(filter) > -1) {
                    hasMatch = true;
                    itemName.style.backgroundColor = '#f2f8ff';
                    items[j].parentNode.style.display = '';
                } else {
                    itemName.style.backgroundColor = '';
                    items[j].parentNode.style.display = 'none';
                }
            }
        }

        if (hasMatch) {
            characterBlocks[i].style.display = '';
        } else {
            characterBlocks[i].style.display = 'none';
        }
    }
}

// function itemIconClicked(event, itemHash, itemInstanceId, characterId) {
//     event.stopPropagation();
//     console.log('Character ID:', characterId);
//     console.log('Item Hash:', itemHash);
//     console.log('Item Instance ID:', itemInstanceId);
    
//     // Open the transfer modal
//     var modal = document.getElementById("transferModal");
//     modal.style.display = "block";
// }

// function emblemClicked(element, event) {
//     event.stopPropagation();
//     var characterId = element.getAttribute("data-character-id");
//     console.log("Emblem clicked. Character ID:", characterId);
    
//     // Do not close the transfer modal
//     // var modal = document.getElementById("transferModal");
//     // modal.style.display = "none";
// }

let itemHash = '';
let itemInstanceId = '';
let selectedCharacterId = '';
let sendToCharacterId = '';

function itemIconClicked(event, iHash, iInstanceId, characterId) {
    event.stopPropagation();
    selectedCharacterId = characterId;
    itemHash = iHash;
    itemInstanceId = iInstanceId;
    console.log('Character ID:', characterId);
    console.log('Item Hash:', itemHash);
    console.log('Item Instance ID:', itemInstanceId);


    // Open the transfer modal
    var modal = document.getElementById("transferModal");
    modal.style.display = "block";
}

function emblemClicked(element, event) {
    event.stopPropagation();
    sendToCharacterId = element.getAttribute("data-character-id");
    console.log("Emblem clicked. Character ID:", sendToCharacterId);

    sendTransferRequest();
}

function sendTransferRequest() {
    fetch('/api/transfer-item', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            itemHash: itemHash,
            itemInstanceId: itemInstanceId,
            selectedCharacterId: selectedCharacterId,
            sendToCharacterId: sendToCharacterId
        })
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to transfer item');
        }
    })
    .then(result => {
        console.log(result);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// Get the modal
var modal = document.getElementById("transferModal");

// Get the close button element
var closeButton = document.getElementsByClassName("close")[0];

// When the user clicks on the close button, close the modal
closeButton.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

// document.getElementById("refreshButton").addEventListener("click", refreshData);

// async function refreshData() {
//     try {
//       // Replace '/your-endpoint' with the actual endpoint URL
//       const response = await fetch('/charactersUnequipt');
      
//       if (response.ok) {
//         // If the response is successful, reload the page
//         location.reload();
//       } else {
//         // If the response failed, log the error
//         console.error("Error refreshing data:", response.statusText);
//       }
//     } catch (error) {
//       // If there's an error during the request, log the error
//       console.error("Error fetching data:", error);
//     }
//   }