//note: Still need to do something with nSize and determine if there are
//collisions
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <sys/time.h>

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
//total number of collisions to occur
int collisions = 0;
//flags for bodies that have experienced collision
int collisionFlags[N_SIZE];
//postion, velocity
point p[N_SIZE], v[N_SIZE], f[N_SIZE];
//force and mass for each body
double m[N_SIZE];
double G = 6.67e-11;

void calculateForces();
void moveBodies();
void detectCollisions();

int main(int argc, char *argv[]){

    int i;
    struct timeval startTime, endTime;
    int seconds, micros;
    FILE *outputFile;

    outputFile = fopen("nBodyResults.txt", "w");

    if (argc > 3){
        //number of bodies
        n = atoi(argv[1]);
        //size of each body 
        nSize = atoi(argv[2]);
        //number of time steps
        dt = atoi(argv[3]);
    }
    else{
        fprintf(stderr, "Too few arguments\n");
    }

    int a = 0, b = 0;
    //Initialize the positions, velocities, forces, and masses
    for (i = 1; i < n; i++){
        if (i == 1){
            p[i].x = 1;
            p[i].y = 1;
        }
        else if (i == 2){
            p[i].x = 1;
            p[i].y = 4;
        }
        else{
            p[i].x = a;
            p[i].y = b;
        }
        v[i].x = 0.0;
        v[i].y = 0.0;
        f[i].x = 0.0;
        f[i].y = 0.0;
        m[i] = 1.0;
        collisionFlags[i] = 0;
        //Temporary way plotting bodies
        a += 2;//rand() % 3;
        b += 2;//rand() % 3 + 1;
    }

    //May not be needed later
    fprintf(outputFile, "Before moving:\n");
    fprintf(outputFile, "%9s%9s%9s%9s\n", "x", "y", "vx", "vy");
    for (i = 1; i < n; i++){
        fprintf(outputFile, "%9.4f %9.4f %9.4f %9.4f\n", p[i].x, p[i].y, v[i].x, v[i].y);
    }
    fprintf(outputFile, "\n");

    gettimeofday(&startTime, NULL);

    // The value of DT (delta time) is the length of a time step.
    /*
    for [time = start to finish by DT] {
        calculateForces();
        moveBodies();
    }
    */
    calculateForces();
    moveBodies();
    detectCollisions();

    gettimeofday(&endTime, NULL);

    seconds = endTime.tv_sec - startTime.tv_sec;
    micros = endTime.tv_usec - startTime.tv_usec;
    if (endTime.tv_usec < startTime.tv_usec){
        micros += 1000000;
        seconds--;
    }

    printf("computation time = %d seconds, %d microseconds\n", seconds, micros);
    printf("number of collisions detected = %d\n", collisions);

    fprintf(outputFile, "After moving:\n");
    fprintf(outputFile, "%9s%9s%9s%9s\n", "x", "y", "vx", "vy");
    for (i = 1; i < n; i++){
        fprintf(outputFile, "%9.4f %9.4f %9.4f %9.4f\n", p[i].x, p[i].y, v[i].x, v[i].y);
    }
    return 0;
}

// calculate total force for every pair of bodies
void calculateForces(){
    double distance, magnitude;
    point direction;

    int i, j;
    for (i = 1; i < (n -1); i++){
        for (j = i+1; j < n; j++){
            distance = sqrt((pow((p[i].x - p[j].x), 2.0) + pow((p[i].y - p[j].y), 2.0)));
            magnitude = ((G*m[i]*m[j]) / (pow(distance, 2.0)));
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
    for (i = 1; i < n; i++){
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

//Updates the velocities of bodies that have experienced a collision
void detectCollisions(){
    
}
