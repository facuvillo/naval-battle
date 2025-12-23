export const subscribeTopic = (client, topic, messageHandler) => {
  client.subscribe(topic, (err) => {
    if (err) {
      console.error("Error al suscribirse al topic:", err);
    }
  });

  client.on("message", (receivedTopic, message) => {
    if (
      receivedTopic === topic ||
      receivedTopic.startsWith(topic.replace("#", ""))
    ) {
      messageHandler(receivedTopic, message.toString());
    }
  });
};
