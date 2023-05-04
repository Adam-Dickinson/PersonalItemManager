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