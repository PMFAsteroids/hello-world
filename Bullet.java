package Entity;
import java.awt.Graphics2D;
import Main.*;
import Utility.*;


//Predstavlja metke u igrici

public class Bullet extends Entity
{
	private static final double VELOCITY_MAGNITUDE = 6.75;	//Magnituda brzine metka.
	private static final int MAX_LIFESPAN = 60;		//Maksimalan broj ciklusa metka
	private int lifespan;		//Broj ciklusa metka
	
	
	//Konstruktor za kreiranje instance
	public Bullet(Entity owner, double angle)
	{
		super(new Vector(owner.position), new Vector(angle).scale(VELOCITY_MAGNITUDE), 2.0, 0);
		this.lifespan = MAX_LIFESPAN;
	}
	
	
	
	@Override
	public void update(Game game) 
	{
		super.update(game);
		
		//Povecava zivotni vek metka i brise ga ako je potrebno
		this.lifespan--;
		if(lifespan <= 0)
		{
			flagForRemoval();
		}
	}
	

	@Override
	public void handleCollision(Game game, Entity other)
	{
		if(other.getClass() != Player.class)
		{
			flagForRemoval();
		}		
	}

	@Override
	public void draw(Graphics2D g, Game game)
	{
		g.drawOval(-1, -1, 2, 2);
	}

}
