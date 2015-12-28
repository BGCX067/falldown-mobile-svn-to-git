import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;


public class Affichage2d extends GameCanvas implements Affichage  {

	private static final int[] COLOR_FOND = new int[]{173,245,250};
	private static final int[] COLOR_QUADRILLAGE = new int[]{16,5,226};
	private static final int[] COLOR_P1 = new int[]{255,0,0};
	private static final int[] COLOR_P2 = new int[]{250,250,0};
	private static final int[] COLOR_TEXT = new int[]{0,0,255};
	
	private Puissance4 p4;
	Buffer buffer;
	
	protected Affichage2d(Puissance4 p4) {
		super(false);
		this.p4 = p4;
		setFullScreenMode(true);
		buffer = Buffer.getBuffer(this, getGraphics());
	}

	private Controleur controleur;
	private Partie partie;
	
	public void setControleur(Controleur c) {
		this.controleur = c;
	}

	public void setPartie(Partie partie) {
		this.partie = partie;
		partie.addAffichage(this);
	}

	public void update() {
		// TODO Auto-generated method stub
		repaint();
	}
	
	public void paint(Graphics g)
	{
		g = buffer.getGraphics();
		
		clearScreen(g);
		drawGrille(g);
		drawCurseur(g);
		drawQuadrillage(g);
		if(partie.etat()==Partie.FINI)
		{
			String s=null;
			if(partie.getVainqueur()==Partie.P1)
			{
				s="Vainqueur : rouge";
			}
			else if (partie.getVainqueur()==Partie.P2)
			{
				s="Vainqueur : jaune";
			}
			else if (partie.getVainqueur()==Partie.EGALITE)
			{
				s="Egalite";
			}
			setColor(g, COLOR_TEXT);
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.drawString(s, g.getClipWidth()/2, g.getClipHeight()/2, Graphics.BOTTOM|Graphics.HCENTER);
		}
		
		buffer.flushGraphics(g);
	}
	
	

	private void setColor(Graphics g, int[] color)
	{
		g.setColor(color[0],color[1],color[2]);
	}
	
	private void clearScreen(Graphics g)
	{
		setColor(g,COLOR_FOND);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
	}
	
	private void drawQuadrillage(Graphics g)
	{
		int h = getHeightCase(g);
		int w = getWidthCase(g);
		setColor(g, COLOR_QUADRILLAGE);
		for(int i=1; i<=partie.numLignes(); i++)
		{
			g.drawLine(0, (i+1)*h,g.getClipWidth(),(i+1)*h);
		}
		for(int i=0; i<=partie.numColonnes(); i++)
		{
			g.drawLine(i*w, h, i*w, g.getClipHeight());
		}
	}
	
	private void drawGrille(Graphics g)
	{
		int h = getHeightCase(g);
		int w = getWidthCase(g);
		
		for(int i=0; i<partie.numColonnes();i++)
		{
			for(int j=0; j<partie.numLignes();j++)
			{
				int val = partie.getCase(i, j);
				if (val==Partie.P1)
				{
					drawPiece(i*w,(j+1)*h,COLOR_P1,g);
				}
				else if (val==Partie.P2)
				{
					drawPiece(i*w,(j+1)*h,COLOR_P2,g);
				}
				
			}
		}
	}
	
	private void drawCurseur(Graphics g)
	{
		int col = partie.getPosCurseur();
		if(partie.etat() == Partie.TOUR_P1)
		{
			drawPiece(col*getWidthCase(g),0,COLOR_P1,g);
		}
		else if(partie.etat() == Partie.TOUR_P2)
		{
			drawPiece(col*getWidthCase(g),0,COLOR_P2,g);
		}
	}
	
	private void drawPiece(int x, int y, int[] color, Graphics g)
	{
		setColor(g, color);
		g.fillArc(x, y, getWidthCase(g), getHeightCase(g), 0, 360);
	}
	
	private int getHeightCase(Graphics g)
	{
		return g.getClipHeight()/(partie.numLignes()+1);
	}
	
	private int getWidthCase(Graphics g)
	{
		return g.getClipWidth()/partie.numColonnes();
	}
	
	protected void keyPressed(int keyCode) {
		if(keyCode==-8)
		{
			p4.notifyDestroyed();
		}
		else if( getGameAction(keyCode)==Canvas.FIRE && partie.etat()==Partie.FINI)
		{
			p4.partieFinie();
		}
		if(controleur!=null)
		{
			controleur.keyPressed(getGameAction(keyCode));
		}
	}
	
	protected void keyReleased(int keyCode) {
		if(controleur!=null)
		{
			controleur.keyReleased(getGameAction(keyCode));
		}
	}
	
}
