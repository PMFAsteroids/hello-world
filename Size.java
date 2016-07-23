package Entity;
import java.awt.Polygon;

//Sadrzi podatke o razlicitim velicine asteroida.

public enum Size
{
	//Mali Asteroidi imaju radijus 15, a vrede 100 bodova.
	Small(15.0, 100),
	Medium(25.0, 50),
	Large(40.0, 20);
	
	
	private static final int NUMBER_OF_POINTS = 5;	//broj tacaka
	public final Polygon polygon;	//Poligon za ovu vrstu asteroida.
	public final double radius;		//Radijus za ovu vrstu asteroida.
	public final int killValue;		//Broj poena koji se osvajaju pogadjenjem ove vrste asteroida.
	
	
	
	//Kreiranje novog tipa asteroida
	private Size(double radius, int value) 
	{
		this.polygon = generatePolygon(radius);
		this.radius = radius + 1.0;
		this.killValue = value;
	}
	
	

	//Generise poligon
	private static Polygon generatePolygon(double radius)
	{
		//Kreira niz da sacuva koordinate
		int[] x = new int[NUMBER_OF_POINTS];
		int[] y = new int[NUMBER_OF_POINTS];
		
		//Generise tacke poligona
		double angle = (2 * Math.PI / NUMBER_OF_POINTS);
		for(int i = 0; i < NUMBER_OF_POINTS; i++)
		{
			x[i] = (int) (radius * Math.sin(i * angle));
			y[i] = (int) (radius * Math.cos(i * angle));
		}
		
		//Kreira poligon sa zadatim tackama i vraca ga
		return new Polygon(x, y, NUMBER_OF_POINTS);
	}
	
}
