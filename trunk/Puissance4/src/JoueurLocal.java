import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;


public class JoueurLocal implements Joueur, Controleur {

	private Partie partie;
	private Affichage affichage;
	private boolean monTour = false;
	private int n;
	
	public void partieModifiee() {
		// TODO Auto-generated method stub
		
	}

	public void setPartie(Partie p, int n) {
		this.partie = p;
		this.n = n;
	}
	
	public void setAffichage(Affichage a)
	{
		this.affichage = a;
	}

	public void tourDeJouer() {
		monTour = true;
		affichage.setControleur(this);
	}

	public void keyPressed(int keyCode) {
		if(! monTour) return;
		
		if(keyCode == Canvas.LEFT)
		{
			partie.allerAGauche(this);
		}
		else if (keyCode == Canvas.RIGHT)
		{
			partie.allerADroite(this);
		}
		else if (keyCode == Canvas.FIRE || keyCode == Canvas.DOWN)
		{
			partie.laisserTomberPiece(this);
		}
			
		
	}

	public void keyReleased(int keyCode) {
		// TODO Auto-generated method stub
		
	}

	public int getNumber() {
		return n;
	}

}
