
public class ReglesClassiques extends Regles {

	public int estFinie(Grille grille) {
		int a = alignement(grille);
		if(a!=-1)
			return a;
		
		if (remplie(grille))
			return Partie.EGALITE;

		return -1;
	}

	public boolean estUnCoupValide(Grille grille, int x) {
		if(x<0 || x>=grille.numColonnes() || grille.getCase(x, 0)!=Partie.VIDE) return false;
		return true;
	}

	public int getNumColonnes() {
		return 7;
	}

	public int getNumLignes() {
		return 6;
	}
	
	private int alignement(Grille grille)
	{
		for(int x=0; x<grille.numColonnes(); x++)
		{
			for(int y=0; y<grille.numLignes();y++)
			{
				if(alignement(grille,x,y))return grille.getCase(x, y);
			}
		}
		return -1;
	}
	
	private boolean alignement(Grille grille, int x, int y)
	{
		for(int i=-1; i<=1; i++)
			for(int j=-1;j<=1; j++)
			{
				if(alignement(grille,x,y,i,j)) return true;
			}
		return false;
	}
	
	/**
	 * Check si la case (x,y) est la premiere case d'un alignement de 4 cases
	 * dans la direction dx dy 
	 */
	private boolean alignement(Grille grille,int x, int y, int dx, int dy)
	{
		
		int PLAYER;
		if(ok(grille,x,y) && grille.getCase(x, y) != Partie.VIDE && (dx != 0 || dy != 0))
		{
			PLAYER = grille.getCase(x, y);
		}
		else
		{
			return false;
		}
		
		for(int i=1; i<4; i++)
		{
			if( !ok(grille,x+i*dx,y+i*dy) || grille.getCase(x+i*dx,y+i*dy) != PLAYER )
				return false;
		}
		
		return true;
	}
	
	/**
	 * Vérifie que la case x,y existe bien dans la partie p
	 */
	private boolean ok(Grille grille, int x, int y)
	{
		return (x>=0 && x<grille.numColonnes() && y >=0 && y<grille.numLignes());
	}
	
	/**
	 * regarde si la partie n'est pas finie car toutes la grille serait remplie(ex aequo)
	 */
	private boolean remplie(Grille grille)
	{
		for(int i=0; i<grille.numColonnes(); i++)
		{
			if(grille.getCase(i, 0)==Partie.VIDE) return false;
		}
		return true;
	}
	
	public int estFinie(Grille grille, int col)
	{
		int ligne=0;
		while(ligne<grille.numLignes() && grille.getCase(col,ligne)==Partie.VIDE)
		{
			ligne++;
		}
		if(ligne>=grille.numLignes())ligne = grille.numLignes()-1;
		
		if(!ok(grille,col,ligne))return -2;
		
		int dx=-1;
		int dy=1;
		if(compte(grille, col, ligne, dx, dy) >= 4)
		{
			return grille.getCase(col,ligne);
		}
		
		dx=-1;
		dy=0;
		if(compte(grille, col, ligne, dx, dy) >= 4)
		{
			return grille.getCase(col,ligne);
		}
		
		dx=-1;
		dy=-1;
		if(compte(grille, col, ligne, dx, dy) >= 4)
		{
			return grille.getCase(col,ligne);
		}
		
		dx=0;
		dy=-1;
		if(compte(grille, col, ligne, dx, dy) >= 4)
		{
			return grille.getCase(col,ligne);
		}
		
		if(remplie(grille))
		{
			return Partie.EGALITE;
		}
		
		return -1;
	}
	
	private int compte(Grille grille, int x, int y, int dx, int dy)
	{
		int i=1;
		for(i=1; i<4 && ok(grille, x+i*dx,y+i*dy) && grille.getCase(x+i*dx,y+i*dy) == grille.getCase(x, y); i++);
		int r=i-1;
		for(i=1; i<4 && ok(grille, x+i*-dx,y+i*-dy) && grille.getCase(x+i*-dx,y+i*-dy) == grille.getCase(x, y); i++);
		r+=i;
		return r;
	}
	
	private int debut(Grille grille, int col, int ligne, int dx, int dy)
	{
		int d = 0;
		while(ok(grille, col+dx*d,ligne+dy*d) && grille.getCase( col+dx*d, ligne+dy*d)==grille.getCase(col, ligne))
		{
			d++;
		}
		return d;
	}

}
