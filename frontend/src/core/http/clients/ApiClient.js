import axios from "axios";
import env from "../../env.js";

const api = axios.create({
    baseURL: `${env.API_URL}/api`,
    headers: {
        "Content-Type": "application/json",
    }
});

export default api;

// TODO Implementar interceptor para JWT

// api.interceptors.request.use();