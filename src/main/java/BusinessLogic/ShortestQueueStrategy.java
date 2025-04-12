package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    private Server server;
    @Override
    public void addTask(List<Server> servers, Task t) {
        // presupunem initial ca prima coada este cea mai scurta
        Server bestServer = servers.get(0);
        int minimSize = bestServer.getTaskCount();
        // parcurgem toate cozile si cautam serverul cu cele mai puține task-uri
        for (Server server : servers) {
            if (server.getTaskCount() < minimSize) {
                bestServer = server;
                minimSize = bestServer.getTaskCount();
            }
        }

        // adaugam taskul in coada aleasa
        bestServer.addTask(t);
        server=bestServer;
    }

    @Override
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}

