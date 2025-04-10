package BusinessLogic;

import java.util.ArrayList;
import java.util.Random;

import GUI.SimulationFrame;
import Model.Task;


public class SimulationManager implements Runnable {
    // Spune că sunt input, ar trebui să le primesc din interfață
    public int timeLimit = 60;
    public int maxArrivleTime=15;
    public int minArrivalTime=2;
    public int maxServiceTime = 4;
    public int minServiceTime = 2;
    public int numberOfServers = 2;
    public int numberOfClients = 10;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME; // selectez metoda de sortare
    private Scheduler scheduler;
    private SimulationFrame frame;

    private ArrayList<Task> generatedTasks;

    public SimulationManager() {
        // intializam scheduler-ul cu numarul maxim de servere si clienti per server
        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        // frame pentru a afisa rezultatele
       frame = new SimulationFrame();

        // aici stocam taskurile generate
        generatedTasks = new ArrayList<>();
    }

    // genram taskuri random
    private void generateNRandomTasks() {
        Random rand = new Random();

        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = rand.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime; // un numar random intre 0 si maxProcessingTime-minProcessingTime,punem plus 1 pt ca retruneaza rand intr 0 si x-1
            //+minProcessingTime ca sa fie intre min si max
            int arrivalTime = rand.nextInt(maxArrivleTime-minArrivalTime+1) + minArrivalTime;// intre 0 si timeLimit

            Task task = new Task(i + 1, arrivalTime, processingTime); //creem taskul
            generatedTasks.add(task);
        }

        // sortam taskurile dupa arrival time
        generatedTasks.sort(null);  //folosim compare to
    }


    @Override
    public void run() {

        int currentTime = 0;
        StringBuilder logData = new StringBuilder();
        // generam taskurile random
        generateNRandomTasks();
         while(true) {
             while (currentTime <= timeLimit) {
                 // cautam taskurile care au  arrivalTime == currentTime
                 for (Task task : new ArrayList<>(generatedTasks)) {
                     if (task.getArrivalTime() == currentTime) {
                         // vedem in ce coada punem
                         scheduler.dispatchTask(task);
                         generatedTasks.remove(task); // dupa ce il punem in coada il stergem
                     }
                 }

                 // actualizam frame
                frame.update(currentTime, generatedTasks, scheduler.getServers());


                 // asteptam 1 sec
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 // trecem la urmatorul time
                 currentTime++;
             }


        frame.displayResults(scheduler);
             }
    }

    public static void main(String[] args) {
        SimulationManager simManager = new SimulationManager();
        Thread simulationThread = new Thread(simManager);
        simulationThread.start();
    }
}
