
public class Grille {
	byte[][] grille;
	
	public static final int VIDE = 0;
	public static final int P1 = 1;
	public static final int P2 = 2;
	
	public Grille(int colonnes, int lignes)
	{
		grille = new byte[colonnes][lignes];
	}
	
	public Grille(Grille g)
	{
		grille = new byte[g.numColonnes()][g.numLignes()];
		for(int i=0; i<grille.length;i++)
		{
			for(int j=0; j<grille[i].length;j++)
			{
				grille[i][j]=(byte) g.getCase(i, j);
			}
		}
	}
	
	public void faitTomberPiece(int value, int col)
	{
		int i=0;
		for(i=0; i<grille[col].length && grille[col][i]==Partie.VIDE; i++);
		grille[col][i-1]=(byte)value;
	}
	
	public int getCase(int colonne, int ligne)
	{
		return grille[colonne][ligne];
	}
	
	public int numColonnes() {
		return grille.length;
	}

	public int numLignes() {
		return grille[0].length;
	}
	
	public void retire(int col)
	{
		int i=0;
		for(i=0; i<grille[col].length && grille[col][i]==Grille.VIDE;i++);
		if(i<grille[col].length)
			grille[col][i]=VIDE;
		
	}
}
