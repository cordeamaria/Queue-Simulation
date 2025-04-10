package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;  //serverul ia taskuri din ea pe rand si așteapta automat daca e goala ,  opreste temporar threadul serverului pana apare un task în coada
    private AtomicInteger waitingPeriod; //pentru a evitate problme de concurenta, cand mai multe threaduri vor sa modifice valoarea
    // un tip de operatie care ori se executa ori nu se executa
    int queueNumber;
   public Server(int queueNumber) {
       this.queueNumber = queueNumber; // inițializeaza numarul cozii
       this.tasks = new LinkedBlockingQueue<>();
       this.waitingPeriod = new AtomicInteger(0);
   }

    public void addTask(Task newTask) {
        //adaugam task in coada
        tasks.add(newTask); //verica
        waitingPeriod.addAndGet(newTask.getServiceTime()); // adaugs si returneaza noua valoare.
    }

    public synchronized void run() {
        while (true) {
            //bucla while ruleaza pana cand este intrerupta - trece automat la următorul task prin tasks.take().
            try {
                Task currentTask= tasks.peek();
               // Task currentTask = tasks.poll(1, TimeUnit.SECONDS); //aici nu e bine,strege taskul nu il pune in coada,thread pool sa salvam,sa ne gandim cum oprim thredurile de servere
                if(currentTask != null) {
                    Thread.sleep(1000L); // prin getServiceTime luam cat dureaza un task si inmulțim cu 1000 pentru cs sleep primeste timpul în milisecunde, iar serviceTime e în secunde
                    if(currentTask.getServiceTime()==1)
                    {
                        tasks.remove(currentTask);
                    }
                    else{
                        currentTask.setServiceTime(currentTask.getServiceTime()-1);
                    }
                       // scadem timpul task-ului din waitingPeriod, pentru ca el nu mai contribuie la timpul de așteptare al cozii—acum este în execuție
                }
              if(waitingPeriod.get()>0)  waitingPeriod.addAndGet(-1);
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
        return waitingPeriod; // Returnează timpul total de procesare al tuturor task-urilor din coada respectiva
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

    @Override
    public String toString() {
        return "Server " + this.queueNumber + " | Tasks: " + tasks.size()+" "+ tasks + " | Waiting Period: " + getTotalWaitingTime();
    }
}