const toggleButton = document.getElementById('toggle-btn');
const sidebar = document.getElementById('sidebar');
function toggleSideBar(){
    sidebar.classList.toggle('close');
    toggleButton.classList.toggle('rotate');

    closeAllSubMenus();

}


function toggleSubMenu(button) {

    const subMenu = button.nextElementSibling;

    const isOpen = subMenu.classList.contains("show");

    closeAllSubMenus();

    if (!isOpen) {
        subMenu.classList.add("show");
        button.classList.add("rotate");
    }
}
function toggleSubMenuForTeam(button) {

    const firstMenu = button.nextElementSibling;         // first UL
    const secondMenu = firstMenu.nextElementSibling;    // second UL (teamSubMenu)

    const isOpen = firstMenu.classList.contains("show");

    closeAllSubMenus();

    if (!isOpen) {
        firstMenu.classList.add("show");
        secondMenu.classList.add("show");
        button.classList.add("rotate");
    }
}
function closeAllSubMenus() {

    document.querySelectorAll(".sub-menu").forEach(menu => {
        menu.classList.remove("show");
    });

    document.querySelectorAll(".dropdown-btn").forEach(btn => {
        btn.classList.remove("rotate");
    });
}

let teamsLoaded = false; // Prevent multiple API calls
function loadTeams(button) {

    if (teamsLoaded) return;

    api("/teams")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch teams");
            return res.json();
        })
        .then(teams => {

            const teamSubMenu = document.getElementById("teamSubMenu");

            teams.forEach(team => {

                const li = document.createElement("li");
                const div = document.createElement("div");

                div.innerHTML = `
                    <a href="/teams/${team.id}">
                        <i class="fa-solid fa-users"></i>
                        ${team.name}
                    </a>
                `;

                li.appendChild(div);
                teamSubMenu.appendChild(li);
            });

            teamsLoaded = true;
        })
        .catch(err => console.error("Error loading teams:", err));
}

function logout() {
    localStorage.removeItem("access");
    localStorage.removeItem("refresh");
    window.location.href = "/";
}


function setupAuthButton() {
    const authBtn = document.getElementById("authBtn");
    if (!authBtn) return;

    const token = localStorage.getItem("access");

    if (token) {
        authBtn.innerHTML =
            '<i class="fa-solid fa-right-from-bracket"></i> Logout';
        authBtn.onclick = logout;
    } else {
        authBtn.innerHTML =
            '<i class="fa-solid fa-right-to-bracket"></i> Login';
        authBtn.onclick = () => window.location.href = "/auth";
    }
}

function createTeam() {
    document.getElementById("teamModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("teamModal").style.display = "none";
}
document.getElementById("teamForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const name = document.getElementById("teamName").value.trim();
    const description = document.getElementById("teamDescription").value.trim();

    if (!name) {
        alert("Team name is required!");
        return;
    }

    const token = getAccessToken();
    console.log(token)

    try {
        const response = await fetch(`${API_BASE}/teams`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                name: name,
            })
        });

        if (!response.ok) {
            throw new Error("Failed to create team");
        }

        const data = await response.json();
        console.log("Team Created:", data);

        // Close modal
        closeModal();

        // Redirect to team workspace using returned ID
        // Assuming backend returns: { id: 5, name: "...", ... }
        window.location.href = "/team"

    } catch (error) {
        console.error(error);
        alert("Something went wrong while creating the team.");
    }
});
document.addEventListener("DOMContentLoaded", setupAuthButton);