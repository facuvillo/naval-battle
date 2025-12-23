package org.batallanaval.backend.config;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
public class MqttClientConfig {

    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.port}")
    private int port;

    @Bean
    public Mqtt5Client mqttClient() {
        return Mqtt5Client.builder()
                .identifier("Battle-Ship Server")
                .serverHost(host)
                .automaticReconnectWithDefaultConfig()
                .serverPort(port)
                .sslWithDefaultConfig()
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connectMqtt(ApplicationReadyEvent event) {
        // TODO Revisar funcionamiento
        Mqtt5Client client = event.getApplicationContext().getBean(Mqtt5Client.class);
        try {
            client.toBlocking().connectWith()
                    .simpleAuth()
                    .username(username)
                    .password(password.getBytes(StandardCharsets.UTF_8))
                    .applySimpleAuth()
                    .willPublish()
                    .topic("battleship/status")
                    .payload("Server finish".getBytes())
                    .applyWillPublish()
                    .send();

            client.toBlocking().publishWith()
                    .topic("battleship/status")
                    .payload("Server running".getBytes())
                    .send();

            System.out.println("Conectado a HiveMQ Cloud como servidor");
        } catch (Exception e) {
            System.err.println("Error al conectar con MQTT: " + e.getMessage());
        }
    }
}
