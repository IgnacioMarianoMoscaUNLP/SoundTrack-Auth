const loginBtn = document.getElementById("loginBtn");
const jwtContainer = document.getElementById("jwtContainer");
const jwtText = document.getElementById("jwtText");

// URL de tu backend que inicia el login con Spotify
const loginUrl = "http://localhost:8080/api/auth/login"; 

loginBtn.addEventListener("click", () => {
  // Redirige al usuario a tu app para login con Spotify
  window.location.href = loginUrl;
});

// Función para capturar JWT de la URL
function getJwtFromUrl() {
  const params = new URLSearchParams(window.location.search);
  const jwt = params.get("jwt");
  if(jwt){
    localStorage.setItem("jwt", jwt);
    showJwt(jwt);
    // Limpiar URL
    window.history.replaceState({}, document.title, "/");
  }
}

// Mostrar JWT en pantalla
function showJwt(jwt) {
  loginBtn.style.display = "none";
  jwtContainer.style.display = "block";
  jwtText.textContent = jwt;
}

// Ejecutar al cargar la página
getJwtFromUrl();
