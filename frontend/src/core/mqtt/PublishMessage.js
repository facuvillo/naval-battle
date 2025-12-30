export const publishMessage = (client, topic, message) => {
  client.publish(topic, message, (err) => {
    if (err) {
      console.error("Error al publicar mensaje:", err);
    }
  });
};
