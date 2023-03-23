import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class DiskScheduler extends JFrame {
    private JLabel lblHead, lblTracks, lblRequests, lblAlgorithm, lblOutput;
    private JTextField txtHead, txtTracks, txtRequests;
    private JComboBox<String> cboAlgorithm;
    private JButton btnRun;
    private JTextArea txtOutput;

    public void runGUI(){
        //super("Disk Scheduler");

        // Initialize GUI components
        lblHead = new JLabel("Head position:");
        lblTracks = new JLabel("Number of tracks:");
        lblRequests = new JLabel("Requests:");
        lblAlgorithm = new JLabel("Algorithm:");
        lblOutput = new JLabel("Output:");

        txtHead = new JTextField(5);
        txtTracks = new JTextField(5);
        txtRequests = new JTextField(20);

        String[] algorithms = { "FCFS", "SSTF", "SCAN", "C-SCAN", "LOOK", "C-LOOK" };
        cboAlgorithm = new JComboBox<String>(algorithms);

        btnRun = new JButton("Run");
        txtOutput = new JTextArea(10, 20);
        txtOutput.setEditable(false);

        // Set layout and add components to frame
        Container c = getContentPane();
        c.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        c.add(lblHead, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        c.add(txtHead, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        c.add(lblTracks, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        c.add(txtTracks, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        c.add(lblRequests, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        c.add(txtRequests, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        c.add(lblAlgorithm, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        c.add(cboAlgorithm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        c.add(btnRun, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        c.add(lblOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        c.add(new JScrollPane(txtOutput), gbc);

        // Add event listener for Run button
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int head = Integer.parseInt(txtHead.getText());
                int tracks = Integer.parseInt(txtTracks.getText());
                String[] requestsString = txtRequests.getText().split("\\s+");
                int[] requests = new int[requestsString.length];

                for (int i = 0; i < requestsString.length; i++) {
                    requests[i] = Integer.parseInt(requestsString[i]);
                    }

            String algorithm = cboAlgorithm.getSelectedItem().toString();
            int[] movement;

            switch (algorithm) {
                case "FCFS":
                    movement = FCFS(head, requests);
                    break;
                case "SSTF":
                    movement = SSTF(head, requests);
                    break;
                case "SCAN":
                    movement = SCAN(head, tracks, requests);
                    break;
                case "C-SCAN":
                    movement = CSCAN(head, tracks, requests);
                    break;
                case "LOOK":
                    movement = LOOK(head, requests);
                    break;
                case "C-LOOK":
                    movement = CLOOK(head, requests);
                    break;
                default:
                    movement = new int[0];
                    break;
            }

            // Display output
            txtOutput.setText("");
            txtOutput.append("Head movement: ");
            for (int i = 0; i < movement.length; i++) {
                txtOutput.append(movement[i] + " ");
            }
            txtOutput.append("\nTotal head movement: " + getTotalMovement(movement) + " tracks.");
            plotMovement(movement, tracks);
        }
    });

    // Set frame properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 400);
    setLocationRelativeTo(null);
    setVisible(true);
}

private int[] FCFS(int head, int[] requests) {
    int[] movement = new int[requests.length];
    for (int i = 0; i < requests.length; i++) {
        movement[i] = Math.abs(head - requests[i]);
        head = requests[i];
    }
    return movement;
}

private int[] SSTF(int head, int[] requests) {
    int[] movement = new int[requests.length];
    boolean[] visited = new boolean[requests.length];
    int minIndex, minDistance;

    for (int i = 0; i < requests.length; i++) {
        minDistance = Integer.MAX_VALUE;
        minIndex = -1;

        for (int j = 0; j < requests.length; j++) {
            if (!visited[j]) {
                int distance = Math.abs(head - requests[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = j;
                }
            }
        }

        visited[minIndex] = true;
        movement[i] = minDistance;
        head = requests[minIndex];
    }

    return movement;
}

private int[] SCAN(int head, int tracks, int[] requests) {
    int[] movement = new int[requests.length + 2];
    int[] tmp = new int[requests.length];
    int index = 0;

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] < head) {
            tmp[index++] = requests[i];
        }
    }

    tmp[index++] = 0;
    for (int i = 0; i < requests.length; i++) {
        if (requests[i] >= head) {
            tmp[index++] = requests[i];
        }
    }
    tmp[index++] = tracks - 1;

    // Run SSTF on the sorted array
    movement = SSTF(head, tmp);
    return movement;
}

private int[] CSCAN(int head, int tracks, int[] requests) {
    int[] movement = new int[requests.length + 2];
    int[] tmp = new int[requests.length];
    int index = 0;

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] < head) {
            tmp[index++] = requests[i];
        }
    }

    tmp[index++] = 0;
        tmp[index++] = tracks - 1;
    for (int i = 0; i < requests.length; i++) {
        if (requests[i] >= head) {
            tmp[index++] = requests[i];
        }
    }

    // Run SSTF on the sorted array
    movement = SSTF(head, tmp);
    return movement;
}

private int[] LOOK(int head, int[] requests) {
    int[] movement = new int[requests.length];
    int[] tmp = new int[requests.length];
    int index = 0;

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] < head) {
            tmp[index++] = requests[i];
        }
    }

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] >= head) {
            tmp[index++] = requests[i];
        }
    }

    // Run SSTF on the sorted array
    movement = SSTF(head, tmp);
    return movement;
}

