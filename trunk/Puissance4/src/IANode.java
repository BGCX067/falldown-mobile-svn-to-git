import java.util.Vector;


public class IANode {

	private Vector fils = new Vector(7);
	private boolean terminal=false;
	
	private int coup;
	private int aQuiLeTour;
	private Regles regles;
	private Grille grille;

	
	public IANode(Regles regles, Grille grille, int aQuiLeTour, int hauteur, int coupCol)
	{
		this.coup = coupCol;
		this.aQuiLeTour = aQuiLeTour;
		this.regles = regles;
		this.grille = grille;
		
		if(hauteur>0 && regles.estFinie(grille)==-1)
		{
			for(int i=0; i<grille.numColonnes();i++)
			{
				Grille newg = new Grille(grille);
				if(regles.estUnCoupValide(newg, i))
				{
					newg.faitTomberPiece(aQuiLeTour, i);
					IANode ianode = new IANode(regles,newg, aQuiLeTour==Partie.P1 ? Partie.P2 : Partie.P1, hauteur-1, i);
					fils.addElement(ianode);
				}
			}
		}
		else
		{
			this.terminal = true;
		}
	}
	
	public int evalue(int playerNumber)
	{
		if(terminal)
		{
			if(regles.estFinie(grille)!=-1)
			{
				return Integer.MAX_VALUE;
			}
			else
			{
				return 0; // A CHANGER : calculer le nombre en fonction du nombre d'alignement pour l'ia et pour l'adversaire
			}
		}
		else
		{
			if(playerNumber==aQuiLeTour)
			{
				int n=0;
				int val=Integer.MIN_VALUE;
				for(int i=0; i<fils.size();i++)
				{
					int tmp = ((IANode)fils.elementAt(i)).evalue(playerNumber);
					if(tmp>=val)
					{
						n=i;
						val = tmp;
					}
				}
				return val;
			}
			else
			{
				int n=0;
				int val=Integer.MAX_VALUE;
				for(int i=0; i<fils.size();i++)
				{
					int tmp = ((IANode)fils.elementAt(i)).evalue(playerNumber);
					if(tmp<=val)
					{
						n=i;
						val = tmp;
					}
				}
				return val;
			}
		}
	}
	
	

}
