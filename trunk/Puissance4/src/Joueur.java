
public interface Joueur 
{

	
	/**
	 * Est appelé par la partie elle-même quand le Joueur est passé à la partie 
	 * Cette méthode ne doit en principe jamais être appelé autrement
	 * Le second parametre dit si le joueur est joueur 1 ou 2
	 */
	public void setPartie(Partie p, int n);
	
	
	/**
	 * Méthode appelée par la partie lorsque c'est au tour de ce Joueur de jouer
	 * Doit amener le Joueur à tot ou tard appelé la méthode partie.joue(int col)
	 * Sinon blocage
	 */
	public void tourDeJouer();
	
	/**
	 * Appelé par la partie lorsque l'état de celle-ci a évolué
	 * Classiquement : un coup valide a été joué par un des deux joueurs
	 */
	public void partieModifiee();
	
	/**
	 * renvoie Partie.P1 ou Partie.P2 selon si le joueur est le player 1 ou 2
	 */
	public int getNumber();
}
