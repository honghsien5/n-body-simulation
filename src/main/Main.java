package main;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello.");
        if ( args.length < 3){
            System.out.println("Need more than three arguments.");
            return;
        }
        int numBodies=Integer.parseInt(args[0]);
        int sizeBodies = Integer.parseInt(args[1]);
        int numTimeSteps = Integer.parseInt(args[2]);
        int sizeTimeSteps = Integer.parseInt(args[3]);
        long startTime = System.currentTimeMillis();
        long estimatedTime;
        while(true){
            estimatedTime = System.currentTimeMillis() - startTime;
            long seconds = estimatedTime/1000;
            long milliseconds = estimatedTime-(seconds*1000);
            System.out.println("computation time:" + seconds +" seconds "+ milliseconds + " milliseconds");
            if(estimatedTime%sizeTimeSteps==0 ){
                System.out.println("computation time:" + seconds +" seconds "+ milliseconds + " milliseconds");
            }
        }

    }
}
