import java.io.IOException;

import javax.bluetooth.L2CAPConnection;


public class JoueurDistant implements Joueur, Runnable
{
	private Partie partie;
	private L2CAPConnection connection;
	private boolean receiving = false;
	private int n;
	
	public JoueurDistant(L2CAPConnection connection)
	{
		this.connection = connection;
	}
	
	public void partieModifiee() {
		if(!receiving)
		{
			receiving = true;
			byte[] b  = new byte[4];
			b[0] = Protocole.NEW_GAME;
			b[1] = (byte) partie.numColonnes();
			b[2] = (byte) partie.numLignes();
			b[3] = (byte) partie.etat();
			try {
				connection.send(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread(this).start();
		}
		sendPartie();
	}

	public void setPartie(Partie p, int n) {
		this.partie = p;
		this.n = n;
	}

	/*public void start()
	{
		byte[] b  = new byte[4];
		b[0] = Protocole.NEW_GAME;
		b[1] = (byte) partie.numColonnes();
		b[2] = (byte) partie.numLignes();
		b[3] = (byte) partie.etat();
		try {
			connection.send(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendPartie();
	}*/
	
	public void tourDeJouer() {
		// TODO Auto-generated method stub
		
	}
	
	private void sendPartie()
	{
		byte[] b = new byte[1+partie.numLignes()*partie.numColonnes()+7];
		b[0]=Protocole.GRILLE;
		for(int i=0; i<partie.numColonnes();i++)
		{
			for(int j=0; j<partie.numLignes();j++)
			{
				b[1+j*partie.numColonnes()+i]=(byte)partie.getCase(i, j);
			}
		}
		int i=2+partie.numColonnes()*partie.numLignes();
		
		b[i]=Protocole.CURSEUR;
		b[i+1]=(byte)partie.getPosCurseur();
		b[i+2]=Protocole.ETAT;
		b[i+3]=(byte)partie.etat();
		b[i+4]=Protocole.VAINQUEUR;
		b[i+5]=(byte)partie.getVainqueur();
		
		try {
			connection.send(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while(partie.etat() != Partie.FINI)
		{
			try {
				if(connection.ready())
				{
					byte[] b= new byte[connection.getReceiveMTU()];
					int n = connection.receive(b);
					switch(b[0])
					{
					case Protocole.DROITE:
						partie.allerADroite(this);
						break;
					case Protocole.GAUCHE:
						partie.allerAGauche(this);
						break;
					case Protocole.VALIDER:
						partie.laisserTomberPiece(this);
						break;	
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public int getNumber() {
		return n;
	}
	
}
