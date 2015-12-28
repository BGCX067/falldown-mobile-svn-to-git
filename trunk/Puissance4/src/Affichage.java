
public interface Affichage 
{	
	
	/**
	 * D�signe l'objet qui se sert de l'affichage pour obtenir les entr�es utilisateurs
	 * Cet objet sera inform� de chaque touches press�es ou relach�es
	 */
	public void setControleur(Controleur c);
	
	/**
	 * D�signe la partie pour laquelle l'affichage servira
	 */
	public void setPartie(Partie partie);
	
	/**
	 * indique � l'affichage que la partie a �t� modifi�e
	 */
	public void update();

}
