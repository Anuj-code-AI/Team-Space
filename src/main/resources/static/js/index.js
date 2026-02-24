console.log("Js loaded")
let access = null;
let refresh = null;

if (typeof getAccessToken === "function") {
    access = getAccessToken();
}

if (typeof getRefreshToken === "function") {
    refresh = getRefreshToken();
}

function redirectToLogin() {
    window.location.href = "/auth";
}

function redirectToHome() {
    window.location.href = "/home";
}
const hamburger = document.getElementById("hamburger");
const navLinks = document.getElementById("navLinks");

hamburger.addEventListener("click", () => {
    console.log("clicked");
    navLinks.classList.toggle("active");
});



// Close nav on link click (mobile)
document.querySelectorAll(".nav-links a").forEach(link=>{
    link.addEventListener("click", ()=>{
        navLinks.classList.remove("active");
    });
});

// FAQ toggle
document.querySelectorAll(".faq-item").forEach(item=>{
    item.addEventListener("click", ()=>{
        item.classList.toggle("active");
    });
});

// Smooth scroll
document.querySelectorAll("a[href^='#']").forEach(anchor=>{
    anchor.addEventListener("click", function(e){
        e.preventDefault();
        document.querySelector(this.getAttribute("href"))
            .scrollIntoView({behavior:"smooth"});
    });
});