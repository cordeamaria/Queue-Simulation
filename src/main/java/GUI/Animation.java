package GUI;

import Model.Server;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Animation extends JPanel {
    // lista de servere care vor fi desenate
    private List<Server> servers;

    // lista de taskuri care asteapta sa fie trimise la servere
    private List<Task> waitingTasks;

    // metoda care se apeleaza pentru a actualiza starea animatiei
    public void updateServers(List<Server> servers, List<Task> waitingTasks) {
        this.servers = servers; // se actualizeaza serverele
        this.waitingTasks = waitingTasks; // se actualizeaza taskurile in asteptare
        repaint(); // redeseneaza componenta (apeleaza automat paintComponent)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (servers == null) return; // daca nu exista servere, nu desenam nimic

        Graphics2D g2d = (Graphics2D) g;

        // dimensiuni si pozitii pentru desen
        int serverWidth = 80;       // latimea unui server desenat
        int serverGap = 20;         // distanta dintre servere
        int blockHeight = 20;       // inaltimea unui task desenat ca bloc
        int baseY = getHeight() - 50; // pozitia de baza (de jos) unde incepem sa desenam,inaltimea ferestrei in care desenam - 50

        // desenam fiecare server ca un turn vertical de taskuri
        for (int i = 0; i < servers.size(); i++) {
            int x = 50 + i * (serverWidth + serverGap); // pozitia pe orizontala pentru fiecare server
            g2d.setColor(Color.BLACK);
            g2d.drawString("S" + (i + 1), x + 20, baseY + 20); // eticheta serverului (S1, S2, ...)

            // Ldam taskurile serverului curent si le desenam
            java.util.List<Task> tasks = new java.util.ArrayList<>(servers.get(i).getTasks());
            for (int j = 0; j < tasks.size(); j++) {
                Task task = tasks.get(j);
                // culoare variabila in functie de id-ul taskului
                g2d.setColor(new Color(100 + 10 * (task.getId() % 15), 150, 200));
                // deseneaza blocul (task-ul) ca un dreptunghi colorat
                g2d.fillRect(x, baseY - (j + 1) * blockHeight, serverWidth, blockHeight);
                // contur si text
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, baseY - (j + 1) * blockHeight, serverWidth, blockHeight);
                g2d.drawString("T" + task.getId(), x + 10, baseY - (j + 1) * blockHeight + 15);
            }
        }

        // desenam taskurile in asteptare
        if (waitingTasks != null) {
            // pozitia unde incepem sa desenam taskurile in asteptare
            int waitingX = 50 + servers.size() * (serverWidth + serverGap) + 30;
            int waitingY = baseY - 70;

            g2d.setColor(Color.BLACK);
            g2d.drawString("Waiting tasks:", waitingX, waitingY - 15);

            int tasksPerRow = 10; // cate taskuri pe rand
            int rowSpacing = 30;  // spatiul vertical intre randuri

            // desenam fiecare task in asteptare
            for (int i = 0; i < waitingTasks.size(); i++) {
                Task task = waitingTasks.get(i);

                int row = i / tasksPerRow; // linia curenta
                int col = i % tasksPerRow; // coloana curenta

                int x = waitingX + col * 35;
                int y = waitingY + row * rowSpacing;

                // desenam blocul colorat
                g2d.setColor(new Color(200, 100 + 10 * (task.getId() % 10), 100));
                g2d.fillRect(x, y, 30, blockHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, 30, blockHeight);
                g2d.drawString("T" + task.getId(), x + 5, y + 15);
            }
        }
    }
}
