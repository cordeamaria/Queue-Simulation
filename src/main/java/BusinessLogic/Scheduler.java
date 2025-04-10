package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.ArrayList;


public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        // crez cozile si threadurile pt fiecare coada
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(i + 1); // fiecare server primește un numar unic
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        // schimbam strategia in functie de policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public synchronized void dispatchTask(Task t) {
        // folosim synchronized ca sa ne asiguram ca un singur thread poate trimite un task la servere la un moment dat.
        // protejeaza lista de servere si evita probleme cand mai multe threaduri ar putea adauga taskuri în același timp.

        // in functie de strategia aleasa,apelam addTask
        if (strategy != null) {
            strategy.addTask(servers, t);
        }
    }

    public List<Server> getServers() {

        return servers;
    }
}
