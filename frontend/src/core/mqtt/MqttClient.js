import mqtt from "mqtt";
import env from "../env.js";

console.log(env);


export function connectMQTT() {
  const client = mqtt.connect(env.HIVEMQ_URL, {
    username: env.HIVEMQ_USER,
    password: env.HIVEMQ_PASS,
    reconnectPeriod: 1000,
  });

  client.on("connect", () => console.log("Conectado a HiveMQ Cloud"));
  client.on("reconnect", () => console.log("Reintentando conexión..."));
  client.on("error", (err) => console.error("Error MQTT:", err));

  return client;
}
