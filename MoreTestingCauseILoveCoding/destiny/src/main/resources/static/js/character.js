function showItemName(imgElement, itemName) {
    const nameElem = document.createElement('div');
    nameElem.classList.add('item.name');
    nameElem.textContent = itemName;
    imgElement.parentNode.appendChild(nameElem);
    setTimeout(() => {
        nameElem.remove();
    }, 3000);
}

function showCharacterId(imgElement, event) {
    const characterId = imgElement.getAttribute('data-character-id');
    console.log('Character ID:', characterId);

    const menu = document.getElementById('overlay-menu');
    menu.style.display = 'block';
    menu.style.left = event.pageX + 'px';
    menu.style.top = event.pageY + 'px';

    // Attach the character ID to the menu links
    document.getElementById('go-to-vault').setAttribute('data-character-id', characterId);
    document.getElementById('go-to-items').setAttribute('data-character-id', characterId);
}

document.getElementById('go-to-items').addEventListener('click', function (event) {
    const characterId = event.target.getAttribute('data-character-id');
    // You can pass the characterId as a query parameter or using another method
    window.location.href = '/charactersUnequipt?characterId=' + characterId;
});

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