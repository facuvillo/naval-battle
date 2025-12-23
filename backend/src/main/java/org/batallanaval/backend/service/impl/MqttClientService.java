package org.batallanaval.backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.batallanaval.backend.persistence.entity.gameLogic.*;
import org.batallanaval.backend.util.dto.WinnerData;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MqttClientService {

    private final Mqtt5Client connection;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GameManager gameManager;

    // TODO Fijarse si se queda
    private final ConcurrentMap<UUID, String> activeTopics = new ConcurrentHashMap<>();

    public MqttClientService(Mqtt5Client connection, GameManager gameManager, ContentNegotiatingViewResolver contentNegotiatingViewResolver) {
        this.connection = connection;
        this.gameManager = gameManager;
    }

    public String startGame(Player player) {
        GameLogic game = gameManager.createGamelogic(player);
        String topicBase = "battleship/games/" + game.getUuid();

        subscribeGameTopics(topicBase);

        return topicBase;
    }

    public String joinGame(String playerId, String gameId) {
        String topicBase = gameManager.joinGame(playerId, gameId);
        String message = "{\"command\":\"CONNECTED\",\"playerId\":\""+playerId+"\",\"gameId\":\""+gameId+"\"}";
        sendMessage(topicBase+"/status", message);
        return topicBase;
    }

    public void subscribeGameTopics(String topicBase){
        suscribeTopics(topicBase + "/shot");
        suscribeTopics(topicBase + "/ships");
        suscribeTopics(topicBase + "/chat");
        suscribeTopics(topicBase + "/status");
    }

    public void suscribeTopics(String topic){
        connection.toAsync().subscribeWith()
                .topicFilter(topic)
                .callback(publish -> {
                    byte[] payloadBytes = publish.getPayloadAsBytes();
                    String payloadStr = new String(payloadBytes);
                    handleMessage(publish.getTopic().toString(), payloadStr);
                }).send();
    }

    public void handleMessage(String topic, String payload){
        try {
            JsonNode json = objectMapper.readTree(payload);

            if (json.has("command")) {
                String command = json.get("command").asText();

                switch (command) {
                    case "CONNECTED" -> processConnected (json, topic);
                    case "SHOT" -> processShot(json, topic);
                    case "SETSHIP" -> processSetShips(json, topic);
                    case "CHAT" -> processChat(json, topic);
                    case "GIVE_UP"  -> processGiveUp(json, topic);
                    default -> System.out.println("Comando desconocido: " + command);
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private void processChat(JsonNode json, String topic) {

        System.out.println("CHAT");
    }

    private void processConnected (JsonNode json, String topic) {
        gameManager.getGameLogic(UUID.fromString(json.get("gameId").asText())).addNewPlayer(new Player(UUID.fromString(json.get("playerId").asText()), "jugador2"));

    }

    private void processSetShips(JsonNode json, String topic) {
        String gameId = topic.split("/")[2];
        String playerId = json.get("playerId").asText();
        List<Ship> ships = new ArrayList<>();

        JsonNode shipsNode = json.get("ships");
        if (shipsNode != null && shipsNode.isArray()) {
            for (JsonNode shipNode : shipsNode) {
                String typeStr = shipNode.get("type").asText().toUpperCase();
                ShipType type = ShipType.valueOf(typeStr);

                List<Coordinate> positions = new ArrayList<>();
                JsonNode positionsNode = shipNode.get("positions");
                if (positionsNode != null && positionsNode.isArray()) {
                    for (JsonNode posNode : positionsNode) {
                        int x = posNode.get("x").asInt();
                        int y = posNode.get("y").asInt();
                        positions.add(new Coordinate(x, y));
                    }
                }

                ships.add(new Ship(type, positions));
            }
        }

        gameManager.setShips(gameId, ships, playerId);
    }

    private void processShot(JsonNode json, String topic) throws JsonProcessingException {
        String gameId = topic.split("/")[2];

        Player currentPlayer = gameManager.getGameLogic(UUID.fromString(gameId)).getCurrentPlayer();
        sendMessage("battleship/games/"+gameId+"/status", "{\"command\":\"CHANGE_TURN\",\"currentPlayerId\":\""+currentPlayer.getUuid()+"\"}");

        Coordinate coordinate = Coordinate.builder()
                .x(json.get("position").get("x").asInt())
                .y(json.get("position").get("y").asInt())
                .build();
        boolean isHit = gameManager.processShot(gameId,  coordinate);
        String positionString = "\"position\":{\"x\":"+coordinate.getX()+",\"y\":"+coordinate.getY()+"}";

        WinnerData winnerData = gameManager.processGameOver(gameId);
        if (winnerData != null) {
            String winnerJson = objectMapper.writeValueAsString(winnerData);
            sendMessage("battleship/games/"+gameId+"/status", winnerJson);
            return;
        }

        if (isHit) {
            // TODO Crear un nuevo metodo con todos lo comados
            sendMessage("battleship/games/"+gameId+"/status", "{\"command\":\"SHOT_RESULT\",\"result\":\"HIT\","+positionString+",\"playerId\":\""+currentPlayer.getUuid()+"\"}");
        }else {
            sendMessage("battleship/games/"+gameId+"/status", "{\"command\":\"SHOT_RESULT\",\"result\":\"WATER\","+positionString+",\"playerId\":\""+currentPlayer.getUuid()+"\"}");
        }
    }

    public void sendMessage(String topic, String message){
        connection.toBlocking().publishWith()
                .topic(topic)
                .payload(message.getBytes())
                .retain(false).send();
    }

    public void sendInvitation(String playerId, UUID gameId, String inviterName) {
        String topic = "battleship/players/" + playerId + "/invite";

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("command", "INVITATION");
        payloadMap.put("gameId", gameId.toString());
        payloadMap.put("inviterName", inviterName);

        try {
            String message = objectMapper.writeValueAsString(payloadMap);
            sendMessage(topic, message);
            System.out.println("Invitación enviada a " + playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processGiveUp(JsonNode json, String topic) throws JsonProcessingException {
        System.out.println("CHECK 1");
        String gameId = topic.split("/")[2];
        Player winner = gameManager.getGameLogic(UUID.fromString(gameId)).getCurrentPlayer();
        if (!winner.getUuid().toString().equals(json.get("playerId").asText())) {
            gameManager.getGameLogic(UUID.fromString(gameId)).changeCurrentPlayerIndex();
            winner = gameManager.getGameLogic(UUID.fromString(gameId)).getCurrentPlayer();
        }

        WinnerData winnerData = WinnerData.builder()
                .command("GIVE_UP_FINISHED")
                .winnerId(winner.getUuid().toString())
                .winnerName(winner.getUsername())
                .build();
        String winnerJson = objectMapper.writeValueAsString(winnerData);
        sendMessage("battleship/games/"+gameId+"/status", winnerJson);
        gameManager.removeGame(gameId);
    }
}
