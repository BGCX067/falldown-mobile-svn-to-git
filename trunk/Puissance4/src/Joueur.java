
public interface Joueur 
{

	
	/**
	 * Est appel� par la partie elle-m�me quand le Joueur est pass� � la partie 
	 * Cette m�thode ne doit en principe jamais �tre appel� autrement
	 * Le second parametre dit si le joueur est joueur 1 ou 2
	 */
	public void setPartie(Partie p, int n);
	
	
	/**
	 * M�thode appel�e par la partie lorsque c'est au tour de ce Joueur de jouer
	 * Doit amener le Joueur � tot ou tard appel� la m�thode partie.joue(int col)
	 * Sinon blocage
	 */
	public void tourDeJouer();
	
	/**
	 * Appel� par la partie lorsque l'�tat de celle-ci a �volu�
	 * Classiquement : un coup valide a �t� jou� par un des deux joueurs
	 */
	public void partieModifiee();
	
	/**
	 * renvoie Partie.P1 ou Partie.P2 selon si le joueur est le player 1 ou 2
	 */
	public int getNumber();
}
