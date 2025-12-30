import { setModalGiveUp } from "../../gameLogic/modalGiveUp.js";
import {
  setShipsPosition,
  setStyleShips,
} from "../../gameLogic/setShipsPosition.js";
import { getUserInfo } from "../../gameLogic/UserInfoMock.js";
import { connectMQTT } from "./MqttClient.js";
import { publishMessage } from "./PublishMessage.js";
import { subscribeTopic } from "./suscribeTopic.js";

const GAME_ID = getUserInfo().currentGame;
const baseTopic = `battleship/games/${GAME_ID}`;
let myTurn = false;
let giveUpModalShown = false;

const client = connectMQTT();

client.on("connect", () => {
  subscribeTopic(client, `${baseTopic}/#`, (topic, message) => {
    messageHandler(topic, message);
  });
});

const messageHandler = (topic, message) => {
  const parsedMessage = JSON.parse(message);
  console.log(parsedMessage);
  

  if (parsedMessage.command === "SETSHIP") {
    if (parsedMessage.playerId === getUserInfo().userId) {
      setStyleShips();
    }
  }

  if (parsedMessage.command === "SHOT_RESULT") {
    if (parsedMessage.playerId === getUserInfo().userId) {
      const cells = document.querySelectorAll(".opponent-cell");
      switch (parsedMessage.result) {
        case "HIT":
          cells.forEach((cell) => {
            const x = parseInt(cell.dataset.x);
            const y = parseInt(cell.dataset.y);
            if (
              x === parsedMessage.position.x &&
              y === parsedMessage.position.y
            ) {
              cell.classList.add("hit");
            }
          });
          break;
        case "WATER":
          cells.forEach((cell) => {
            const x = parseInt(cell.dataset.x);
            const y = parseInt(cell.dataset.y);
            if (
              x === parsedMessage.position.x &&
              y === parsedMessage.position.y
            ) {
              cell.classList.add("water-hit");
            }
          });
          break;
        default:
          break;
      }
    } else {
      const cells = document.querySelectorAll(".player-cell");
      switch (parsedMessage.result) {
        case "HIT":
          cells.forEach((cell) => {
            const x = parseInt(cell.dataset.x);
            const y = parseInt(cell.dataset.y);
            if (
              x === parsedMessage.position.x &&
              y === parsedMessage.position.y
            ) {
              cell.classList.add("hit");
            }
          });
          break;
        case "WATER":
          cells.forEach((cell) => {
            const x = parseInt(cell.dataset.x);
            const y = parseInt(cell.dataset.y);
            if (
              x === parsedMessage.position.x &&
              y === parsedMessage.position.y
            ) {
              cell.classList.add("water-hit");
            }
          });
          break;
        default:
          break;
      }
    }
  }

  if (parsedMessage.command === "CHANGE_TURN") {
    if (parsedMessage.currentPlayerId === getUserInfo().userId) {
      changeTurn(false);
    } else {
      changeTurn(true);
    }
  }

  if (parsedMessage.command === "FINISHED") {
    setModalGameOver(parsedMessage);
  }

  if(parsedMessage.command === "GIVE_UP_FINISHED" && !giveUpModalShown){
    if(parsedMessage.playerId != getUserInfo().userId){
      giveUpModalShown = true;
      setModalGiveUp(parsedMessage);
    }
  }
};

const changeTurn = (isMyTurn) => {
  myTurn = isMyTurn;
  const opponentBoard = document.getElementById("opponent-board");

  if (myTurn) {
    opponentBoard.classList.remove("notYourTurn");
  } else {
    opponentBoard.classList.add("notYourTurn");
  }
};

setShipsPosition();

export { client, baseTopic };

const setModalGameOver = (parsedMessage) => {
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
      <h2>${isWinner ? "¡Ganaste!" : "Perdiste..."}</h2>
      <p>${
        isWinner
          ? "¡Felicitaciones, comandante!"
          : "Tu flota ha sido destruida."
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
