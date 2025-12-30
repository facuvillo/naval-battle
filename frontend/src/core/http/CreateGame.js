import axios from "axios";
import { getUserInfo } from "../../gameLogic/UserInfoMock";

const api = axios.create({
  baseURL: "http://localhost:8080/api/games",
});


export const createGame = async () => {
  const user = getUserInfo();

  if (!user) {
    console.error("No hay usuario almacenado");
    return;
  }

  const response = await api.post(
    "/newGame",
    {
      playerUserId: user.userId,
      playerUsername: user.username,
    },
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  return response.data;
};