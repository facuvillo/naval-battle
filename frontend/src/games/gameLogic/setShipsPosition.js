import { publishMessage } from "../mqtt/PublishMessage.js";
import { client, baseTopic } from "../mqtt/game.js";
import { getUserInfo } from "./UserInfoMock.js";

const userInfo = getUserInfo();

const shipsData = [
  // {
  //   type: "CARRIER",
  //   positions: [
  //     { x: 0, y: 0 },
  //     { x: 0, y: 1 },
  //     { x: 0, y: 2 },
  //     { x: 0, y: 3 },
  //     { x: 0, y: 4 },
  //   ],
  // },
  // {
  //   type: "BATTLESHIP",
  //   positions: [
  //     { x: 2, y: 1 },
  //     { x: 3, y: 1 },
  //     { x: 4, y: 1 },
  //     { x: 5, y: 1 },
  //   ],
  // },
  // {
  //   type: "CRUISER",
  //   positions: [
  //     { x: 3, y: 4 },
  //     { x: 3, y: 5 },
  //     { x: 3, y: 6 },
  //   ],
  // },
  {
    type: "SUBMARINE",
    positions: [
      { x: 5, y: 4 },
      { x: 5, y: 5 },
      { x: 5, y: 6 },
    ],
  },
  {
    type: "DESTROYER",
    positions: [
      { x: 7, y: 8 },
      { x: 8, y: 8 },
    ],
  },
];

export const setStyleShips = () => {
  const cells = document.querySelectorAll("#player-board .cell");
  shipsData.forEach((ship) => {
    ship.positions.forEach((position) => {
      cells.forEach((cell) => {
        const x = parseInt(cell.dataset.x);
        const y = parseInt(cell.dataset.y);
        if (x === position.x && y === position.y) {
          cell.classList.add("my-ship-cell");
        }
      });
    });
  });
}

export const setShipsPosition = () => {
  const message = JSON.stringify({
    command: "SETSHIP",
    playerId: userInfo.userId,
    ships: shipsData,
  });  
  publishMessage(client, `${baseTopic}/ships`, message);
  setStyleShips();
}