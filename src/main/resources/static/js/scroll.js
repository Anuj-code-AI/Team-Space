let currentPage = 0;
let isLoading = false;
let hasMore = true;

const overlay = document.getElementById("searchOverlay");
const resultContainer = document.getElementById("searchResult");
const loadingIndicator = document.getElementById("loadingIndicator");

/* OPEN */
function openSearch() {
    overlay.classList.add("active");
    resetAndLoad();
}

/* CLOSE */
function closeSearch() {
    overlay.classList.remove("active");
}

/* RESET */
function resetAndLoad() {
    currentPage = 0;
    hasMore = true;
    resultContainer.innerHTML = "";
    loadAllTeams();
}

/* LOAD TEAMS */
function loadAllTeams() {

    if (isLoading || !hasMore) return;

    isLoading = true;
    loadingIndicator.style.display = "block";

    api("/global/teams")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch teams");
            return res.json();
        })
        .then(page => {
            page.content.forEach(team => {

                const card = document.createElement("div");
                card.classList.add("team-card");

                card.innerHTML = `
                    <h3>${team.name}</h3>
                    <p>${team.members.length} Members</p>
                    <a href="/teams/${team.id}" class="join-btn">
                        Join
                    </a>
                `;

                resultContainer.appendChild(card);
            });

            hasMore = !page.last;
            currentPage++;

        })
        .catch(err => console.error(err))
        .finally(() => {
            isLoading = false;
            loadingIndicator.style.display = "none";
        });
}

overlay.addEventListener("scroll", () => {
    if (overlay.scrollTop + overlay.clientHeight >= overlay.scrollHeight - 50) {
        loadAllTeams();
    }
});