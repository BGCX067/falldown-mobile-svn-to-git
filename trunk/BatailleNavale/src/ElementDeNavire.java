
public class ElementDeNavire
{
	public static final int INTACT = 1;
	public static final int TOUCHE = 2;
	
	private Navire navire;
	//private Case caseDeJeu;
	private int etat;
	
	public ElementDeNavire(Navire navire)
	{
		if(navire==null)throw new IllegalArgumentException();
		this.navire = navire;
		setEtat(ElementDeNavire.INTACT);
	}
	
	public void setEtat(int etat)
	{
		this.etat = etat;
	}
	
	public int getEtat()
	{ return etat;}
	
	public Navire getNavire()
	{
		return navire;
	}
	
	public boolean estCoule()
	{
		return navire.estCoule();
	}
	
	
	

}
