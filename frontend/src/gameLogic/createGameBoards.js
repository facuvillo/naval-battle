const playerBoard = document.getElementById("player-board");
const opponentBoard = document.getElementById("opponent-board");

const createGameBoard = (boardElement, isPlayerBoard) => {
  let x = 0;
  let y = 0;
  for (let i = 0; i < 100; i++) {
    const cell = document.createElement("div");
    cell.dataset.x = x;
    cell.dataset.y = y;
    x++;
    if (x === 10) {
      x = 0;
      y++;
    }
    cell.className = "cell";
    if (isPlayerBoard) {
      cell.classList.add("player-cell");
    } else {
      cell.classList.add("opponent-cell");
    }
    boardElement.appendChild(cell);
  }
};

createGameBoard(playerBoard, true);
createGameBoard(opponentBoard, false);
