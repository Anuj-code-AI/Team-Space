const params = new URLSearchParams(window.location.search);

const access = params.get("access");
const refresh = params.get("refresh");

if (access && refresh) {
    localStorage.setItem("access", access);
    localStorage.setItem("refresh", refresh);
    console.log(access);
    console.log(refresh);
    window.location.href = "/home";
}