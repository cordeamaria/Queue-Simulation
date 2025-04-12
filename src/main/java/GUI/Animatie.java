package GUI;

import Model.Server;
import Model.Task;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class Animatie extends JPanel {
    private List<Task> tasks;
    private List<Server> servers;
    private int panelWidth;
    private int panelHeight;

    public Animatie(List<Task> tasks, List<Server> servers) {
        this.tasks = tasks;
        this.servers = servers;
        this.setPreferredSize(new Dimension(600, 200)); // Dimensiunea poate fi ajustată după necesitate
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Setează fontul pentru text
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Desenează fiecare task pe serverul corespunzător
        int xOffset = 10;
        for (Server server : servers) {
            int yOffset = 30 * (server.getQueueNumber() - 1);
            int taskCount = 0;

            // Desenează task-urile pentru server
            for (Task task : server.getTasks()) {
                // Calculați poziția X și lățimea pentru fiecare task
                int width = task.getServiceTime() * 20; // Poți ajusta factorul pentru dimensiune
                int x = xOffset + (taskCount * 30); // Lățimea fiecărei bare (în funcție de numărul de taskuri)

                g2d.setColor(Color.CYAN); // Poți schimba culoarea în funcție de starea task-ului
                g2d.fillRect(x, yOffset, width, 20); // Desenează barele pentru taskuri
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, yOffset, width, 20); // Desenează conturul barei

                // Adaugă ID-ul task-ului pe bara corespunzătoare
                g2d.drawString("ID: " + task.getId(), x + 5, yOffset + 15); // Textul
                taskCount++;
            }
            xOffset += 200; // Deplasare pe axa X pentru a crea un spațiu între cozi
        }
    }

    // Metodă pentru actualizarea listelor de taskuri și servere
    public void update(List<Task> tasks, List<Server> servers) {
        this.tasks = tasks;
        this.servers = servers;
        repaint();
    }
}
