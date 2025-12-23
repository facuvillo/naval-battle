/*
Metodo para conectar a un juego existente con axios

Esta es la estrucutra de la request:

{
    "playerId":"82fe08d5-5e48-440c-a2af-df83230d910d",
    "gameId":"e37690b9-2b84-44ed-805d-4192bd92e9ef"
}

*/

import axios from "axios";
import { getUserInfo } from "../gameLogic/UserInfoMock";

const api = axios.create({
  baseURL: "http://localhost:8080/api/games",
});

export const joinGame = async (gameId) => {
  const user = getUserInfo();

  if (!user) {
    console.error("No hay usuario almacenado");
    return;
  }

  try {
    const response = await api.post(
      "/joinGame",
      {
        playerId: user.userId,
        gameId: gameId,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("Error al unirse al juego:", error);
    throw error;
  }
};