
public interface Affichage 
{	
	
	/**
	 * Désigne l'objet qui se sert de l'affichage pour obtenir les entrées utilisateurs
	 * Cet objet sera informé de chaque touches pressées ou relachées
	 */
	public void setControleur(Controleur c);
	
	/**
	 * Désigne la partie pour laquelle l'affichage servira
	 */
	public void setPartie(Partie partie);
	
	/**
	 * indique à l'affichage que la partie a été modifiée
	 */
	public void update();

}
