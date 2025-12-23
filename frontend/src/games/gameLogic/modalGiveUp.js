import { getUserInfo } from "./UserInfoMock";

export const setModalGiveUp = (parsedMessage) => {
  const winnerId = parsedMessage.winnerId; 
  const isWinner = winnerId === getUserInfo().userId;

  const modal = document.createElement("div");
  modal.style.position = "fixed";
  modal.style.top = 0;
  modal.style.left = 0;
  modal.style.width = "100%";
  modal.style.height = "100%";
  modal.style.backgroundColor = "rgba(0, 0, 0, 0.7)";
  modal.style.display = "flex";
  modal.style.flexDirection = "column";
  modal.style.alignItems = "center";
  modal.style.justifyContent = "center";
  modal.style.zIndex = 1000;

  const messageBox = document.createElement("div");
  messageBox.style.background = "white";
  messageBox.style.padding = "30px";
  messageBox.style.borderRadius = "12px";
  messageBox.style.textAlign = "center";
  messageBox.style.boxShadow = "0 0 20px rgba(0,0,0,0.3)";

  messageBox.innerHTML = `
      <h2>${isWinner ? "¡Has ganado por rendición!" : "Te has rendido..."}</h2>
      <p>${
        isWinner
          ? "El otro jugador se ha rendido, ¡felicidades comandante!"
          : "Has decidido rendirte. Tu flota ha sido destruida."
      }</p>
    `;

  const homeButton = document.createElement("button");
  homeButton.textContent = "Volver al inicio";
  homeButton.style.marginTop = "20px";
  homeButton.style.padding = "10px 20px";
  homeButton.style.background = "#007BFF";
  homeButton.style.color = "white";
  homeButton.style.border = "none";
  homeButton.style.borderRadius = "8px";
  homeButton.style.cursor = "pointer";
  homeButton.onclick = () => {
    window.location.href = "../home.html";
  };

  messageBox.appendChild(homeButton);
  modal.appendChild(messageBox);
  document.body.appendChild(modal);
};
