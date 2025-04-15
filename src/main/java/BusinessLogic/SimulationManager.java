package BusinessLogic;

import java.util.ArrayList;
import java.util.Random;

import GUI.SimulationFrame;
import Model.Task;


public class SimulationManager implements Runnable {
    // Spune că sunt input, ar trebui să le primesc din interfață
    private int timeLimit;
    private int maxArrivleTime;
    private int minArrivalTime;
    private int maxServiceTime;
    private int minServiceTime;
    private int numberOfServers;
    private int numberOfClients;
    private SelectionPolicy selectionPolicy; // selectez metoda de sortare
    private Scheduler scheduler;
    private SimulationFrame frame;

    private ArrayList<Task> generatedTasks;
    public void init() {
        this.timeLimit = frame.getTimeLimit();
        this.maxArrivleTime = frame.getMaxArrivalTime();
        this.minArrivalTime = frame.getMinArrivalTime();
        this.maxServiceTime = frame.getMaxServiceTime();
        this.minServiceTime = frame.getMinServiceTime();
        this.numberOfClients = frame.getNumberOfTasks();
        this.numberOfServers = frame.getNumberOfServers();
        this.selectionPolicy = frame.getPolicyComboBox();

        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
    }


    public SimulationManager(SimulationFrame frame) {
        this.frame = frame;
        this.generatedTasks = new ArrayList<>();
    }


    // genram taskuri random
    private void generateNRandomTasks() {
        Random rand = new Random();

        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = rand.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime; // un numar random intre 0 si maxProcessingTime-minProcessingTime,punem plus 1 pt ca retruneaza rand intr 0 si x-1
            //+minProcessingTime ca sa fie intre min si max
            int arrivalTime = rand.nextInt(maxArrivleTime - minArrivalTime + 1) + minArrivalTime;// intre 0 si timeLimit

            Task task = new Task(i + 1, arrivalTime, processingTime); //creem taskul
            generatedTasks.add(task);
        }

        // sortam taskurile dupa arrival time
        generatedTasks.sort(null);  //folosim compare to
    }


    @Override
    public void run() {
        init();
        int currentTime = 0;

        int peakHour=0;
        int maxTasks =0;
        int totalTasks =0;

        double totalWaitingTime=0;
        int tasksWaiting=0;

        double totalServiceTime=0;
        int tasksServed=0;

        // generam taskurile random
        generateNRandomTasks();
         while(true) {

             if (currentTime > timeLimit || (generatedTasks.size() == 0  && scheduler.schedulerIsEmpty())) {
                 break;
             }

                 // cautam taskurile care au  arrivalTime == currentTime
                 for (Task task : new ArrayList<>(generatedTasks)) {
                     int waiting=0;
                     if (task.getArrivalTime() == currentTime) {
                         tasksWaiting++;  // numaram cate taskuri asteapta pe parcursul intregii simulari
                         waiting = scheduler.dispatchTask(task); // cand adugam un task functia returneaza cat va trebui sa astepte taskul respectiv pana ajunge in fata cozii
                         if (currentTime + waiting > timeLimit) {  // daca are mai mult de asteptat decat timpul simulrai va astepta pana se termina simularea,adica timeLimit-currentTime
                             totalWaitingTime += timeLimit - currentTime;
                         } else {
                             totalWaitingTime += waiting;
                         }
                         if(currentTime + waiting+task.getServiceTime() <= timeLimit) {
                             //calculam timpul total de servire pentru a face media,
                             // daca currentTime + cat trebuie sa astepte clientul pana este servit+timpul de servire sunt mai mici decat timpul simularii,
                             // luam in cacul taskul. Daca nu inseamna ca clientuk nu va fi servit
                             totalServiceTime += task.getServiceTime();
                             tasksServed++;   //cati clienti au fost serviti
                         }
                         generatedTasks.remove(task); //stergem din lista de taskuri generate
                     }

                 }
             totalTasks =scheduler.totalTasks(); // la fiecare timp calculam numarul de taskuri din toate cozile
             if(totalTasks > maxTasks) {
                 peakHour=currentTime;
                 maxTasks = totalTasks;
             }

                 // actualizam frame
                frame.update(currentTime, generatedTasks, scheduler.getServers());


                 // asteptam 1 sec
                //se simuleaza trecerea unui "timp real" de o secundă la fiecare pas
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 // trecem la urmatorul time
                 currentTime++;

             }
         //calculam average waiting time
        totalWaitingTime=totalWaitingTime/(double)tasksWaiting;
         //calculam average service time
        totalServiceTime=totalServiceTime/(double)tasksServed;
        //actualizam frame cu datele finale
       frame.lastUpdate("Final Time", generatedTasks, scheduler.getServers(),peakHour,totalWaitingTime,totalServiceTime);

    }


    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager(new SimulationFrame());
        simulationManager.frame.startSimulation();
    }
}
