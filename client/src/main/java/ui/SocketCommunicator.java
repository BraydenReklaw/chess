package ui;

import javax.websocket.*;
import java.io.IOException;

public class SocketCommunicator extends Endpoint{

    public Session session;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Recieved: " + message);
    }

    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
