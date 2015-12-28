import java.util.Random;
import java.util.Vector;



public class PartieLocale extends Partie {

	private Regles regles;
	Grille grille;
	private int nbTours=0;
	private int curseur=0;
	private Random random = new Random();
	private int etat;
	private int vainqueur;
	
	private Vector coups = new Vector(42);


	public PartieLocale(Joueur p1, Joueur p2, Regles regles)
	{
		super(p1, p2);
		this.regles = regles;
		grille = new Grille(regles.getNumColonnes(),regles.getNumLignes());

	}
	
	public void demarrer()
	{
		// tirer au sort qui jouera en premier
		if(random.nextInt(2)==0)
		{
			etat = Partie.TOUR_P1;
			getPlayer(P1).tourDeJouer();
		}
		else
		{
			etat = Partie.TOUR_P2;
			getPlayer(P2).tourDeJouer();
		}
		informeTous();
	}
	
/*	public void joue(int col, Joueur j)
	{
		if(regles.estUnCoupValide(this, col))
		{
			int value;
			if(j==getPlayer1())
			{
				value = P1;
			}
			else
			{
				value = P2;
			}
			grille.faitTomberPiece(value,col);
		}
	}
	*/
	
	public int getCase(int ligne, int colonne)
	{
		return grille.getCase(ligne, colonne);
	}
	
	public Regles getRegles() {
		return regles;
	}

	public int getNbTours() {
		return coups.size();
	}

	public int etat() {
		return etat;
	}

	public int numColonnes() {
		
		return grille.numColonnes();
	}

	public int numLignes() {
		return grille.numLignes();
	}

	public void allerADroite(Joueur j) {
		// TODO Auto-generated method stub
		if((etat==P1 && j!=getPlayer(P1)) || (etat==P2 && j!=getPlayer(P2)))
			return;
		
		if(curseur>=grille.numColonnes()-1)return;
		curseur++;
		informeTous();
	}

	public void allerAGauche(Joueur j) {
		if((etat==P1 && j!=getPlayer(P1)) || (etat==P2 && j!=getPlayer(P2)))
			return;
		
		if(curseur<=0) return;
		curseur--;
		informeTous();
	}

	public int getPosCurseur() {
		return curseur;
	}

	public void laisserTomberPiece(Joueur j) {
		if((etat==P1 && j!=getPlayer(P1)) || (etat==P2 && j!=getPlayer(P2)))
			return;
		
		if(! regles.estUnCoupValide(grille, curseur))
			return;
		
		grille.faitTomberPiece(etat, curseur);
		coups.addElement(new Coup(curseur, j==getPlayer(P1) ? P1:P2 ));
		int a = regles.estFinie(grille, curseur);
		if(a==-1)
		{
			changerTour();
			
		}
		else
		{
			vainqueur = a;
			etat=FINI;
		}
		informeTous();
	}



	private void changerTour()
	{
		if(etat == TOUR_P1)
		{
			etat = TOUR_P2;
			getPlayer(P2).tourDeJouer();
		}
		else
		{
			etat = TOUR_P1;
			getPlayer(P1).tourDeJouer();
		}
	}
	
	public int getVainqueur() {
		return vainqueur;
	}

	public Coup getCoup(int tour) {
		return (Coup)(coups.elementAt(tour));
	}
	
	public Grille getGrille()
	{
		return new Grille(grille);
	}
}
