import java.io.IOException;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.CustomItem;


public class PartieDistante extends Partie implements Runnable {
	
	private int curseur;
	private int vainqueur;
	private int numLignes=1;
	private int numColonnes=1;
	private int nbTours;
	private int etat;
	private int[][] grille;
	private L2CAPConnection connection;
	
	public PartieDistante(Joueur joueurLocal, L2CAPConnection connection) 
	{
		super(new JoueurNull(),joueurLocal);
		this.connection=connection;
	}

	public void allerADroite(Joueur j) {
		if(etat!=P2) return;
		
		try {
			connection.send(new byte[]{Protocole.DROITE});
		} catch (IOException e) {e.printStackTrace();}
	}

	public void allerAGauche(Joueur j) {
		if(etat!=P2) return;
		
		try {
			connection.send(new byte[]{Protocole.GAUCHE});
		} catch (IOException e) {e.printStackTrace();}
	}

	public void demarrer() {
		new Thread(this).start();
	}

	public int etat() {
		return etat;
	}

	public int getCase(int colonne, int ligne) {
		if(grille == null)return Partie.VIDE;
		return grille[colonne][ligne];
	}

	public int getNbTours() {
		return nbTours;
	}

	public int getPosCurseur() {
		return curseur;
	}

	public int getVainqueur() {
		return vainqueur;
	}

	public void laisserTomberPiece(Joueur j) {
		if(etat!=P2) return;
		try {
			connection.send(new byte[]{Protocole.VALIDER});
		} catch (IOException e) {e.printStackTrace();}
	}

	public int numColonnes() {
		return numColonnes;
	}

	public int numLignes() {
		return numLignes;
	}

	public void run() {
		
		try {
			while(etat!=Partie.FINI)
			{
				try 
				{
					
					byte[] b = new byte[connection.getReceiveMTU()];
					
					if(connection.ready())
					{
						int n = connection.receive(b);
						int i=0;
						while(i<n)
						{
							switch(b[i])
							{
							case Protocole.CURSEUR:
								curseur = b[i+1];
								i+=2;
								break;
							case Protocole.CASE:
								grille[b[i+1]][b[i+2]] = b[i+3];
								i+=4;
								break;
							case Protocole.FINI:
								vainqueur =  b[i+1];
								etat=Partie.FINI;
								i+=2;
								break;
							case Protocole.GRILLE:
								for(int x=0; x< numColonnes; x++)
								{
									for(int y=0; y<numLignes;y++)
									{
										grille[x][y]=b[i+1+numColonnes()*y+x];
									}
								}
								i+=2+numLignes*numColonnes;
								break;
							case Protocole.NEW_GAME:
								numColonnes = b[i+1];
								numLignes = b[i+2];	
								grille = new int[numColonnes][numLignes];
								etat = b[i+3];
								i+=4;
								break;
							case Protocole.ETAT:
								etat = b[i+1];
								if(etat==Partie.P2)
								{
									getPlayer(P2).tourDeJouer();
								}
								i+=2;
								break;
							case Protocole.VAINQUEUR:
								vainqueur = b[i+1];
								i+=2;
								break;
							case Protocole.NB_TOURS:
								nbTours = b[i+1];
								i+=2;
								break;
							}
						}
						informeTous();
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		} catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}

	// history à implémenter
	public Coup getCoup(int tour) {
		return null;
	}
	
	// a implémenter si besoin
	public Grille getGrille(){
		return null;
	}

}
