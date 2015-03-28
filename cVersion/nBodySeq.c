#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define N_SIZE 100

typedef struct {
    double x, y;
} point;

//arguments for
//
//number of bodies
//size of each body
//number of time steps
int n, nSize, dt;
//postion, velocity
point p[N_SIZE], v[N_SIZE], f[N_SIZE];
//force and mass for each body
double m[N_SIZE];
double G = 6.67e-11;

void calculateForces();
void moveBodies();

int main(int argc, char *argv[]){

    if (argc > 1){
        n = atoi(argv[1]);
    }
    if (argc > 2){ 
        nSize = atoi(argv[2]);
    }
    if (argc > 3){
        dt = atoi(argv[3]);
    }

    //Initialize bodies

    // The value of DT (delta time) is the length of a time step.
    /*
    for [time = start to finish by DT] {
        calculateForces();
        moveBodies();
    }
    */

    return 0;
}

// calculate total force for every pair of bodies
void calculateForces(){
    double distance, magnitude;
    point direction;

    int i, j;
    for (i = 1; i < (n -1); i++){
        for (j = i+1; j < n; j++){
            distance = sqrt((pow(2.0, (p[i].x - p[j].x)) + pow(2.0, (p[i].y - p[j].y))));
            magnitude = pow(2.0, (G*m[i]*m[j]) / distance);
            direction.x = p[j].x-p[i].x;
            direction.y = p[j].y-p[i].y;
            f[i].x = f[i].x + magnitude*direction.x/distance;
            f[j].x = f[j].x - magnitude*direction.x/distance;
            f[i].y = f[i].y + magnitude*direction.y/distance;
            f[j].y = f[j].y - magnitude*direction.y/distance;
        }
    }
}
//calculate new velocity and position for each body
void moveBodies(){
    // dv = f/m * DT
    point deltav;
    // dp = (v + dv/2) * DT
    point deltap;
    
    int i;
    for (i = 0; i < n; i++){
        deltav.x = (f[i].x/m[i] * dt);
        deltav.y = (f[i].y/m[i] * dt);
        deltap.x = ((v[i].x + deltav.x/2) * dt);
        deltap.y = ((v[i].y + deltav.y/2) * dt);
        v[i].x = v[i].x + deltav.x;
        v[i].y = v[i].y + deltav.y;
        p[i].x = p[i].x + deltap.x;
        p[i].y = p[i].y + deltap.y;
        // reset force vector
        f[i].x = f[i].y = 0.0;
    }
}
