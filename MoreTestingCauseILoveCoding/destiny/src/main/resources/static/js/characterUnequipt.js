function showItemName(imgElement, itemName) {
    const nameElem = document.createElement('div');
    nameElem.classList.add('item.name');
    nameElem.textContent = itemName;
    imgElement.parentNode.appendChild(nameElem);
    setTimeout(() => {
        nameElem.remove();
    }, 3000);
}

function showCharacterId(imgElement) {
    const characterId = imgElement.getAttribute('data-character-id');
    console.log('Character ID:', characterId);
}