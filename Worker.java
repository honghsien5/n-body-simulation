
import javax.swing.*;

//worker thread class
public class Worker extends Thread{
    private int id;
    private static double G = 6.67*Math.pow(10,-11);
    private static double mass = 1;
    JFrame frame;
    JLabel timeStepLabel;

    //worker constructor
    public Worker(int id,JFrame frame, JLabel timeStepLabel){
        this.id = id;
        this.frame = frame;
        this.timeStepLabel = timeStepLabel;
    }

    public void run(){
        for (int i = 0 ; i < nBody_par.numTimeSteps;i++){
            calculateForces();
            barrier();
            moveBodies(nBody_par.sizeTimeSteps);
            barrier();
            detectBoundary();
            detectCollision();
            barrier();
            if(nBody_par.graphicSwitch){
                timeStepLabel.setText("Time: " + i);
                frame.repaint();
            }
        }

    }

    private void calculateForces(){
        double distance, magnitude;
        Particle_par direction;

        for(int i = id; i < nBody_par.bodies.length;i += nBody_par.numWorkers){
            for(int j = i+1; j < nBody_par.bodies.length ; j++){
                distance = Math.sqrt(Math.pow((nBody_par.bodies[i].x- nBody_par.bodies[j].x),2) + Math.pow((nBody_par.bodies[i].y- nBody_par.bodies[j].y),2));
                magnitude = (G*mass*mass)/Math.pow(distance,2);
                direction = new Particle_par(id, nBody_par.bodies[j].x- nBody_par.bodies[i].x, nBody_par.bodies[j].y- nBody_par.bodies[i].y, 0);

                nBody_par.forces[id][i][0] = nBody_par.forces[id][i][0]+ magnitude*direction.x/distance;
                nBody_par.forces[id][j][0] = nBody_par.forces[id][j][0]- magnitude*direction.x/distance;
                nBody_par.forces[id][i][1] = nBody_par.forces[id][i][1]+ magnitude*direction.y/distance;
                nBody_par.forces[id][j][1] = nBody_par.forces[id][j][1]- magnitude*direction.y/distance;
            }
        }
    }
    //move the bodies according to their velocity. also perform force summation for each body
    private void moveBodies(double deltaT){
        Particle_par deltaV;
        Particle_par deltaP;

        for(int i = id ; i < nBody_par.bodies.length;i += nBody_par.numWorkers){
            for (int k = 0; k < nBody_par.numWorkers; k++){
                nBody_par.bodies[i].forceX += nBody_par.forces[k][i][0];
                nBody_par.forces[k][i][0] = 0;
                nBody_par.bodies[i].forceY += nBody_par.forces[k][i][1];
                nBody_par.forces[k][i][1] = 0;
            }
            deltaV = new Particle_par(id, nBody_par.bodies[i].forceX/mass * deltaT, nBody_par.bodies[i].forceY/mass * deltaT, 0);
            deltaP = new Particle_par(id, (nBody_par.bodies[i].velocityX+deltaV.x/2)*deltaT, (nBody_par.bodies[i].velocityY+deltaV.y/2)*deltaT, 0);

            nBody_par.bodies[i].velocityX = nBody_par.bodies[i].velocityX + deltaV.x;
            nBody_par.bodies[i].velocityY = nBody_par.bodies[i].velocityY + deltaV.y;
//            System.out.println("Velocity "+ i + ": " + Main.bodies[i].velocityX +" " + Main.bodies[i].velocityY);
            nBody_par.bodies[i].x = nBody_par.bodies[i].x + deltaP.x;
            nBody_par.bodies[i].y = nBody_par.bodies[i].y + deltaP.y;
            nBody_par.bodies[i].forceX = 0;
            nBody_par.bodies[i].forceY= 0;
        }
    }
    //detect if any of the particle hits the boundary and change the direction where the particle goes
    private void detectBoundary(){
        for(int i = id; i < nBody_par.bodies.length;i++){
            double x = nBody_par.bodies[i].x;
            double y = nBody_par.bodies[i].y;
            double r = nBody_par.bodies[i].radius;
            if(x-r < -100){
//                System.out.println(bodies[i].id + " hits the left boundary and bounced.");
                nBody_par.bodies[i].velocityX = -nBody_par.bodies[i].velocityX;
            }else if(x+r> 100){
//                System.out.println(bodies[i].id + " hits the right boundary and bounced.");
                nBody_par.bodies[i].velocityX = -nBody_par.bodies[i].velocityX;
            }

            if(y-r < -100){
//                System.out.println(bodies[i].id + " hits the bottom boundary and bounced.");
                nBody_par.bodies[i].velocityY = -nBody_par.bodies[i].velocityY;
            }else if(y+r > 100){
//                System.out.println(bodies[i].id + " hits the top boundary and bounced.");
                nBody_par.bodies[i].velocityY = -nBody_par.bodies[i].velocityY;
            }

        }
    }
    //detect the collision and increase the collision count if a collision happens
    private void detectCollision(){
        for(int i = id; i < nBody_par.bodies.length;i += nBody_par.numWorkers){
            for(int j = i+1; j < nBody_par.bodies.length;j++){
                if(nBody_par.bodies[i].intersect(nBody_par.bodies[j])){
                    nBody_par.collisionCount++;
                    calculateCollision(nBody_par.bodies[i], nBody_par.bodies[j]);
                }
            }

        }
    }
    //calculate the velocity when collision happens between two particles
    private void calculateCollision(Particle_par p1, Particle_par p2){
        double x1  = p1.x;
        double y1  = p1.y;
        double v1x = p1.velocityX;
        double v1y = p1.velocityY;
        double x2  = p2.x;
        double y2  = p2.y;
        double v2x = p2.velocityX;
        double v2y = p2.velocityY;
//        System.out.println(p1.velocityX + " " + p1.velocityY);
//        System.out.println(p2.velocityX + " " + p2.velocityY);
        p1.velocityX = (v2x*Math.pow(x2-x1,2) + v2y*(x2-x1)*(y2-y1) + v1x*Math.pow(y2-y1,2) - v1y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p1.velocityY = (v2x*(x2-x1)*(y2-y1) + v2y*Math.pow(y2-y1,2) - v1x*(y2-y1)*(x2-x1) + v1y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityX = (v1x*Math.pow(x2-x1,2) + v1y*(x2-x1)*(y2-y1) + v2x*Math.pow(y2-y1,2) - v2y*(x2-x1)*(y2-y1))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        p2.velocityY = (v1x*(x2-x1)*(y2-y1) + v1y*Math.pow(y2-y1,2) - v2x*(y2-y1)*(x2-x1) + v2y*Math.pow(x2-x1,2))/(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
//        System.out.println(p1.velocityX + " " + p1.velocityY);
//        System.out.println(p2.velocityX + " " + p2.velocityY);
    }
    //print out all of the bodies location
    private void printBodies(){
        for(int i = 0 ; i < nBody_par.bodies.length;i++){
            System.out.println(i+" is at "+ nBody_par.bodies[i].x+" "+ nBody_par.bodies[i].y);
        }
    }
    //dissemination barrier
    private void barrier(){
        int stage = 1;

        while (stage < nBody_par.numWorkers){
            nBody_par.arrive[id]++;
            try {
                nBody_par.mutex.acquire();
                while(!(nBody_par.arrive[((id + stage) % nBody_par.numWorkers)] >= nBody_par.arrive[id])){
                    nBody_par.mutex.release();
                    //Thread.sleep(20);
                    nBody_par.mutex.acquire();
                }
                nBody_par.mutex.release();
            } catch (InterruptedException e) {};
            stage *= 2;

        }

    }

}
