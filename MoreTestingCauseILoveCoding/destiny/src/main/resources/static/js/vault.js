// Get all accordion elements
const accordions = document.getElementsByClassName("accordion");

// Add click event listeners to toggle visibility
for (let i = 0; i < accordions.length; i++) {
    accordions[i].addEventListener("click", function() {
        this.classList.toggle("active");
        const cardSection = this.querySelector(".card-section");
        cardSection.style.display = cardSection.style.display === "none" ? "block" : "none";
    });
}