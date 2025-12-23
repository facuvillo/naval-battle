import { getCurrentGames } from "./games/http/GetCurrentGames.js";
import { createGame } from "./games/http/CreateGame.js";
import { getUserInfo, setCurrentTopicBase } from "./games/gameLogic/UserInfoMock.js";
import { joinGame } from "./games/http/joinGame.js";
import { getOnlinePlayers } from "./games/http/GetOnlinePlayers.js";
import { publishMessage } from "./games/mqtt/PublishMessage.js";
import { baseTopic, client } from "./games/mqtt/game.js";
import { subscribeTopic } from "./games/mqtt/suscribeTopic.js";
import { currentGameCard } from "./games/gameLogic/gameCard.js";

const btnRefresh = document.getElementById("refreshGames");
const availableGamesHTML = document.getElementById("games-cards-container");
const btnCreateGame = document.getElementById("createGameBtn");

const usernameDisplay = document.getElementById("username");
const userIdDisplay = document.getElementById("userId");

const userInfo = getUserInfo();

usernameDisplay.textContent = userInfo.username;
userIdDisplay.textContent = userInfo.userId;

const setCurrentGames = async () => {
    const currentGames = await getCurrentGames();
    currentGames.forEach(game => {
        availableGamesHTML.innerHTML += currentGameCard(game.payerUsername);
        /*
        const gameElement = document.createElement("div");
        gameElement.className = "cards";
        gameElement.innerHTML = `
            <img src="../assets/astronaut.png" width="60px" height="60px" alt="astronaut-image">
            <h3>${game.playerUsername}</h3>
            <p>${game.playerUserId}</p>
            <button id="joinGameBtn" class="btn-accept">Unirse</button>
        `;
        */
        const joinBtn = gameElement.querySelector("#joinGamebtn");
        joinBtn.addEventListener("click", async () => {
          try {
            const response = await joinGame(game.gameId);
            setCurrentTopicBase(game.gameId)
            window.location.href = "../game.html";
          } catch (error) {
            console.error("Error al unirse al juego:", error);
          }
        });
    });

}

setCurrentGames();

btnRefresh?.addEventListener("click", (event) => {
    event.preventDefault();
    availableGamesHTML.innerHTML = "";
    setCurrentGames();
});

btnCreateGame?.addEventListener("click", async (event) => {
    const response = await createGame();
    const gameId = response.split("/")[2];
    setCurrentTopicBase(gameId);
    setTimeout(() => {
        window.location.href = "../game.html";
    })
});

const inviteBtn = document.getElementById("inviteBtn");
const modal = document.getElementById("inviteModal");
const onlinePlayersList = document.getElementById("onlinePlayersList");

const loadOnlinePlayers = async () => {
  onlinePlayersList.innerHTML = "";
  try {
    const players = await getOnlinePlayers();
    
    players.forEach((player) => {
      if(player.uuid !== getUserInfo().userId){
        const li = document.createElement("li");
        li.textContent = player.username;
        li.dataset.playerId = player.uuid;
        li.style.cursor = "pointer";
  
        li.addEventListener("click", async () => {
          console.log("Invitando a ", player.username, player.uuid);
          const response = await createGame();
          const gameId = response.split("/")[2];
          setCurrentTopicBase(gameId);
          const message = JSON.stringify({
            command:"INVITE",
            gameId:gameId,
            playerName : player.username
          });
          publishMessage(client, `battleship/players/${player.uuid}`, message);
          setTimeout(() => {
            window.location.href = "../game.html";
          }, 500);
        });
  
        onlinePlayersList.appendChild(li);
      }
    });
  } catch (error) {
    console.error("Error cargando jugadores online:", error);
  }
};

inviteBtn.addEventListener("click", async () => {
  modal.style.display = "block";
  await loadOnlinePlayers();
});

client.on("connect", () => {
  const personalTopic = `battleship/players/${getUserInfo().userId}`;

  subscribeTopic(client, personalTopic, (topic, message) => {
    const parsedMessage = JSON.parse(message);
    if (parsedMessage.command === "INVITE") {
      showInviteModal(parsedMessage);
    }
  });
});

function showInviteModal(inviteData) {
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

  const box = document.createElement("div");
  box.style.background = "white";
  box.style.padding = "30px";
  box.style.borderRadius = "12px";
  box.style.textAlign = "center";
  box.style.boxShadow = "0 0 20px rgba(0,0,0,0.3)";
  box.innerHTML = `
    <h2>Invitación recibida</h2>
    <p>El jugador <strong>${inviteData.playerName}</strong> te invita a un juego.</p>
  `;

  const acceptBtn = document.createElement("button");
  acceptBtn.textContent = "Aceptar";
  acceptBtn.style.margin = "10px";
  acceptBtn.style.padding = "10px 20px";
  acceptBtn.style.background = "#28a745";
  acceptBtn.style.color = "white";
  acceptBtn.style.border = "none";
  acceptBtn.style.borderRadius = "8px";
  acceptBtn.style.cursor = "pointer";
  acceptBtn.onclick = async () => {
    try {
      await joinGame(inviteData.gameId);
      setCurrentTopicBase(inviteData.gameId);
      window.location.href = "../game.html";
    } catch (error) {
      console.error("Error al unirse al juego:", error);
    }
  };

  const declineBtn = document.createElement("button");
  declineBtn.textContent = "Rechazar";
  declineBtn.style.margin = "10px";
  declineBtn.style.padding = "10px 20px";
  declineBtn.style.background = "#dc3545";
  declineBtn.style.color = "white";
  declineBtn.style.border = "none";
  declineBtn.style.borderRadius = "8px";
  declineBtn.style.cursor = "pointer";
  declineBtn.onclick = () => {
    document.body.removeChild(modal);
  };

  box.appendChild(acceptBtn);
  box.appendChild(declineBtn);
  modal.appendChild(box);
  document.body.appendChild(modal);
}
