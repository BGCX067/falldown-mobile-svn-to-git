
abstract public class Regles 
{
	
	abstract public int estFinie(Grille grille);
	
	abstract public int estFinie(Grille grille, int col);
	
	abstract public boolean estUnCoupValide(Grille grille, int x);

	abstract public int getNumLignes();

	abstract public int getNumColonnes();

}
