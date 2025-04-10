package Model;

public class Task implements Comparable<Task> {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
       // return "Task :" + "arrivalTime=" + arrivalTime + ", serviceTime=" + serviceTime;
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }

    // Implementarea metodei compareTo
    @Override
    public int compareTo(Task otherTask) {
        // Compară obiectul curent cu alt Task pe baza arrivalTime
        return Integer.compare(this.arrivalTime, otherTask.arrivalTime);
    }
}