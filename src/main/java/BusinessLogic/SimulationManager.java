package BusinessLogic;

import java.util.ArrayList;
import java.util.Random;

import GUI.SimulationFrame;
import Model.Task;


public class SimulationManager implements Runnable {
    // Spune că sunt input, ar trebui să le primesc din interfață
    public int timeLimit;
    public int maxArrivleTime;
    public int minArrivalTime;
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy; // selectez metoda de sortare
    private Scheduler scheduler;
    private SimulationFrame frame;

    private ArrayList<Task> generatedTasks;
    public void init() {
        this.timeLimit = frame.getTimeLimit();
        this.maxArrivleTime = frame.getMaxArrivalTime();
        this.minArrivalTime = frame.getMinArrivalTime();
        this.maxServiceTime = frame.getMaxServiceTime();
        this.minServiceTime = frame.getMinServiceTime();
        this.numberOfClients = frame.getNumberOfClients();
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
            int arrivalTime = rand.nextInt(maxArrivleTime-minArrivalTime+1) + minArrivalTime;// intre 0 si timeLimit

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
        int maxTasks =0; // aici compram in fiecare timp cate taskuri sunt in cozi
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
                         tasksWaiting++;
                         waiting = scheduler.dispatchTask(task); // acum e corect
                         if (currentTime + waiting > timeLimit) {
                             totalWaitingTime += timeLimit - currentTime;
                         } else {
                             totalWaitingTime += waiting;
                         }
                         if(currentTime + waiting+task.getServiceTime() <= timeLimit) {
                             totalServiceTime += task.getServiceTime();
                             tasksServed++;
                         }
                         generatedTasks.remove(task);
                     }

                 }
             totalTasks =scheduler.totalTasks();
             if(totalTasks > maxTasks) {
                 peakHour=currentTime;
                 maxTasks = totalTasks;
             }
                 scheduler.removeCompletedTasks();

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
        System.out.println("totalW: " + totalWaitingTime + ", TasksWaiting: " + tasksWaiting+"\n");
         System.out.println("totalS " + totalServiceTime + ", TasksServed: " + tasksServed+"\n");
        totalWaitingTime=(double)totalWaitingTime/(double)tasksWaiting; //calculam pe rand pentru ficare task cat are de stat in coada,daca depaseste timpul simulari audnam cat mai e pana la finalul simularii. impartim la cate taskuri au ajuns in cozi
        totalServiceTime=(double)totalServiceTime/(double)tasksServed;
       frame.lastUpdate("Final Time", generatedTasks, scheduler.getServers(),peakHour,totalWaitingTime,totalServiceTime);
        System.out.println("Simularea s-a încheiat!");
        System.out.println("Ora de vârf: " + peakHour);
        frame.displayResults(scheduler);
       // System.out.println("Timp mediu de așteptare: " + averageWaitingTime);
//        System.out.println("Timp mediu de procesare: " + averageServiceTime);
//        System.out.println("Cozile sunt goale la timpul " + currentTime);
    }
}
