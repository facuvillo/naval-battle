import axios from "axios";
import errorAnimation from "./errorAnimation.js";

const inputEmail = document.getElementById("inputEmailRegister");
const inputUsername = document.getElementById("inputUsernameRegister");
const inputPassword = document.getElementById("inputPasswordRegister");
const inputRepeatPassword = document.getElementById("inputRepeatPasswordRegister");
const btnRegister = document.getElementById("registerButton");

const validatePassword = () => {
  const pass = inputPassword.value;
  const repeatPass = inputRepeatPassword.value;

  if (pass === repeatPass) {
    return true;
  }
  return false;
};

const validateEmptyFields = (...fields) => {
  return fields.every((field) => field.value.trim() !== "");
};

const isValidEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

const setEmptyInputs = () => {
  inputEmail.value = "";
  inputUsername.value = "";
  inputPassword.value = "";
  inputRepeatPassword.value = "";
};

const api = axios.create({
  baseURL: "http://localhost:8080/auth",
});

const registerUser = async (user) => {
  const response = await api.post("/register", user, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
};

btnRegister.addEventListener("click", async () => {
  const errorText = document.getElementById("error-text");

  if (
    !validateEmptyFields(
      inputEmail,
      inputUsername,
      inputPassword,
      inputRepeatPassword
    )
  ) {
    errorAnimation("Todos los campos son obligatorios", errorText);
    return;
  }

  if (!isValidEmail(inputEmail.value)) {
    errorAnimation("El email no es válido", errorText);
    return;
  }

  if (!validatePassword()) {
    errorAnimation("Las contraseñas no coinciden", errorText);
    return;
  }

  const user = {
    email: inputEmail.value,
    username: inputUsername.value,
    password: inputPassword.value,
  };

  try {
    await registerUser(user);
    setEmptyInputs();
    alert("¡Registro exitoso!");
    document.body.classList.add("fade-out");
    setTimeout(() => {
      window.location.href = "./index.html";
    }, 150);
  } catch (error) {
    console.error(error);
    errorAnimation("Hay un incoveniente con el servidor", errorText);
  }
});
