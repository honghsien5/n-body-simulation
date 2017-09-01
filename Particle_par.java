
public class Particle_par {
    public int id;
    public double x,y;
    public double velocityX,velocityY;
    public double forceX,forceY;
    public double radius;
    public Particle_par(int id, double x, double y, double radius){
        this.id = id;
        this.x=x;
        this.y=y;
        velocityX = 0 ;
        velocityY = 0;
        forceX = 0;
        forceY = 0;
        this.radius = radius;
    }
    boolean intersect(Particle_par another){

        return (Math.pow((another.x-x),2) + Math.pow((y-another.y),2)) <= Math.pow((radius+another.radius),2);

    }
}
