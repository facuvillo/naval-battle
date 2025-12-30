import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/games",
});

export const getCurrentGames = async () => {
    const response = await api.get("/");
    return response.data;
}