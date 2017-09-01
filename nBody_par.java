
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.concurrent.Semaphore;

public class nBody_par extends JPanel{
    // Global variables
    public static Particle_par[] bodies;
    public static double forces[][][];
    public static int arrive[];
    public static int collisionCount = 0;
    public static int numThreadBodies = 0;
    public static int sizeTimeSteps, numTimeSteps, numWorkers;
    public static JLabel timeStepLabel;
    public static boolean graphicSwitch;
    public static Semaphore mutex;

    //draw a circle that is centered around coordinate x and y
    public void drawCenteredCircle(Graphics2D g, double x, double y, double r) {
        x = x-(r/2);
        y = y-(r/2);
        Ellipse2D.Double shape = new Ellipse2D.Double(x, y, 2*r, 2*r);
        g.fill(shape);
    }
    //responsible for painting the frame
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);

        Graphics2D g = (Graphics2D) gg;

    /* Enable anti-aliasing */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int baseX = 150;
        int baseY = 100;
    /* Construct a shape and draw it */
        for(int i = 0 ; i < bodies.length;i++){
            drawCenteredCircle(g, baseX+100+bodies[i].x,baseY+100-bodies[i].y, bodies[i].radius);
        }
        g.drawRect(baseX,baseY,200,200);
        //drawCenteredCircle(g, 300+50, 300+50,.5);


    }
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        final JLabel timeStepLabel = new JLabel("", JLabel.CENTER);
        //check for the correct amount of arguments
        if ( args.length < 4){
            System.out.println("Need four or more arguments.");
            return;
        }
        if( args.length > 6){
            System.out.println("Max argument is 6");
            return;
        }

        // Local variables

        mutex = new Semaphore(1);

        // Command-line arguments
        numWorkers = Integer.parseInt(args[0]);
        int numBodies=Integer.parseInt(args[1]);
        if(numBodies > 10){
            System.out.println("Cannot process more than 10 bodies due to the init file constraint");
            return;
        }
        double radiusBodies = Double.parseDouble(args[2]);
        numTimeSteps = Integer.parseInt(args[3]);

        if(args.length < 5){
            sizeTimeSteps = 500;
        }else{
            sizeTimeSteps = Integer.parseInt(args[4]);
        }
        if(args.length < 6){
            graphicSwitch = false;
        }else{
            graphicSwitch= Boolean.parseBoolean(args[5]);
        }


        bodies = new Particle_par[numBodies];
        forces = new double[numWorkers][numBodies][2];
        arrive = new int[numWorkers];
        numThreadBodies = numBodies / numWorkers;
        String fileName = "init.txt";
        String line;

        for (int i = 0; i < numWorkers; i++){
            for (int j = 0; j < numBodies; j++){
                forces[i][j][0] = 0;
                forces[i][j][1] = 0;
            }
        }
        //input file with init location
        try{

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for(int i = 0 ; i < numBodies ; i++){
                line = bufferedReader.readLine();
                String[] tempString = line.split(" ");
                bodies[i] = new Particle_par(i,Double.parseDouble(tempString[0]),Double.parseDouble(tempString[1]),radiusBodies);
            }
            bufferedReader.close();
        }catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

        //windows configuration
        if(graphicSwitch) {
            frame.setTitle("Particle world");
            frame.setSize(500, 500);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            timeStepLabel.setText("TimeStep : ");
            timeStepLabel.setVerticalTextPosition(JLabel.TOP);
            timeStepLabel.setHorizontalTextPosition(JLabel.CENTER);
            frame.add(timeStepLabel, BorderLayout.NORTH);

            final JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, sizeTimeSteps);
            speedSlider.setMajorTickSpacing(100);
            speedSlider.setMinorTickSpacing(10);
            speedSlider.setPaintLabels(true);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintTrack(true);
            speedSlider.setAutoscrolls(true);
            speedSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    sizeTimeSteps = speedSlider.getValue();
                    frame.repaint();
                }
            });
            frame.add(speedSlider, BorderLayout.SOUTH);
            Container contentPane = frame.getContentPane();
            contentPane.add(new nBody_par());
            frame.setVisible(true);
        }

        //Initialize the worker
        Worker[] worker = new Worker[numWorkers];
        long startTime = System.currentTimeMillis(); //start counting time
        long estimatedTime;
        //Start the worker class and run them
        for (int i = 0; i < numWorkers; i++){
            worker[i] = new Worker(i,frame,timeStepLabel);
            worker[i].start();
        }


        for (int i = 0; i < numWorkers; i++){
            try {
                worker[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        long seconds = estimatedTime/1000;
        long milliseconds = estimatedTime-(seconds*1000);
        System.out.println("computation time:\t" + seconds +" seconds\t"+ milliseconds + " milliseconds");
        System.out.println("collision count:\t" + collisionCount);

        try{
            PrintWriter writer = new PrintWriter("output_parallel.txt", "UTF-8");
            //continue the operation for time steps
            for(int i = 0 ; i < bodies.length;i++){
                writer.println("Particle " + bodies[i].id + ":");
                writer.println("Position X:\t" + bodies[i].x + "\t\tPosition Y:\t" + bodies[i].y);
                writer.println("Velocity X:\t" + bodies[i].velocityX + "\tVelocity Y:\t" + bodies[i].velocityY);
                writer.println();
            }

            writer.close();
        }catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            "output.txt" + "'");
        }catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + "output.txt" + "'");
        }
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        return;


    }
}
