// =======================
// CONFIG
// =======================

const API_BASE = "http://localhost:8080/api";


// =======================
// AUTHENTICATION FORM
// =======================

let isRegisterMode = false;

function redirectOAuth(provider) {
    if (provider === "google") {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
    } else if (provider === "github") {
        window.location.href = "/oauth2/authorization/github";
    }
}

function showEmailForm() {
    document.getElementById("oauthSection").classList.add("hidden");
    document.getElementById("emailSection").classList.remove("hidden");
}

function goBack() {
    document.getElementById("emailSection").classList.add("hidden");
    document.getElementById("oauthSection").classList.remove("hidden");
}


// =======================
// TOKEN STORAGE
// =======================

function saveTokens(access, refresh) {
    localStorage.setItem("access", access);
    localStorage.setItem("refresh", refresh);
}

function getAccessToken() {
    return localStorage.getItem("access");
}

function getRefreshToken() {
    return localStorage.getItem("refresh");
}

function clearTokens() {
    localStorage.removeItem("access");
    localStorage.removeItem("refresh");
}

function autoLogin() {
    const token = localStorage.getItem("access");

    if (!token) return;

    // optional: try a protected endpoint to verify token
    api("/home")
        .then(res => {
            if (res.ok) {
                window.location.href = "/home";
            }
        })
        .catch(() => logout());
}



// =======================
// LOGIN
// =======================

async function login(email, password) {
    const res = await fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (!res.ok) throw new Error("Invalid credentials");

    const data = await res.json(); // { accessToken, refreshToken }

    saveTokens(data.accessToken, data.refreshToken);

    window.location.href = "/home";
}


// =======================
// Register
// =======================

async function register(name, email, password) {
    const res = await fetch(`${API_BASE}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password })
    });

    if (!res.ok) {
        const err = await res.text();
        throw new Error(err);
    }

    const data = await res.json(); // { accessToken, refreshToken }

    saveTokens(data.accessToken, data.refreshToken);

    window.location.href = "/home";
}


// =======================
// AUTO LOGIN CHECK
// =======================

function requireAuth() {
    const token = getAccessToken();

    if (!token) {
        window.location.href = "/auth";
    }
}


// =======================
// REFRESH TOKEN
// =======================

async function refreshAccessToken() {
    const refresh = getRefreshToken();

    if (!refresh) {
        logout();
        return;
    }

    const res = await fetch(`${API_BASE}/auth/refresh`, {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: refresh
    });

    if (!res.ok) {
        logout();
        return;
    }

    const data = await res.json();

    localStorage.setItem("access", data.accessToken);
}


// =======================
// PROTECTED API WRAPPER
// =======================
async function api(url, options = {}) {
    let token = localStorage.getItem("access");

    options.headers = {
        ...options.headers,
        Authorization: "Bearer " + token,
        "Content-Type": "application/json"
    };

    let res = await fetch(API_BASE + url, options);

    // 👇 THIS is where 401 handling goes
    if (res.status === 401) {
        await refreshAccessToken();

        token = localStorage.getItem("access");
        options.headers.Authorization = "Bearer " + token;

        res = await fetch(API_BASE + url, options);
    }

    return res;
}


// =======================
// LOGOUT
// =======================

function logout() {
    clearTokens();
    window.location.href = "/index";
}

// =======================
// FORM HANDLER (LOGIN PAGE)
// =======================

function toggleMode() {
    isRegisterMode = !isRegisterMode;

    const nameField = document.getElementById("nameField");
    const submitText = document.getElementById("submitText");
    const switchMessage = document.getElementById("switchMessage");

    if (isRegisterMode) {
        nameField.classList.remove("hidden");
        submitText.textContent = "Register";
        switchMessage.textContent = "Already have an account?";
    } else {
        nameField.classList.add("hidden");
        submitText.textContent = "Login";
        switchMessage.textContent = "Don't have an account?";
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("#authForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = document.querySelector("#name").value;
        const email = document.querySelector("#email").value;
        const password = document.querySelector("#password").value;

        try {
            if (isRegisterMode) {
                await register(name, email, password);
            } else {
                await login(email, password);
            }
        } catch (err) {
            alert("Operation failed");
            console.error(err);
        }
    });
});
