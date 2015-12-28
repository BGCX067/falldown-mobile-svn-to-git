import java.util.Random;
import java.util.Vector;

import com.sun.mmedia.GIFPlayer;


public class IA implements Joueur, Runnable {
	private int n;
	private Partie partie;
	private int profondeur = 4;
	private Regles regles;
	private Random random = new Random();
	private static final int[] BAREMES = new int[] {2,3,6,100};
	private static final int MAX = 1000000;
	
	public int getNumber() {
		
		return n;
	}

	public void partieModifiee() {
	}

	public void setPartie(Partie p, int n) {
		this.n = n;
		this.partie = p;
	}

	public void run() {
		//System.out.println("computing");
		regles = ((PartieLocale)partie).getRegles();
		Grille g = partie.getGrille();
		Vector vcol=new Vector();
		int val = Integer.MIN_VALUE;
		for(int i=0; i<g.numColonnes();i++)
		{
			if(regles.estUnCoupValide(g, i))
			{
				g.faitTomberPiece(n, i);
				int tmp = evalue(g,n == Partie.P1 ? Partie.P2 : Partie.P1, profondeur-1, i, val);
				//System.out.println(tmp);System.out.println(); 
				//System.out.println(i +" : " + tmp);
				if(tmp>=val)
				{
					if(val==tmp)
					{
						vcol.addElement(new Integer(i));
					}
					else
					{
						vcol.removeAllElements(); 
						vcol.addElement(new Integer(i));
						val = tmp;
					}
				}
				g.retire(i);
			}
		}
		int col = ((Integer)vcol.elementAt(random.nextInt(vcol.size()))).intValue();
		while(partie.getPosCurseur()<col)
		{
			partie.allerADroite(this);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(partie.getPosCurseur()>col)
		{
			partie.allerAGauche(this);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		partie.laisserTomberPiece(this);
		
	}
	
	public void tourDeJouer() {
		new Thread(this).start();
	}
	
	private int evalue(Grille grille, int aQuiLeTour, int hauteur, int coup,int alphabeta)
	{
		//printGrille(grille);
		if(aQuiLeTour == n) // c'est à l'ia de jouer => MAXIMISANT
		{
			if(regles.estFinie(grille, coup)!=-1)
			{
				return Integer.MIN_VALUE+100-hauteur;
			}
			else if(regles.estFinie(grille)!=-1 || hauteur == 0)
			{
				 return 0;
				/*int a = evalue(grille,coup);
				System.out.println(a);
				return a;*/
				//return 0; // A REMPLACER PAR UNE FONCTION D'EVALUATION DE LA GRILLE EN tenant compte du nombre d'alignements
				
			}
			else
			{
				int val=Integer.MIN_VALUE;
				for(int i=0; i<grille.numColonnes();i++)
				{
					if(regles.estUnCoupValide(grille, i))
					{
						grille.faitTomberPiece(aQuiLeTour, i);
						int tmp = evalue(grille,aQuiLeTour == Partie.P1 ? Partie.P2 : Partie.P1, hauteur-1, i,val);
						//System.out.println(tmp);System.out.println(); 
						if(tmp>=val)
						{
							val = tmp;
							if(val>alphabeta)
							{
								grille.retire(i);
								return val;
							}
						}
						grille.retire(i);
					}
				}
				return val;
			}	
		}
		else // c'est l'adversaire de l'ia qui jouera ce coup => MINIMISANT
		{
			if(regles.estFinie(grille,coup)!=-1)
			{
				return Integer.MAX_VALUE-100+hauteur;
			}
			else if(regles.estFinie(grille)!=-1 || hauteur == 0)
			{
				/*int a = evalue(grille,coup);
				System.out.println(a);*/
				return 0;
			}
			else
			{
				int val=Integer.MAX_VALUE;
				for(int i=0; i<grille.numColonnes();i++)
				{
					if(regles.estUnCoupValide(grille, i))
					{
						grille.faitTomberPiece(aQuiLeTour, i);
						int tmp = evalue(grille,aQuiLeTour == Partie.P1 ? Partie.P2 : Partie.P1, hauteur-1,i,val);
						//System.out.println(tmp);System.out.println(); 
						if(tmp<=val)
						{
							val = tmp;
							if(val<alphabeta)
							{
								grille.retire(i);
								return val;
							}
						}
						grille.retire(i);
					}
				}
				return val;
			}
		}
	}
	
	private void printGrille(Grille grille) {
		for(int j=0; j<grille.numLignes();j++)
		{
			for(int i=0;i<grille.numColonnes();i++)
			{
				System.out.print(grille.getCase(i, j)+" ");
			}
			System.out.println();
		}
	}

	private static int evalue(Grille grille, int coup)
	{
		int val = 0;
		int[] alignements = new int[4];
		int ligne;
		for(ligne=0;ligne<grille.numLignes() && grille.getCase(coup,ligne)==Partie.VIDE;ligne++);
		if(ligne==grille.numLignes())ligne = grille.numLignes()-1;
		
		int[][] dir = new int[][]{ {-1,0},{-1,1},{-1,-1},{0,-1}};
		for(int i=0; i<dir.length; i++)
			numAlignement(grille,coup,ligne,dir[i][0],dir[i][1],alignements);
	
		for(int i=0; i<alignements.length;i++)
		{
			val+=(alignements[i]*BAREMES[i]);
		}
		return val;
	}
	
	private static void numAlignement(Grille grille, int x, int y, int dx, int dy, int[] res)
	{
		for(int d=-3;d<=0; d++)
		{
			if(x+dx*d>=0 && x+dx*d<grille.numColonnes() && y+d*dy >=0 && y+dy*d<grille.numLignes())
			{
				int n = numDansAlignement(grille, grille.getCase(x, y), x+d*dx, y+d*dy, dx, dy);
				if (n!=-1)
				{
					res[n-1]++;
				}
			}
		}
	}
	
	private static int numDansAlignement(Grille grille, int player,int x, int y, int dx, int dy)	
	{
		if( x+dx*4<0 || x+dx*4>=grille.numColonnes() || y+4*dy <0 || y+dy*4>=grille.numLignes())return -1;
		int n=0;
		for(int d=0;d<4;d++)
		{
			if(grille.getCase(x+dx*d, y+dy*d)==player)
			{
				n++;
			}
			else if (grille.getCase(x+dx*d, y+dy*d)!=grille.VIDE)
			{
				return -1;
			}
		}
		return n;
	}

	public static void main(String[] args)
	{
		Grille g = new Grille(7,6);
		g.faitTomberPiece(Partie.P1, 3);
		g.faitTomberPiece(Partie.P1, 3);
		g.faitTomberPiece(Partie.P1, 4);
		System.out.println(evalue(g, 3));
		g.retire(3);g.retire(3);
		g.faitTomberPiece(Partie.P1, 0);
		System.out.println(evalue(g, 0));
		//g.faitTomberPiece(Partie.P1, 4);
		//g.faitTomberPiece(Partie.P2, 3);
		int tab[] = new int[3];
		
		/*numAlignement(g, 2,5,  1, 0, tab);
		for(int i=0;i<tab.length;i++)
			System.out.print(tab[i]+" ");
		System.out.println();*/
	}
	
}
