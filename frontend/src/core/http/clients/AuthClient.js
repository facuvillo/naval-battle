import axios from "axios";
import env from "../../env.js";

const authClient = axios.create({
    baseURL: `${env.API_URL}/auth`,
    headers: {
        "Content-Type": "application/json",
    }
});

export default authClient;