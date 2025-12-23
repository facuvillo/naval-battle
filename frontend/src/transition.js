const page = document.body.dataset.page;

// TODO Revisar como reutilizar el código
if (page === "login"){
    const registerLink = document.getElementById("toRegister");
    registerLink.addEventListener("click", (e) => {
      e.preventDefault();
      document.body.classList.add("fade-out");
      setTimeout(() => {
        window.location.href = "./register.html";
      }, 150);
    });
}else if (page === "register"){
    const registerLink = document.getElementById("toLogin");
    registerLink.addEventListener("click", (e) => {
      e.preventDefault();
      document.body.classList.add("fade-out");
      setTimeout(() => {
        window.location.href = "./index.html";
      }, 150);
    });
}