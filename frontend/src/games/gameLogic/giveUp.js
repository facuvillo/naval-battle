import { baseTopic, client } from "../mqtt/game";
import { publishMessage } from "../mqtt/PublishMessage";
import { getUserInfo } from "./UserInfoMock";

const giveUpBtn = document.getElementById("give-up-button");

giveUpBtn.addEventListener("click" , () => {

    const message = JSON.stringify({
        command: "GIVE_UP",
        playerId: getUserInfo().userId,
        gameId: getUserInfo().currentGame
    });

    publishMessage(client, `${baseTopic}/status`, message);

});