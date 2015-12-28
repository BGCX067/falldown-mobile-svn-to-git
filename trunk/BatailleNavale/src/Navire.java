
public class Navire 
{
	private ElementDeNavire[] elements;
	
	public Navire(int taille)
	{
		elements = new ElementDeNavire[taille];
		for(int i=0; i<elements.length;i++)
		{
			elements[i] = new ElementDeNavire(this);
		}
	}
	
	public boolean estCoule()
	{
		for(int i=0; i<elements.length;i++)
		{
			if(elements[i].getEtat()==ElementDeNavire.INTACT)return false;
		}
		return true;
	}
	
	public int taille()
	{
		return elements.length;
	}
	
	public ElementDeNavire element(int i)
	{
		return elements[i];
	}
}
