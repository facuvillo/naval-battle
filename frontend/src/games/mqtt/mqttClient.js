import mqtt from "mqtt";

export function connectMQTT() {
  const client = mqtt.connect(import.meta.env.VITE_HIVEMQ_URL, {
    username: import.meta.env.VITE_HIVEMQ_USER,
    password: import.meta.env.VITE_HIVEMQ_PASS,
    reconnectPeriod: 1000,
  });

  client.on("connect", () => console.log("Conectado a HiveMQ Cloud"));
  client.on("reconnect", () => console.log("Reintentando conexión..."));
  client.on("error", (err) => console.error("Error MQTT:", err));

  return client;
}
