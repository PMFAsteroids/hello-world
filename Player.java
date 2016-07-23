package Entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Main.*;
import Utility.*;




public class Player extends Entity
{
	private static final double DEFAULT_ROTATION = -Math.PI / 2.0;
	private static final double THRUST_MAGNITUDE = 0.0385;	//Vrednost potiska broda
	private static final double MAX_VELOCITY_MAGNITUDE = 6.5;	//Maksimalna brzina broda
	private static final double ROTATION_SPEED = 0.052;		//Brzina rotiranja broda
	private static final double SLOW_RATE = 0.995;		//Faktor usporavanja broda
	private static final int MAX_BULLETS = 10;//Maksimalan broj metaka koji odjednom moze biti ispaljen
	private static final int FIRE_RATE = 4;		//Broj obrtaja izmedju dva pucanja
	private static final int MAX_CONSECUTIVE_SHOTS = 8;//Maksimalan broj pogodaka pre pregrevanja
	private static final int MAX_OVERHEAT = 30;//Max broj ciklusa koje mora proci nakon pregrevanja
	private boolean thrustPressed;//Da li se moze pristupiti brodu kada se update-uje
	private boolean rotateLeftPressed;//Da li se moze rotirati brod u levo kada se update-uje
	private boolean rotateRightPressed;//Da li se moze rotirati brod u desno kada se update-uje
	private boolean firePressed;////Da li se moze ispaliti metak kada se update-uje
	private boolean firingEnabled;//Da li je brodu dozvoljeno da puca
	private int consecutiveShots;//Broj uzastopnih otkaza
	private int fireCooldown;	//
	private int overheatCooldown;	//
	private int animationFrame;	//Sadasnji nivo
	private List<Bullet> bullets;//Metci koji treba da budu ispaljeni
	
	
	
	//Konstruktor za inicijalizaciju igraca
	public Player() 
	{
		super(new Vector(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0),
				new Vector(0.0, 0.0), 10.0, 0);
		this.bullets = new ArrayList<>();
		this.rotation = DEFAULT_ROTATION;
		this.thrustPressed = false;
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
	}
	
	
	
	//Postavlja da li se moze bezbedno pristupiti igracu
	public void setThrusting(boolean state)
	{
		this.thrustPressed = state;
	}
	
	
	
	
	//Postavlja da li se moze rotirati u levo nakon update
	public void setRotateLeft(boolean state)
	{
		this.rotateLeftPressed = state;
	}
	
	
	
	
	
	//Postavlja da li se moze rotirati u desno nakon update
	public void setRotateRight(boolean state) 
	{
		this.rotateRightPressed = state;
	}
	
	
	
	
	//Postavlja da li se moze pucati nakon update
	public void setFiring(boolean state)
	{
		this.firePressed = state;
	}
	
	
	
	
		
	//Postavlja da li je dozvoljeno pucanje
	public void setFiringEnabled(boolean state)
	{
		this.firingEnabled = state;
	}
	
	
	
	
	//reset-ovanje karakteristika igraca
	public void reset() 
	{
		this.rotation = DEFAULT_ROTATION;
		position.set(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0);
		velocity.set(0.0, 0.0);
		bullets.clear();
	}
		
	
	
	
	@Override
	public void update(Game game) 
	{
		super.update(game);
		
		//Povecava nivo
		this.animationFrame++;
		
		//Dozvoljava rotaciju ako be bilo koji flag true
		if(rotateLeftPressed != rotateRightPressed)
		{
			rotate(rotateLeftPressed ? -ROTATION_SPEED : ROTATION_SPEED);
		}
		
		// Nanesite potisak na brod u brzini, te osigurati da brod ne ide brze od maksimalne velicine.
		if(thrustPressed) 
		{
			//Stvoricemo novi vektor na temelju rotacije naseg broda,
			velocity.add(new Vector(rotation).scale(THRUST_MAGNITUDE));
			
			//Ovde proveravamo da li je nas brod ide brže nego je to dopusteno. 
			//Prilikom provere sudara, proveravamo kvadratne velicina * Uzeti koren.*
			// Ako je nasa brzina veca od maksimalno dopustene brzina, normalizacije se
			if(velocity.getLengthSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE)
			{
				velocity.normalize().scale(MAX_VELOCITY_MAGNITUDE);
			}
		}
		
				
		// Ako se nas brod krece, i usporavamo ga,moze doci do zaustavljanja broda
		if(velocity.getLengthSquared() != 0.0)
		{
			velocity.scale(SLOW_RATE);
		}
		
		
		//Petlja koja prolazi kroz svaki metak i uklanja ga ako je neophodno
		Iterator<Bullet> iter = bullets.iterator();
		while(iter.hasNext())
		{
			Bullet bullet = iter.next();
			if(bullet.needsRemoval())
			{
				iter.remove();
			}
		}
		
		//Proveriti da li se brod pregrejao, da li se moze pustiti jos metaka
		this.fireCooldown--;
		this.overheatCooldown--;
		if(firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0) 
		{
			//Mozemo stvoriti novi metak, ako jos nismo prekoracili maksimalni broj metaka 
			//Ako se novi metak moze dodati, registrujemo ga
			if(bullets.size() < MAX_BULLETS) 
			{
				this.fireCooldown = FIRE_RATE;
				Bullet bullet = new Bullet(this, rotation);
				bullets.add(bullet);
				game.registerEntity(bullet);
			}
			
			//Kada pokusamo da ispalmo metak, mi povecavati broj uzastopnih pucanja
			//treba i utvrditi je li je potrebno postaviti pregrijavanja flag
			this.consecutiveShots++;
			if(consecutiveShots == MAX_CONSECUTIVE_SHOTS)
			{
				this.consecutiveShots = 0;
				this.overheatCooldown = MAX_OVERHEAT;
			}
		}
		
		else if(consecutiveShots > 0)
		{
			//povecavamo broj pucanja sve dok ne zaustavimo vatru
			this.consecutiveShots--;
		}
		
	}
	
	
	
	
	
	@Override
	public void handleCollision(Game game, Entity other)
	{
		//Ubiti igraca ako se sudari sa asteroidom
		if(other.getClass() == Asteroid.class) 
		{
			game.killPlayer();
		}
	}
	
	
	
	
	@Override
	public void draw(Graphics2D g, Game game) 
	{
		//Kada igrac izgubi zivot pre neogo sto se sledeci put pojavi ako ima jos zivota trepace
		if(!game.isPlayerInvulnerable() || game.isPaused() || animationFrame % 20 < 10)
		{
			//Nacrtati brod
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);
		
			//Nacrtati plamen pored broda ako nije pauziran i moze da se puca
			if(!game.isPaused() && thrustPressed && animationFrame % 6 < 3) 
			{
				g.drawLine(-6, -6, -12, 0);
				g.drawLine(-6, 6, -12, 0);
			}
		}
	}
	
	
}
