package GUI;

import BusinessLogic.Scheduler;
import Model.Server;
import Model.Task;
import Utils.Storage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    private JTextArea textArea;

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        setVisible(true);
    }

    public void update(int currentTime, List<Task> tasks, List<Server> servers) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(currentTime).append("\n");

        sb.append("Waiting clients:\n");
        for (Task t : tasks) {
            sb.append(t).append("\n");
        }

        sb.append("\nServers:\n");
        for (Server s : servers) {
            sb.append(s).append("\n");
        }

        textArea.setText(sb.toString());
        Storage.writeLog(sb.toString(),"simulation_log.txt");  //slavam in fisier

    }

    public void displayResults(Scheduler scheduler) {
        JOptionPane.showMessageDialog(this, "Simulation finished!");
        System.exit(0);
    }
}
