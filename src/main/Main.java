package main;

import java.io.*;

/**
 * Particle class used for storing particle's information as well as testing if one interact with another
 */
class Particle{
    int id;
    double x,y;
    double velocityX,velocityY;
    double forceX,forceY;
    double radius;

    //constructor for the particle creation
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

    //constructor for placeholder particle
    Particle(double x, double y){
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;
        forceX = 0;
        forceY = 0;
    }

    //use the radius and position to calculate if this particle intersect with another particle
    boolean intersect(Particle another){
        return (Math.pow((another.x-x),2) + Math.pow((y-another.y),2)) <= Math.pow((radius+another.radius),2);
    }

}


public class Main {
    //initializing constants and variables
    static double G = 6.67*Math.pow(10,-11);
    static double mass = 1;
    static int collisionCount = 0;

    //method for printing all particles' position
    static void printBodies(Particle[] bodies){
        for(int i = 0 ; i < bodies.length;i++){
            System.out.println(i+" is at "+bodies[i].x+" "+bodies[i].y);
        }
    }
    //method for calculating the forces acting on each particle
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
            }
        }
    }
    //method all the particles according to its force and velocity
    static void moveBodies(Particle[] bodies, double deltaT){
        Particle deltaV;
        Particle deltaP;

        for(int i = 0 ; i < bodies.length;i++){
            deltaV = new Particle(bodies[i].forceX/mass * deltaT, bodies[i].forceY/mass * deltaT);
            deltaP = new Particle((bodies[i].velocityX+deltaV.x/2)*deltaT, (bodies[i].velocityY+deltaV.y/2)*deltaT);

            bodies[i].velocityX = bodies[i].velocityX + deltaV.x;
            bodies[i].velocityY = bodies[i].velocityY + deltaV.y;
            bodies[i].x = bodies[i].x + deltaP.x;
            bodies[i].y = bodies[i].y + deltaP.y;
            bodies[i].forceX = 0;
            bodies[i].forceY= 0;
        }
    }
    //detect collision for all particles and calculate the velocity values of each colliding particle
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
    //detect if any of the particle hits the boundary and change the direction where the particle goes
    static void detectBoundary(Particle[] bodies){
        for(int i = 0; i < bodies.length;i++){
            double x = bodies[i].x;
            double y = bodies[i].y;
            if(x < -100){
                System.out.println(bodies[i].id + " hits the left boundary and bounced.");
                bodies[i].velocityX = -bodies[i].velocityX;
            }else if(x> 100){
                System.out.println(bodies[i].id + " hits the right boundary and bounced.");
                bodies[i].velocityX = -bodies[i].velocityX;
            }

            if(y < -100){
                System.out.println(bodies[i].id + " hits the bottom boundary and bounced.");
                bodies[i].velocityY = -bodies[i].velocityY;
            }else if(y > 100){
                System.out.println(bodies[i].id + " hits the top boundary and bounced.");
                bodies[i].velocityY = -bodies[i].velocityY;
            }

        }
    }

    //calculate the velocity after the collision between particle 1 and 2
    static void calculateCollision(Particle p1, Particle p2){
        double x1  = p1.x;
        double y1  = p1.y;
        double v1x = p1.velocityX;
        double v1y = p1.velocityY;
        double x2  = p2.x;
        double y2  = p2.y;
        double v2x = p2.velocityX;
        double v2y = p2.velocityY;
//        System.out.println("Collision between particle "+ p1.id +" and particle " + p2.id);
//        System.out.println("Velocity Before:");
//        System.out.println(p1.velocityX + " " + p1.velocityY);
//        System.out.println(p2.velocityX + " " + p2.velocityY);
        p1.velocityX = (v2x*Math.pow(x2-x1,2) + v2y*(x2-x1)*(y2-y1) + v1x*Math.pow(y2-y1,2) - v1y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p1.velocityY = (v2x*(x2-x1)*(y2-y1) + v2y*Math.pow(y2-y1,2) - v1x*(y2-y1)*(x2-x1) + v1y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityX = (v1x*Math.pow(x2-x1,2) + v1y*(x2-x1)*(y2-y1) + v2x*Math.pow(y2-y1,2) - v2y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityY = (v1x*(x2-x1)*(y2-y1) + v1y*Math.pow(y2-y1,2) - v2x*(y2-y1)*(x2-x1) + v2y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
//        System.out.println("Velocity after:");
//        System.out.println(p1.velocityX + " " + p1.velocityY);
//        System.out.println(p2.velocityX + " " + p2.velocityY);
//        System.out.println();
    }

    public static void main(String[] args) {

        //check for the correct amount of arguments
        if ( args.length < 3){
            if( args.length > 4){
                System.out.println("Max argument is 4");
            }
            System.out.println("Need more than three arguments.");
            return;
        }

        //apply arguments
        int numBodies=Integer.parseInt(args[0]);
        double radiusBodies = Double.parseDouble(args[1]);
        int numTimeSteps = Integer.parseInt(args[2]);
        int sizeTimeSteps;
        if(args.length == 4){
            sizeTimeSteps = Integer.parseInt(args[3]);
        }else{
            sizeTimeSteps = 5000;
        }

        Particle[] bodies = new Particle[numBodies];
        String fileName = "init.txt";
        String line;

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
        }

        //start counting time
        long startTime = System.currentTimeMillis();
        long estimatedTime;
        for (int i = 0 ; i < numTimeSteps;i++){
            calculateForces(bodies);
            moveBodies(bodies, sizeTimeSteps);
            detectBoundary(bodies);
            detectCollision(bodies);
            //System.out.println("Computation time:Time: " + i*sizeTimeSteps +"ms Collision Count: " + collisionCount);
           // printBodies(bodies);
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        long seconds = estimatedTime/1000;
        long milliseconds = estimatedTime-(seconds*1000);
        System.out.println("computation time:\t" + seconds +" seconds\t"+ milliseconds + " milliseconds");
        System.out.println("collision count:\t" + collisionCount);

        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
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





    }
}
