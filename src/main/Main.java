package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Particle{
    double x,y;
    double velocityX,velocityY;
    double forceX,forceY;
    Particle(double x, double y){
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;
        forceX = 0;
        forceY = 0;
    }


}


public class Main {
    static double G = 6.67*Math.pow(10,-11);
    static double mass = 1;
    static void particlePrint(Particle[] bodies){
        for(int i = 0 ; i < bodies.length;i++){
            System.out.println(i+" is at "+bodies[i].x+" "+bodies[i].y);
        }
    }

    static void calculateForces(Particle[] bodies){
        double distance, magnitude;
        Particle direction;

        for(int i = 0; i < bodies.length-1;i++){
            for(int j = i+1; j < bodies.length ; j++){
                distance = Math.sqrt(Math.pow((bodies[i].x-bodies[j].x),2) + Math.power((bodies[i].y-bodies[j].y),2));
                magnitude = (G*1*1)/Math.pow(distance,2);
                direction = new Particle(bodies[j].x-bodies[i].x,bodies[j].y-bodies[i].y);
                bodies[i].forceX = bodies[i].forceX + magnitude*direction.x/distance;
                bodies[j].forceX = bodies[j].forceX - magnitude*direction.x/distance;
                bodies[i].forceY = bodies[i].forceY + magnitude*direction.y/distance;
                bodies[j].forceY = bodies[j].forceY - magnitude*direction.y/distance;

            }
        }
    }

    static void moveBodies(Particle[] bodies, double deltaT){
        Particle deltaV;
        Particle deltaP;
        for(int i = 0 ; i<n;i++){
            deltaV = new Particle(bodies[i].forceX/1 * deltaT, bodies[i].forceY/1 * deltaT);
            deltaP = new Particle(bodies[i].velocityX+deltaV.x/2)*deltaT, bodies[i].velicotyY+deltaV..y/2)*deltaT);

            bodies[i].velocityX = bodies[i].velocityX + deltaV.x;
            bodies[i].velocityY = bodies[i].velocityY + deltaV.y;
            bodies[i].x = bodies[i].x + deltaP.x;
            bodies[i].y = bodies[i].y + deltaP.y;
            bodies[i].forceX = 0;
            bodies[i].forceY= 0;
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

        //input file with init location
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
        }catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }


        for(int i = 0 ; i < numTimeSteps;i++){

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
