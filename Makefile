optimized:
	javac -O nBody_seq.java
	javac -O nBody_par.java Particle_par.java Worker.java
all:
	javac nBody_seq.java
	javac nBody_par.java Particle_par.java Worker.java

clean:
	rm -r ./*.class





