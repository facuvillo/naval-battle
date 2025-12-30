import axios from "axios";
import errorAnimation from "../utils/errorAnimation.js";
import { getUserInfo, setUserInfo, userInfoMock } from "../gameLogic/UserInfoMock.js";

const inputEmail = document.getElementById("inputEmailLogin");
const inputPassword = document.getElementById("inputPasswordLogin");
const btnLogin = document.getElementById("loginButton");

const isValidEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

const setEmptyInputs = () => {
  inputEmail.value = "";
  inputPassword.value = "";
};

const api = axios.create({
    baseURL: "http://localhost:8080/auth",
});

const loginUser = async (user) => {
  const response = await api.post("/login", user, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
};

btnLogin.addEventListener("click", async () => {
  const errorText = document.getElementById("error-text");

  if(!isValidEmail(inputEmail.value)){
    errorAnimation("Email invalido", errorText);
  }

  const userData = {
    email: inputEmail.value,
    password: inputPassword.value
  };

  try{
    const user = await loginUser(userData);

    const userInfo = {
      userId: user.userId,
      username: user.username,
      email: user.email,
    };

    setUserInfo(userInfo);
    
    setEmptyInputs();
    alert("¡Inicio de sesión exitoso!")
    document.body.classList.add("fade-out");
    setTimeout(() => {
      window.location.href = "./home.html";
    }, 150);
  }catch (error){
    console.error(error);

    const errorText = document.getElementById("error-text");

    if (error.response && error.response.data.message === "Wrong password") {
      errorAnimation("Contraseña incorrecta", errorText);
    } else {
      errorAnimation("Error en el inicio de sesión", errorText);
    }
  }

  try {
    await fetch("http://localhost:8080/api/players/connect", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        playerUserId: getUserInfo().userId,
        playerUsername: getUserInfo().username,
      }),
    });
    console.log("Usuario online");
  } catch (err) {
    console.error("Error registrando usuario online:", err);
  }

});

