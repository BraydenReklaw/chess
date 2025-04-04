package dataaccess;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashSet;
import java.util.Set;

public class GameSession {
    private final Set<Session> clients = new HashSet<>();

    public void addClient(Session session, String authToken) {
        clients.add(session);
    }

    public Set<Session> getClients() {
        return clients;
    }

    public void removeClient(Session session, String authToken) {
        clients.remove(session);
    }
}
