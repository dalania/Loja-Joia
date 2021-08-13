const overlay = document.querySelector(".modal-overlay");
const openModal = document.querySelector(".nova-transacao");
const closeModal = document.querySelector(".button-close");
openModal.addEventListener("click", function (){

    overlay.classList.add("active");
})
closeModal.addEventListener("click", function (){
    overlay.classList.remove("active");

})

