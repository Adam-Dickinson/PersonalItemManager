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