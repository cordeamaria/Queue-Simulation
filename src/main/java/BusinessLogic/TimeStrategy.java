package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    private Server server;
    @Override
    public void addTask(List<Server> servers, Task t) {
        // presupunem initial ca primul server/coada are cel mai mic timp total de procesare
        Server bestServer = servers.get(0);
        int minProcessingTime = bestServer.getTotalWaitingTime().get();

        // parcurgem lista de servere pentru a gasi serverul cu cel mai mic timp total de procesare
        for (Server server : servers) {
            int serverProcessingTime = server.getTotalWaitingTime().get();

            // daca acest server are un timp total mai mic decat cel curent minim, il selectam ca fiind cel mai bun
            if (serverProcessingTime < minProcessingTime) {
                bestServer = server;  // actualizam coada optima
                minProcessingTime = serverProcessingTime;
            }
        }

        // adaugam task-ul in coada cu cel cu cel mai mic timp total de procesare
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
