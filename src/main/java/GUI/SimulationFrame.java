package GUI;

import BusinessLogic.Scheduler;
import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import Model.Server;
import Model.Task;
import Utils.Storage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    private JTextArea textArea;
    private JTextField timeLimit = new JTextField(5);
    private JTextField maxArrivalTime = new JTextField(5);
    private JTextField minArrivalTime = new JTextField(5);
    private JTextField maxServiceTime = new JTextField(5);
    private JTextField minServiceTime = new JTextField(5);
    private JTextField numberOfServers = new JTextField(5);
    private JTextField numberOfTasks = new JTextField(5);
    String[] optiuni = {"SHORTEST_TIME", "SHORTEST_QUEUE"};
    JComboBox<String> policyComboBox = new JComboBox<>(optiuni);
    public JButton startButton = new JButton("Start");

    private Animation visualizerPanel = new Animation();

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Panel pentru inputuri
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));

        inputPanel.add(new JLabel("Number of Clients:"));
        inputPanel.add(numberOfTasks);
        inputPanel.add(new JLabel("Number of Servers:"));
        inputPanel.add(numberOfServers);
        inputPanel.add(new JLabel("Time Limit:"));
        inputPanel.add(timeLimit);
        inputPanel.add(new JLabel("Min Arrival Time:"));
        inputPanel.add(minArrivalTime);
        inputPanel.add(new JLabel("Max Arrival Time:"));
        inputPanel.add(maxArrivalTime);
        inputPanel.add(new JLabel("Min Service Time:"));
        inputPanel.add(minServiceTime);
        inputPanel.add(new JLabel("Max Service Time:"));
        inputPanel.add(maxServiceTime);
        inputPanel.add(new JLabel("Policy:"));
        inputPanel.add(policyComboBox);
        inputPanel.add(new JLabel(""));
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);

        // 🔹 Zona de output text cu scroll
        textArea = new JTextArea(12, 60);
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Simulation Log"));
        add(textScrollPane, BorderLayout.CENTER);

        // 🔹 Panelul pentru vizualizare cozi
        visualizerPanel.setPreferredSize(new Dimension(800, 250));
        visualizerPanel.setBorder(BorderFactory.createTitledBorder("Queue Visualization"));
        add(visualizerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }



    public void update(int currentTime, List<Task> tasks, List<Server> servers) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(currentTime).append("\n");
        int i=0;
        sb.append("Waiting clients:\n");
        for (Task t : tasks) {
            i++;
            sb.append(t).append(",");
            if(i==10) {
                sb.append("\n");
                i=0;
            }
        }

        sb.append("\nServers:\n");
        for (Server s : servers) {
            sb.append(s).append("\n");
        }

        textArea.setText(sb.toString());
        visualizerPanel.updateServers(servers, tasks);
        Storage.writeLog(sb.toString(), "simulation_log."+ getNumberOfServers()+"_"+getPolicyComboBox()+"txt");  //slavam in fisier

    }
    public void lastUpdate(String finaltime, List<Task> tasks, List<Server> servers, int peakHour,double averageWaitingTime,double averageServiceTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(finaltime).append("\n");
        int i=0;
        sb.append("Waiting clients:\n");
        for (Task t : tasks) {
            i++;
            sb.append(t).append(",");
            if(i==10) {
                sb.append("\n");
                i=0;
            }
        }

        sb.append("\nServers:\n");
        for (Server s : servers) {
            sb.append(s).append("\n");
        }
        sb.append("\n");
        sb.append("Peak Hour: ").append(peakHour).append("\n");
        sb.append("Average Waiting Time: ").append(String.format("%.2f", averageWaitingTime)).append("\n");
        sb.append("Average Service Time: ").append(String.format("%.2f", averageServiceTime)).append("\n");
        textArea.setText(sb.toString());
        Storage.writeLog(sb.toString(), "simulation_log."+ getNumberOfServers()+"_"+getPolicyComboBox()+"txt");  //slavam in fisier

    }

    public void displayResults(Scheduler scheduler) {
        JOptionPane.showMessageDialog(this, "Simulation finished!");
        System.exit(0);
    }


    public SelectionPolicy getPolicyComboBox() {
        String selected = (String) policyComboBox.getSelectedItem();
        return SelectionPolicy.valueOf(selected);
    }

    public void setPolicyComboBox(JComboBox<String> policyComboBox) {
        this.policyComboBox = policyComboBox;
    }

    public int getTimeLimit() {
        return Integer.parseInt(timeLimit.getText());
    }

    public void setTimeLimit(JTextField timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMaxArrivalTime() {
        return Integer.parseInt(maxArrivalTime.getText());
    }

    public void setMaxArrivalTime(JTextField maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public int getMinArrivalTime() {
        return Integer.parseInt(minArrivalTime.getText());
    }

    public void setMinArrivalTime(JTextField minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public int getMaxServiceTime() {
        return Integer.parseInt(maxServiceTime.getText());
    }

    public void setMaxServiceTime(JTextField maxServiceTime) {
        this.maxServiceTime = maxServiceTime;
    }

    public int getMinServiceTime() {
        return Integer.parseInt(minServiceTime.getText());
    }

    public void setMinServiceTime(JTextField minServiceTime) {
        this.minServiceTime = minServiceTime;
    }

    public int getNumberOfServers() {
        return Integer.parseInt(numberOfServers.getText());
    }

    public void setNumberOfServers(JTextField numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public int getNumberOfTasks() {
        return Integer.parseInt(numberOfTasks.getText());
    }

    public void setNumberOfTasks(JTextField numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }


    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();

        frame.startButton.addActionListener(e -> {
            SimulationManager manager = new SimulationManager(frame);
            Thread t = new Thread(manager);
            t.start();
        });
    }

}
