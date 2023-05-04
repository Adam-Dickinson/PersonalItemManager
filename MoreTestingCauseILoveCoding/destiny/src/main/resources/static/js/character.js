function searchItems() {
    const input = document.getElementById('search-input');
    const filter = input.value.toLowerCase();
    const characterBlocks = document.getElementsByClassName('character-block');
  
    for (let i = 0; i < characterBlocks.length; i++) {
        const items = characterBlocks[i].getElementsByTagName('li');
        let hasMatch = false;

        for (let j = 0; j < items.length; j++) {
            const itemName = items[j].getElementsByTagName('p')[1];

            if (itemName) {
                const textValue = itemName.textContent || itemName.innerText;

                if (textValue.toLowerCase().indexOf(filter) > -1) {
                    hasMatch = true;
                    itemName.style.backgroundColor = '#f2f8ff';
                    items[j].style.display = '';
                } else {
                    itemName.style.backgroundColor = '';
                    items[j].style.display = 'none';
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

// function logItemInfo(element) {
//     const itemHash = element.getAttribute('data-item-hash');
//     const itemInstanceId = element.getAttribute('data-item-instance-id');
//     console.log('Item Hash:', itemHash);
//     console.log('Item Instance ID:', itemInstanceId);

//     const characterId = element.closest('.character-block').querySelector('.card-image').getAttribute('data-character-id');
//     console.log('Selected Charcter:', characterId);

//     showCharacterDetails(characterId);
//     openTransferModal();
// }

let itemHash = '';
let itemInstanceId = '';
let selectedCharacterId = '';

function logItemInfo(element) {
  itemHash = element.getAttribute('data-item-hash');
  itemInstanceId = element.getAttribute('data-item-instance-id');
  console.log('Item Hash:', itemHash);
  console.log('Item Instance ID:', itemInstanceId);

  selectedCharacterId = element.closest('.character-block').querySelector('.card-image').getAttribute('data-character-id');
  console.log('Selected Character:', selectedCharacterId);

  showCharacterDetails(selectedCharacterId);
  openTransferModal();
}

function showCharacterDetails(element) {
    const characterDetails = document.getElementById('character-details');
    characterDetails.innerHTML = '';

    const characterBlocks = document.getElementsByClassName('character-block');
    for (let i = 0; i < characterBlocks.length; i++) {
        const characterBlock = characterBlocks[i];
        const characterId = characterBlock.querySelector('.card-image').getAttribute('data-character-id');
        const characterClass = characterBlock.querySelector('.overlay > div:last-child').textContent;
        const backgroundEmblem = characterBlock.querySelector('.card-image').getAttribute('src');

        const characterInfo = document.createElement('div');
        characterInfo.innerHTML = `
            <div class="character-container">
                <span class="class-type">${characterClass}</span>
                <img src="${backgroundEmblem}" alt="Emblem" class="character-emblem" onclick="logCharacterId(event)">
            </div>
            <p>Character ID: ${characterId}</p>
        `;
        characterInfo.dataset.characterId = characterId;
        characterDetails.appendChild(characterInfo);
    }
}

// function logCharacterId(event) {
//     const characterId = event.target.closest('.character-container').nextElementSibling.textContent.split(' ')[2];
//     console.log('Send To Charcter:', characterId);
// }

// function logCharacterId(event) {
//     const characterId = event.target.closest('.character-container').nextElementSibling.textContent.split(' ')[2];
//     console.log('Send To Character:', characterId);
  
//     const itemHash = document.querySelector('.character-block .card-image').getAttribute('data-item-hash');
//     const itemInstanceId = document.querySelector('.character-block .card-image').getAttribute('data-item-instance-id');
//     const selectedCharacterId = document.querySelector('.character-block .card-image').getAttribute('data-character-id');
  
//     // Prepare the data object to send in the request body
//     const requestData = {
//       itemHash: itemHash,
//       itemInstanceId: itemInstanceId,
//       selectedCharacterId: selectedCharacterId,
//       sendToCharacterId: characterId
//     };
  
//     // Send the data to your backend API
//     fetch('/api/transfer-item', {
//       method: 'POST',
//       headers: {
//         'Content-Type': 'application/json'
//       },
//       body: JSON.stringify(requestData)
//     })
//       .then(response => response.json())
//       .then(data => {
//         // Handle the response from the backend
//         console.log('Transfer response:', data);
//         // Perform any necessary UI updates based on the response
//       })
//       .catch(error => {
//         // Handle any errors that occurred during the request
//         console.error('Error:', error);
//       });
//   }

function logCharacterId(event) {
    const characterId = event.target.closest('.character-container').nextElementSibling.textContent.split(' ')[2];
    console.log('Send To Character:', characterId);
  
    // Prepare the data object to send in the request body
    const requestData = {
      itemHash: itemHash,
      itemInstanceId: itemInstanceId,
      selectedCharacterId: selectedCharacterId,
      sendToCharacterId: characterId
    };
  
    // Send the data to your backend API
    fetch('/api/transfer-item', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    })
      .then(response => response.json())
      .then(data => {
        // Handle the response from the backend
        console.log('Transfer response:', data);
        // Perform any necessary UI updates based on the response
      })
      .catch(error => {
        // Handle any errors that occurred during the request
        console.error('Error:', error);
      });
  }

function openTransferModal() {
    const transferModal = new Foundation.Reveal($('#transfer-modal'));
    transferModal.open();
}
