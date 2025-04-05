package ui;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SocketCommunicator extends Endpoint{

    public Session session;
    private Gson gson = new Gson();
    private ServerMessageObserver observer;

    public SocketCommunicator(int port) {
        try {
            URI uri =  new URI("ws://localhost:" + String.valueOf(port) + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println("Message recieved");
                    handleMessage(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            System.err.println("Failed to connect to WebSocket server: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void setObserver(ServerMessageObserver observer) {
        this.observer = observer;
    }

    private void handleMessage(String message) {
        System.out.println("Message recieved");
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        if (observer != null) {
            observer.notify(serverMessage);
        } else {
            System.err.println("Observer is not initialized");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendMessage(UserGameCommand command) throws IOException {
        String message = gson.toJson(command);
        session.getBasicRemote().sendText(message);
    }
}
