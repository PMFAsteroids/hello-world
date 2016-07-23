package Entity;
import java.awt.Graphics2D;
import Main.*;
import Utility.*;


// Predstavlja objekte unutar igre, tj kamenje

public abstract class Entity 
{
	protected Vector pozicija; 	//Pozicija objekta
	protected Vector brzina;	//Brzina obrekta
	protected double rotation;	//Rotacija objekta
	protected double radius;	//Radijus sudara
	private boolean ukloni; 	//Oznaka da li objekat treba biti uklonjen iz igre
	private int poeni;		//Broj poena koje igrac osvaja kad pogodi ovaj objekat
	
	
	//Stvara novi primerak objekta
	public Entity(Vector poz, Vector brz, double radius, int score) 
	{
		this.pozicija = poz;
		this.brzina = brz;
		this.radius = radius;
		this.rotation = 0.0f;
		this.poeni = score;
		this.ukloni = false;
	}
	
	//Rotira objekat za datu vredost
	public void rotate(double amount)
	{
		this.rotation += amount;
		//zbog cega?
		this.rotation %= Math.PI * 2;
	}
	
	//Vraca broj poena koji se zaradi pogadjenjem objekta
	public int getPoeni() {
		return poeni;
	}
	
	//Oznaka da treba ukloniti objekat
	public void flagUkloni() 
	{
		this.ukloni = true;
	}
	
	//Vraca poziciju objekta
	public Vector getPozicija()
	{
		return pozicija;
	}
	
	//Vraca brzinu objekta
	public Vector getBrzina() 
	{
		return brzina;
	}
	
	//Vraca rotaciju objekta
	public double getRotation() 
	{
		return rotation;
	}
	
	//Vraca radijus sudara
	public double getRadius() 
	{
		return radius;
	}
	
	//Proverava da li ovaj objekat treba da se ukloni
	public boolean ukloniti()
	{
		return ukloni;
	}
	
	//Azurira stanje objekta
	public void update(Game game) 
	{
		//ubrzavanje objekta
		pozicija.add(brzina);
		
		
		//ako je izasao iz prozora da ga vrati nazad
		if(pozicija.x < 0.0f) 
		{
			pozicija.x += WorldPanel.WORLD_SIZE;
		}
		if(pozicija.y < 0.0f) 
		{
			pozicija.y += WorldPanel.WORLD_SIZE;
		}
		
		//zbog cega??
		pozicija.x %= WorldPanel.WORLD_SIZE;
		pozicija.y %= WorldPanel.WORLD_SIZE;
	}
	
	
	
	//Otkriva koja dva objekta su se sudarila
	public boolean sudar(Entity entity) 
	{
		//Koristi se Pitagorina teorema da se otkrije koja dva objekta su najbliza sudaru
		double radius = entity.getRadius() + getRadius();
		return (pozicija.getDistanceToSquared(entity.pozicija) < radius * radius);
	}
	
	
	//Upravljanje sudarom
	public abstract void handleCollision(Game game, Entity other);
	
	//Crtanje objekta
	public abstract void draw(Graphics2D g, Game game);
}
