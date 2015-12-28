import java.util.Vector;


abstract public class Partie 
{
	public static final int VIDE = 0;
	public static final int P1 = 1;
	public static final int P2 = 2;
	public static final int EGALITE = 3;
	
	public static final int COMPUTING = 0;
	public static final int TOUR_P1=1;
	public static final int TOUR_P2=2;
	public static final int FINI = 3;
	
	private Joueur[] player;
	
	private Vector affichages = new Vector(2);

	public Partie(Joueur p1, Joueur p2)
	{
		player = new Joueur[2];
		setPlayer(P1, p1);
		setPlayer(P2, p2);
	}
	
	abstract public void demarrer();
	
	public Joueur getPlayer(int n) {
		return player[n-1];
	}

	public void setPlayer(int n,Joueur player) {
		this.player[n-1] = player;
		this.player[n-1].setPartie(this,n);
	}

	public void informeJoueurs()
	{
		player[0].partieModifiee();
		player[1].partieModifiee();
	}
	
	public void addAffichage(Affichage a)
	{
		affichages.addElement(a);
	}
	
	public void removeAffichage(Affichage a)
	{
		affichages.removeElement(a);
	}
	
	public void informeAffichage()
	{
		for(int i=0; i<affichages.size();i++)
		{
			((Affichage)affichages.elementAt(i)).update();
		}
	}
	
	public void informeTous()
	{
		informeAffichage();
		informeJoueurs();
	}
	
	abstract public int getNbTours();
	
	/**
	 *	renvoie la valeur de la case à la position (ligne, colonne)
	 *	Partie.VIDE si vide
	 *	Partie.P1 si occupée par le player 1
	 *	Partie.P2 si occupée par le player 2
	 */
	abstract public int getCase(int colonne, int ligne);
	
	
	abstract public void allerAGauche(Joueur j);
	abstract public void allerADroite(Joueur j);
	abstract public void laisserTomberPiece(Joueur j);
	abstract public int getPosCurseur();
	
	//abstract public void joue(int col, Joueur j);

	abstract public int etat();
	abstract public int getVainqueur();

	abstract public int numLignes();
	
	abstract public int numColonnes();
	abstract public Coup getCoup(int tour);
	
	abstract public Grille getGrille();
}
