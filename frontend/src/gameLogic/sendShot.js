import { publishMessage } from "../core/mqtt/PublishMessage";
import { client, baseTopic } from "../core/mqtt/game.js";
import { getUserInfo } from "./UserInfoMock.js";

const cells = document.querySelectorAll(".opponent-cell");

cells.forEach(cell => {
    cell.addEventListener("click", async () => {
        const x = cell.dataset.x;
        const y = cell.dataset.y;
        const mqttMessage = JSON.stringify({
            command: "SHOT",
            gameId: getUserInfo().currentGame,
            position: { x: parseInt(x), y: parseInt(y) }
        });
        
        publishMessage(client, `${baseTopic}/shot`, mqttMessage);

    });
});