private int[] CLOOK(int head, int[] requests) {
    int[] movement = new int[requests.length];
    int[] tmp = new int[requests.length];
    int index = 0;

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] < head) {
            tmp[index++] = requests[i];
        }
    }

    for (int i = 0; i < requests.length; i++) {
        if (requests[i] >= head) {
            tmp[index++] = requests[i];
        }
    }

    // Run SSTF on the sorted array
    movement = SSTF(head, tmp);
    return movement;
}

private int getTotalMovement(int[] movement) {
    int total = 0;
    for (int i = 0; i < movement.length; i++) {
        total += movement[i];
    }
    return total;
}

private void plotMovement(int[] movement, int tracks) {
    JFrame plotFrame = new JFrame();
    plotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    plotFrame.setSize(400, 400);
    plotFrame.setLocationRelativeTo(null);

    JPanel plotPanel = new JPanel();
    plotPanel.setPreferredSize(new Dimension(400, 350));

    int[] x = new int[movement.length];
    int[] y = new int[movement.length];

    // Calculate the x and y coordinates
    int currentPosition = 0;
    for (int i = 0; i < movement.length; i++) {
        x[i] = i * 350 / movement.length;
        currentPosition += movement[i];
        y[i] = 350 - currentPosition * 350 / tracks;
    }

    plotPanel.add(new JLabel(new ImageIcon(createPlot(x, y, movement.length, tracks))));

    plotFrame.add(plotPanel);
    plotFrame.setVisible(true);
}

private BufferedImage createPlot(int[] x, int[] y, int length, int tracks) {
    BufferedImage plot = new BufferedImage(400, 350, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = plot.createGraphics();

    // Draw the x and y axes
    g2d.drawLine(50, 350, 350, 350); // x axis
    g2d.drawLine(50, 0, 50, 350); // y axis

    // Draw the ticks on the y axis
    for (int i = 0; i <= 10; i++) {
        int tickY = 350 - i * 35;
        g2d.drawLine(45, tickY, 50, tickY);
       
        g2d.drawString(Integer.toString(i * tracks / 10), 20, tickY + 5);
    }

    // Draw the ticks on the x axis
    for (int i = 0; i < length; i++) {
        int tickX = i * 350 / length;
        g2d.drawLine(tickX, 345, tickX, 350);
        g2d.drawString(Integer.toString(i), tickX, 365);
    }

    // Draw the line connecting the points
    g2d.setColor(Color.BLUE);
    for (int i = 0; i < length - 1; i++) {
        g2d.drawLine(x[i] + 50, y[i], x[i + 1] + 50, y[i + 1]);
    }

    g2d.dispose();
    return plot;
}

public static void main(String[] args) {
    DiskScheduler diskScheduler = new DiskScheduler();
    diskScheduler.runGUI();
}
}