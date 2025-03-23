package ui;

import server.Server;
import java.io.IOException;

public class RunUI {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        var port = server.run(8810);
        System.out.println("Started UItest HTTP server on " + port);
        UI.preLogIn();
        server.stop();
    }

}
