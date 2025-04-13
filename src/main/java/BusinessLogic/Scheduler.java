package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.ArrayList;


public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private int totalWaitingTime=0;
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

    public synchronized int dispatchTask(Task t) {
        Server serverBest;
        int waitingTimeTask=0;
        // folosim synchronized ca sa ne asiguram ca un singur thread poate trimite un task la servere la un moment dat.
        // protejeaza lista de servere si evita probleme cand mai multe threaduri ar putea adauga taskuri în același timp.
        // in functie de strategia aleasa,apelam addTask
        if (strategy != null) {
            strategy.addTask(servers, t);
            serverBest = strategy.getServer();  // obține serverul selectat in care este introdus taskul

            if (serverBest != null) {
                waitingTimeTask = serverBest.getTotalWaitingTime().get() - t.getServiceTime(); // calculam pentru fiecare task adaugat cat va sta in coada,si anume ctimpul total de service a cozii minus serviceTime a taskului adaugat
            }
        }

        return waitingTimeTask;
    }

    public List<Server> getServers() {

        return servers;
    }
    public boolean schedulerIsEmpty() {
        for (Server server : servers) {
            if (server.isEmpty()==false || server.getTotalWaitingTime().get()>0) {
                return false;
            }
        }
        return true;
    }
    // metoda pentru a elimina task-urile care au serviceTime == 0
    public void removeCompletedTasks() {
        for (Server server : servers) {
            List<Task> toRemove = new ArrayList<>();
            for (Task task : server.getTasks()) {
                if (task.getServiceTime() <= 0) {
                    toRemove.add(task);
                }
            }
            server.getTasks().removeAll(toRemove);
        }
    }
    public int totalTasks()
    {
        int total=0;
        for (Server server : servers) {
                total+=server.getTaskCount();
        }
        return total;
    }

}
