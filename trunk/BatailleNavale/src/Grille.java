import java.util.Vector;


public class Grille {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	private int height;
	private int width;
	
	private Case[][] cases;
	
	public Grille()
	{
		this(10,10);
	}
	
	public Grille(int width, int height)
	{
		this.width = width; this.height=height;
		
		cases = new Case[width][];
		for(int i=0; i<width;i++)
		{
			cases[i]=new Case[height];
			for(int j=0; j< cases[i].length;j++)
			{
				cases[i][j]=new Case();
			}
			
		}
	}
	
	public boolean estUnPlacementPossible(Navire n, int x, int y, int sens)
	{
		// verifier que le placement est dans la grille
		if(x<0 || x>=width || y < 0 || y >= height) return false;
		if(sens == Grille.HORIZONTAL && x+n.taille()>width) return false;
		if(sens == Grille.VERTICAL && y+n.taille()>height) return false;
		
		// verifier que toutes les cases sont libres 
		// et verifier qu'il n'y a pas de cases voisines occupees
		if(sens == Grille.HORIZONTAL)
		{
			if(x>0 && cases[x-1][y].estOccupee())return false;
			if(x<width-1 && cases[x+1][y].estOccupee())return false;
			for(int i=x; i<x+n.taille(); i++)
			{
				if(cases[i][y].estOccupee()) return false;
				if(y>0 && cases[i][y-1].estOccupee())return false;
				if(y<height-1 && cases[i][y+1].estOccupee() )return false;
			}
		}
		else
		{
			if(y>0 && cases[x][y-1].estOccupee())return false;
			if(y<height-1 && cases[x][y+1].estOccupee())return false;
			for(int i=y; i<y+n.taille(); i++)
			{
				if(cases[x][i].estOccupee()) return false;
				if(x>0 && cases[x-1][i].estOccupee())return false;
				if(x<width-1 && cases[x+1][i].estOccupee() )return false;
			}
		}
		return true;
	}
	
	public boolean place(Navire n, int x, int y, int sens)
	{
		if(!estUnPlacementPossible(n, x, y, sens)) return false;
		
		if(sens==Grille.HORIZONTAL)
		{
			for(int i=x; i<x+n.taille(); i++)
			{
				cases[i][y].setOccupant(n.element(i-x));
			}
		}
		else
		{
			for(int i=y; i<y+n.taille(); i++)
			{
				cases[x][i].setOccupant(n.element(i-y));
			}
		}
		return true;
	}
	
	public int bombarde(int x, int y)
	{
		if(x<0 || x>=width || y < 0 || y >= height) return Protocole.INVALIDE;
		
		
		if(cases[x][y].bombarder())
		{
			if(cases[x][y].getNavire().estCoule()) 
				return Protocole.COULE;
			else
				return Protocole.TOUCHE;
		}
		else
		{
			return Protocole.RATE;
		}
	}
	
	public Case getCase(int x, int y)
	{
		return cases[x][y];
	}
	
	public int getWidth()
	{
		return cases.length;
	}
	public int getHeight()
	{
		return cases[0].length;
	}
	
	public Vector elementsIntacts()
	{
		Vector v = new Vector();
		for(int i=0; i<cases.length;i++)
			for(int j=0; j<cases[i].length;j++)
			{
				if  (cases[i][j].estOccupee() && !cases[i][j].bombardee())
				{
					v.addElement( new Byte((byte)i));
					v.addElement( new Byte((byte)j));
				}
			}
		return v;
	}
}
