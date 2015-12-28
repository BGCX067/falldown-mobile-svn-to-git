
public class Case 
{
	private ElementDeNavire occupant;
	private boolean bombardee = false;
	
	public boolean estOccupee()
	{
		return occupant!=null;
	}
	
	public boolean bombarder()
	{
		bombardee = true;
		if(!estOccupee() || occupant.getEtat() == ElementDeNavire.TOUCHE)
			return false;
		else
		{	
			occupant.setEtat(ElementDeNavire.TOUCHE);
			return true;
		}
	}
	
	public void setOccupant(ElementDeNavire e)
	{
		occupant = e;
	}
	
	public Navire getNavire()
	{
		return occupant.getNavire();
	}
	
	public boolean bombardee()
	{
		return bombardee;
	}
	
	public ElementDeNavire occupant()
	{
		return occupant;
	}
}
