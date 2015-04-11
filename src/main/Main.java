package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Particle{
    int id;
    double x,y;
    double velocityX,velocityY;
    double forceX,forceY;
    double radius;
    Particle(double x, double y){
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;
        forceX = 0;
        forceY = 0;
    }
    Particle(int id,double x, double y, double radius){
        this.id = id;
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;
        forceX = 0;
        forceY = 0;
        this.radius = radius;
    }
    boolean intersect(Particle another){

        return (Math.pow((another.x-x),2) + Math.pow((y-another.y),2)) <= Math.pow((radius+another.radius),2);

    }

}


public class Main {
    static double G = 6.67*Math.pow(10,-11);
    static double mass = 1;
    static int collisionCount = 0;
    static void printBodies(Particle[] bodies){
        for(int i = 0 ; i < bodies.length;i++){
            System.out.println(i+" is at "+bodies[i].x+" "+bodies[i].y);
        }
    }

    static void calculateForces(Particle[] bodies){
        double distance, magnitude;
        Particle direction;
        for(int i = 0; i < bodies.length-1;i++){
            for(int j = i+1; j < bodies.length ; j++){
                distance = Math.sqrt(Math.pow((bodies[i].x-bodies[j].x),2) + Math.pow((bodies[i].y-bodies[j].y),2));
                magnitude = (G*mass*mass)/Math.pow(distance,2);
                direction = new Particle(bodies[j].x-bodies[i].x,bodies[j].y-bodies[i].y);
                bodies[i].forceX = bodies[i].forceX + magnitude*direction.x/distance;
                bodies[j].forceX = bodies[j].forceX - magnitude*direction.x/distance;
                bodies[i].forceY = bodies[i].forceY + magnitude*direction.y/distance;
                bodies[j].forceY = bodies[j].forceY - magnitude*direction.y/distance;
//                System.out.println("Force 1: " + bodies[i].forceX +" " + bodies[i].forceY);
//                System.out.println("Force 2: " + bodies[j].forceX + " " + bodies[j].forceY);
            }
        }
    }

    static void moveBodies(Particle[] bodies, double deltaT){
        Particle deltaV;
        Particle deltaP;

        for(int i = 0 ; i < bodies.length;i++){
            deltaV = new Particle(bodies[i].forceX/mass * deltaT, bodies[i].forceY/mass * deltaT);
            deltaP = new Particle((bodies[i].velocityX+deltaV.x/2)*deltaT, (bodies[i].velocityY+deltaV.y/2)*deltaT);

            bodies[i].velocityX = bodies[i].velocityX + deltaV.x;
            bodies[i].velocityY = bodies[i].velocityY + deltaV.y;
            System.out.println("Velocity "+ i + ": " + bodies[i].velocityX +" " + bodies[i].velocityY);
            bodies[i].x = bodies[i].x + deltaP.x;
            bodies[i].y = bodies[i].y + deltaP.y;
            bodies[i].forceX = 0;
            bodies[i].forceY= 0;
        }
    }
    static void detectCollision(Particle[] bodies){
        for(int i = 0; i < bodies.length-1;i++){
            for(int j = i+1; j < bodies.length;j++){
                if(bodies[i].intersect(bodies[j])){
                    collisionCount++;
                    calculateCollision(bodies[i],bodies[j]);
                }
            }

        }
    }
    static void detectBoundary(Particle[] bodies){
        for(int i = 0; i < bodies.length;i++){
            double x = bodies[i].x;
            double y = bodies[i].y;
            if(x < -100 || x > 100){
                bodies[i].velocityX = -bodies[i].velocityX;
            }
            if( y < -100 || y > 100){
                bodies[i].velocityY = -bodies[i].velocityY;
            }

        }
    }
    static void calculateCollision(Particle p1, Particle p2){
        double x1  = p1.x;
        double y1  = p1.y;
        double v1x = p1.velocityX;
        double v1y = p1.velocityY;
        double x2  = p2.x;
        double y2  = p2.y;
        double v2x = p2.velocityX;
        double v2y = p2.velocityY;
        System.out.println("Collision between particle "+ p1.id +" and particle " + p2.id);
        System.out.println("Before:");
        System.out.println(p1.velocityX + " " + p1.velocityY);
        System.out.println(p2.velocityX + " " + p2.velocityY);
        p1.velocityX = (v2x*Math.pow(x2-x1,2) + v2y*(x2-x1)*(y2-y1) + v1x*Math.pow(y2-y1,2) - v1y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p1.velocityY = (v2x*(x2-x1)*(y2-y1) + v2y*Math.pow(y2-y1,2) - v1x*(y2-y1)*(x2-x1) + v1y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityX = (v1x*Math.pow(x2-x1,2) + v1y*(x2-x1)*(y2-y1) + v2x*Math.pow(y2-y1,2) - v2y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityY = (v1x*(x2-x1)*(y2-y1) + v1y*Math.pow(y2-y1,2) - v2x*(y2-y1)*(x2-x1) + v2y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        System.out.println("after:");
        System.out.println(p1.velocityX + " " + p1.velocityY);
        System.out.println(p2.velocityX + " " + p2.velocityY);
        System.out.println();
    }
    public static void main(String[] args) {
	// write your code here
        if ( args.length < 3){
            System.out.println("Need more than three arguments.");
            return;
        }


        int numBodies=Integer.parseInt(args[0]);
        int radiusBodies = Integer.parseInt(args[1]);
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
                bodies[i] = new Particle(i,Double.parseDouble(tempString[0]),Double.parseDouble(tempString[1]),radiusBodies);
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


        for (int i = 0 ; i < numTimeSteps;i++){
            calculateForces(bodies);
            moveBodies(bodies, sizeTimeSteps);
//            detectBoundary(bodies);
            detectCollision(bodies);
            System.out.println("Time: " + i*sizeTimeSteps +"ms Collision Count: " + collisionCount);
            printBodies(bodies);
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
