
/**
 * utiliser pour mémoriser les coups
 *
 */
public class Coup {
	private int colonne;
	private int joueur;
	
	public Coup(int colonne, int joueur)
	{
		this.colonne = colonne;
		this.joueur = joueur;
	}

	public int getColonne() {
		return colonne;
	}

	public int getJoueur() {
		return joueur;
	}
	
	
}
