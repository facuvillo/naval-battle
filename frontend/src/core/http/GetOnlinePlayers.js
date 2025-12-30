import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/players",
});

export const getOnlinePlayers = async () => {
    const response = await api.get("/allusers");
    return response.data;
}