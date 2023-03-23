# Implementation of Disk Scheduler in Java 
The code defines a Java Swing-based GUI application called "DiskScheduler", which simulates disk scheduling algorithms. The application contains a JFrame object that holds multiple GUI components such as labels, text fields, combo box, and a button. The main function runGUI() initializes all these components, sets their properties, and adds them to the JFrame object using GridBagLayout manager.

The application provides the following disk scheduling algorithms to choose from:

1. First Come First Serve (FCFS)
2. Shortest Seek Time First (SSTF)
3. SCAN
4. C-SCAN
5. LOOK
6. C-LOOK

When the user clicks on the "Run" button, the ActionListener attached to it gets triggered, which performs the following steps:

1. It reads the user inputs from the GUI components such as head position, number of tracks, disk requests, and selected algorithm.
2. It calls the corresponding disk scheduling algorithm method with the inputs.
3. It receives an array of head movements from the algorithm method.
4. It updates the output text area with the head movements and total head movement for the selected algorithm.
5. It plots the head movements on a graphical representation of the disk using the plotMovement() method.
The implementation of each disk scheduling algorithm is defined in separate methods such as FCFS(), SSTF(), SCAN(), etc. Each algorithm method takes the head position and disk requests as inputs, and returns an array of head movements based on that algorithm. These methods use different algorithms to determine the order in which disk requests are served.

The plotMovement() method takes the array of head movements and the number of tracks as inputs and generates a graphical representation of the disk that shows the head movements. It uses the Java AWT and Swing classes to create a BufferedImage object, which is then displayed in a separate JFrame.
