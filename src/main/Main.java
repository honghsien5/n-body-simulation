package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Particle{
    double x,y;
    double velocityX,velocityY;

    Particle(double x, double y){
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;

    }


}


public class Main {
    static void particlePrint(Particle[] bodies){
        for(int i = 0 ; i < bodies.length;i++){
            System.out.println(i+" is at "+bodies[i].x+" "+bodies[i].y);
        }
    }
    public static void main(String[] args) {
	// write your code here
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
        Particle[] bodies = new Particle[numBodies];
        String fileName = "init.txt";
        String line;
        double tempX,tempY;
        try{


            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for(int i = 0 ; i < numBodies ; i++){
                line = bufferedReader.readLine();
                String[] tempString = line.split(" ");
                bodies[i] = new Particle(Double.parseDouble(tempString[0]),Double.parseDouble(tempString[1]));
            }
            bufferedReader.close();
        }catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

//        particlePrint(bodies);

//        while(true){
//            estimatedTime = System.currentTimeMillis() - startTime;
//            long seconds = estimatedTime/1000;
//            long milliseconds = estimatedTime-(seconds*1000);
//            System.out.println("computation time:" + seconds +" seconds "+ milliseconds + " milliseconds");
//            if(estimatedTime%sizeTimeSteps==0 ){
//                System.out.println("computation time:" + seconds +" seconds "+ milliseconds + " milliseconds");
//            }
//        }


    }
}
