package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod; //pentru a evitate problme de concurenta, cand mai multe threaduri vor sa modifice valoarea
    int queueNumber;

   public Server(int queueNumber) {
       this.queueNumber = queueNumber; // inițializeaza numarul cozii
       this.tasks = new LinkedBlockingQueue<>();
       this.waitingPeriod = new AtomicInteger(0);
   }

    public void addTask(Task newTask) {
        //adaugam task in coada
        tasks.add(newTask); //verica
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public synchronized void run() {
        while (true) {
            //bucla while ruleaza pana cand este intrerupta .
            try {
                Task currentTask= tasks.peek();

                if (currentTask != null) {
                    Thread.sleep(1000L);
                    // pentru primul task din cada scadem la fiecare timp -1 din serviceTime
                    currentTask.setServiceTime(currentTask.getServiceTime() - 1);
                    if (waitingPeriod.get() > 0) {
                        waitingPeriod.addAndGet(-1);
                        // scadem si din suma totala de asteptare
                    }

                    if (currentTask.getServiceTime() == 0) {
                        tasks.remove(currentTask);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTaskCount() {
       return tasks.size();
    }
    public AtomicInteger getTotalWaitingTime() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }
    public boolean isEmpty()
    {
        return tasks.isEmpty();
    }
    @Override
    public String toString() {
        return "Server " + this.queueNumber + " | Tasks: " + tasks.size()+" "+ tasks + " | Waiting Period: " + getTotalWaitingTime();
    }
}