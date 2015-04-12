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
//postion, velocity
point p[N_SIZE], v[N_SIZE], f[N_SIZE];
//force and mass for each body
double m[N_SIZE];
//double G = (6.673 * pow(10.0,-8.0));
//G = 6.67e-11

void calculateForces();
void moveBodies();
void detectCollisions();
void calculateCollisions(int p1, int p2);

int main(int argc, char *argv[]){

    int i;
    struct timeval startTime, endTime;
    int seconds, micros;
    FILE *inputFile, *outputFile;

    inputFile = fopen("nBodyInput.txt", "r");
    outputFile = fopen("nBodyResults.txt", "w");

    if (outputFile == NULL){
        fprintf(stderr, "Cannot open input file\n");
        exit(1);
    }

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
    i = 0;
    while(!feof(inputFile)){
       fscanf(inputFile, "%lf %lf", &p[i].x, &p[i].y); 
       i++;
    }
    //Initialize the positions, velocities, forces, and masses
    for (i = 0; i < n; i++){
        v[i].x = 0;
        v[i].y = 0;
        f[i].x = 0;
        f[i].y = 0;
        m[i] = 10000000.0;
    }

    //May not be needed later
    fprintf(outputFile, "Before moving:\n");
    fprintf(outputFile, "%9s%9s%9s%9s\n", "x", "y", "vx", "vy");
    for (i = 0; i < n; i++){
        fprintf(outputFile, "%9.4f %9.4f %9.4f %9.4f\n", p[i].x, p[i].y, v[i].x, v[i].y);
    }
    fprintf(outputFile, "\n");

    gettimeofday(&startTime, NULL);

    // The value of DT (delta time) is the length of a time step.
    int start = 0;
    double finish = 18110;
    int time;
    for (time = start; time < finish; time += dt) {
        calculateForces();
        moveBodies();
        detectCollisions();
    }
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
    for (i = 0; i < n; i++){
        fprintf(outputFile, "%9.4f %9.4f %9.4f %9.4f\n", p[i].x, p[i].y, v[i].x, v[i].y);
    }

    fclose(outputFile);
    fclose(inputFile);
    return 0;
}

// calculate total force for every pair of bodies
void calculateForces(){
    double distance, magnitude;
    //double long G = (6.67 * exp(-11));
    double long G = (6.67E-11);
    point direction;

    //printf("G = %Le\n", G);
    int i, j;
    for (i = 0; i < (n -1); i++){
        for (j = (i+1); j < n; j++){
            distance = (sqrt((pow((p[i].x - p[j].x), 2.0) + pow((p[i].y - p[j].y), 2.0))));
            //printf("distance = %lf\n", distance);
            magnitude = ((G*m[i]*m[j]) / (pow(distance, 2.0)));
            //printf("magnitude = %lf\n", magnitude);
            direction.x = (p[j].x-p[i].x);
            direction.y = (p[j].y-p[i].y);
            f[i].x = (((f[i].x + magnitude)*direction.x)/distance);
            f[j].x = (((f[j].x - magnitude)*direction.x)/distance);
            f[i].y = (((f[i].y + magnitude)*direction.y)/distance);
            f[j].y = (((f[j].y - magnitude)*direction.y)/distance);
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
        v[i].x = (v[i].x + deltav.x);
        v[i].y = (v[i].y + deltav.y);
        p[i].x = (p[i].x + deltap.x);
        p[i].y = (p[i].y + deltap.y);
        // reset force vector
        f[i].x = f[i].y = 0.0;
    }
}

//Updates the velocities of bodies that have experienced a collision
void detectCollisions(){
    printf("Start collision detection\n");
    double distance;
    int i, j;
    for (i = 0; i < (n-1); i++){
        for (j = (i+1); j < n; j++){
            distance = (sqrt((pow((p[i].x - p[j].x), 2.0) + pow((p[i].y - p[j].y), 2.0))));
            printf("object1 = %lf %lf with velocity of %lf %lf\n", p[i].x, p[i].y, v[i].x, v[i].y); 
            printf("object2 = %lf %lf with velocity of %lf %lf\n", p[j].x, p[j].y, v[j].x, v[j].y); 
            if (distance <= (nSize * 2)){
                printf("Detected collision\n");
                collisions++;
                calculateCollisions(i, j);
            }
        }
    }
}
void calculateCollisions(int p1, int p2){
    v[p1].x = (((v[p2].x * pow((p[p2].x - p[p1].x), 2)) + (v[p2].y * ((p[p2].x - p[p1].x) * (p[p2].y - p[p1].y))) + ((v[p1].x * pow((p[p2].y - p[p1].y), 2)) - (v[p1].y * (p[p2].x - p[p1].x) * (p[p2].y - p[p1].y)))) / ((pow((p[p2].x - p[p1].x), 2)) + (pow((p[p2].y - p[p1].y), 2))));
    v[p1].y = (((v[p2].x * (p[p2].x - p[p1].x) * (p[p2].y - p[p1].y)) + (v[p2].y * (pow((p[p2].y - p[p1].y), 2))) - (v[p1].x * (p[p2].y - p[p1].y) * (p[p2].x - p[p1].x)) + (v[p1].y * (pow((p[p2].x - p[p1].x), 2)))) / ((pow((p[p2].x - p[p1].x), 2)) + (pow((p[p2].y - p[p1].y), 2))));
    v[p2].x = (((v[p1].x * pow((p[p2].x - p[p1].x), 2)) + (v[p1].y * (p[p2].x - p[p1].x) * (p[p2].y - p[p1].y)) + (v[p2].x * (pow((p[p2].y - p[p1].y), 2))) - (v[p2].y * (p[p2].x - p[p1].x) * (p[p2].y - p[p1].y))) / (pow((p[p2].x - p[p1].x), 2) + (pow((p[p2].y - p[p1].y), 2))));
    v[p2].y = (((v[p1].x * (p[p2].x - p[p1].x) * (p[p2].y - p[p1].y)) + (v[p1].y * pow((v[p2].x - v[p1].x), 2)) - (v[p2].x * (p[p2].y - p[p1].y) * (p[p2].x - p[p1].x)) + (v[p2].y * pow((p[p2].x - p[p1].x), 2))) / (pow((p[p2].x - p[p1].x), 2) + pow((p[p2].y - p[p1].y), 2)));
}
