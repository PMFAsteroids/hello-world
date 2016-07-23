package Entity;

import java.awt.Graphics2D;
import java.util.Random;
import Main.*;
import Utility.*;

public class Asteroid extends Entity
{

	private static final double MIN_ROTATION = 0.0075;//Minimalna brzina rotacije asteroida
	private static final double MAX_ROTATION = 0.0175; //Maksimalna brzina rotacije asteroida
	private static final double ROTATION_VARIANCE = MAX_ROTATION - MIN_ROTATION;//Razlika max i min brzine rotacije
	private static final double MIN_VELOCITY = 0.75;//Minimalna brzina asteroida
	private static final double MAX_VELOCITY = 1.65;//Maksimalna brzina asteroida
	private static final double VELOCITY_VARIANCE = MAX_VELOCITY - MIN_VELOCITY;//Razlika max i min brzine
	private static final double MIN_DISTANCE = 200.0;// Minimalna udaljenost od igraca kada se mogu novi asteroidi umnoziti
	private static final double MAX_DISTANCE = WorldPanel.WORLD_SIZE/2.0;// Maksimalna udaljenost od igraca kada se mogu novi asteroidi umnoziti
	private static final double DISTANCE_VARIANCE = MAX_DISTANCE - MIN_DISTANCE;//Razlika izmedju min i max udaljenosti
	private static final float SPAWN_UPDATES = 10;//Broj azuriranja koji treba da se obave nakon umnozavanja asteroida
	private AsteroidSize size;//Velicina asteroida
	private double rotationSpeed;//Brzina rotacije
	

	//Kreiranje novog aseroida random u svemiru
	public Asteroid(Random random) 
	{
		super(calculatePosition(random), calculateVelocity(random), Size.Large.radius, Size.Large.killValue);
		this.rotationSpeed = -MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = AsteroidSize.Large;
	}
	
	//Kreiranje dete asteroid od roditelja
	public Asteroid(Asteroid parent,AsteroidSize size, Random random)
	{
		super(new Vector(parent.pozicija), calculateVelocity(random), size.radius, size.killValue);
		this.rotationSpeed = MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = size;
		
		for(int i = 0; i < SPAWN_UPDATES; i++)
		{
			update(null);
		}
	}
	
	
	//Dovdeeeee
	
	/**
	 * Calculates a random valid spawn point for an Asteroid.
	 * @param random The random instance.
	 * @return The spawn point.
	 */
	private static Vector calculatePosition(Random random) {
		Vector vec = new Vector(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0);
		return vec.add(new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_DISTANCE + random.nextDouble() * DISTANCE_VARIANCE));
	}
	
	/**
	 * Calculates a random valid velocity for an Asteroid.
	 * @param random The random instance.
	 * @return The velocity.
	 */
	private static Vector calculateVelocity(Random random) {
		return new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_VELOCITY + random.nextDouble() * VELOCITY_VARIANCE);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		rotate(rotationSpeed); //Rotate the image each frame.
	}

	@Override
	public void draw(Graphics2D g, Game game) {
		g.drawPolygon(size.polygon); //Draw the Asteroid.
	}
	
	@Override
	public void handleCollision(Game game, Entity other) {
		//Prevent collisions with other asteroids.
		if(other.getClass() != Asteroid.class) {
			//Only spawn "children" if we're not a Small asteroid.
			if(size != AsteroidSize.Small) {
				//Determine the Size of the children.
				AsteroidSize spawnSize = AsteroidSize.values()[size.ordinal() - 1];
				
				//Create the children Asteroids.
				for(int i = 0; i < 2; i++) {
					game.registerEntity(new Asteroid(this, spawnSize, game.getRandom()));
				}
			}
			
			//Delete this Asteroid from the world.
			ukloniti();
			
			//Award the player points for killing the Asteroid.
			game.addScore(getPoeni());		
		}
	}
	

}
