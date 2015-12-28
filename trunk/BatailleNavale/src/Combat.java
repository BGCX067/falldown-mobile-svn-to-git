import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;



public class Combat extends GameCanvas implements Runnable {
	public static final int INCONNU = 0;
	public static final int TOUCHE = 1;
	public static final int VIDE = 2;
	public static final int COULE = 3; // pas sur que ce sera implemente
	public static final int OCCUPE = 4;
	
	
	public static final int TIR=1;
	public static final int PLACEMENT = 2;
	public static final int TIR_ENEMI = 3;
	public static final int TEXT = 4;
	public static final int FINI_SHOW_ENNEMI = 5;
	public static final int FINI_SHOW_US = 6;
	public static final int ATTENTE_PLACEMENT_ADVERSE = 7;
	
	public static final int[] COLOR_OCEAN = {100,100,255};
	public static final int[] COLOR_TOUCHE = {255,200,100};
	public static final int[] COLOR_VIDE = {100,255,100};
	public static final int[] COLOR_COULE = {255,100,100};
	public static final int[] COLOR_CIBLE = {255,0,0};
	public static final int[] COLOR_NAVIRE = {130,130,130};
	
	private int mode;
	private boolean aChoisiCible = false;
	
	BatailleNavale bn;
	
	Grille grille = new Grille();
	Vector navires = new Vector();
	
	boolean monTour=false;
	int[][] ennemis = new int[10][10];
	int[] cible = new int[2];
	
	L2CAPConnection connection;
	boolean fini;
	
	String affichage="";
	boolean text = false;
	
	Vector naviresAPlacer;
	Navire curNavire;
	int xPlacement=0;
	int yPlacement=0;
	int dirPlacement=Grille.HORIZONTAL;
	
	int[] coordEnemi = new int[2];
	Buffer buffer;
	
	public Combat(BatailleNavale bn, L2CAPConnection adversaire, boolean commence)
	{
		super(false);
		this.bn = bn;
		setFullScreenMode(true);
		Display.getDisplay(bn).setCurrent(this);
		buffer = Buffer.getBuffer(this, getGraphics());
		monTour = commence;
		connection = adversaire;
		
		naviresAPlacer=new Vector();
		naviresAPlacer.addElement(new Navire(2));
		naviresAPlacer.addElement(new Navire(3));
		naviresAPlacer.addElement(new Navire(3));
		naviresAPlacer.addElement(new Navire(4));
		naviresAPlacer.addElement(new Navire(5));
		
		for(int i=0; i<naviresAPlacer.size();i++)
		{
			navires.addElement(naviresAPlacer.elementAt(i));
		}
		
		fini = false;
		
	}

	public void start()
	{
		new Thread(this).start();
	}
	
	public void run()
	{
		placerLesNavires();
		
		setMode(ATTENTE_PLACEMENT_ADVERSE);
		if(monTour)
		{
			try {
				connection.send(new byte[]{Protocole.PRET});
				
				while(!connection.ready())
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				connection.receive(new byte[]{});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				while(!connection.ready())
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				connection.send(new byte[]{Protocole.PRET});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		while(!fini)
		{
			if(monTour)
			{
				setMode(TIR);
				affichage = "choisissez votre cible";
				text=true;
				repaint();
				int[] coord = obtenirCoordonneCible();
				affichage = "attente de la reponse";
				text = true; repaint();
				int degat = tirer(coord);
				
				String s="";
				if(degat == Protocole.TOUCHE)
				{
					s = "touché !";
					ennemis[coord[0]][coord[1]]=TOUCHE;
				}
				else if (degat == Protocole.RATE)
				{
					s = "coup dans l'eau";
					ennemis[coord[0]][coord[1]]=VIDE;
				}
				else if (degat==Protocole.COULE)
				{
					s = "coulé !";
					ennemis[coord[0]][coord[1]]=TOUCHE;
					coule(coord[0],coord[1]);
				}
				else if (degat==Protocole.FINI)
				{
					s = "partie finie";
					ennemis[coord[0]][coord[1]]=TOUCHE;
					coule(coord[0],coord[1]);
					fini = true;
				}
				else if (degat==Protocole.INVALIDE)
				{
					s = "tir invalide";
				}
				
				affiche(s, 2000);
				
				if(degat==Protocole.FINI)
				{
					Vector v = grille.elementsIntacts();
					byte[] c = new byte[v.size()+1];
					for(int i=0; i<v.size();i++)
					{
						c[i]=((Byte)v.elementAt(i)).byteValue();
					}
					try {
						connection.send(c);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			else
			{
				setMode(TIR_ENEMI);
				affichage = "ATTENTE TIR ENNEMI";
				text = true;
				repaint();
				System.out.println("Son tour");
				int[] coord = recevoirTirEnnemi();
				
				System.out.println("ATTAQUE : " + coord[0] + " " + coord[1]+")");
				byte[] b = new byte[1];
				b[0] = (byte) grille.bombarde(coord[0], coord[1]);
				if(b[0]==Protocole.COULE && flotteCoulee())
				{
					b[0]=Protocole.FINI;
					fini =true;
				
				}
				try {
					connection.send(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//affiche("tir ennemi : (" + coord[0]+", "+coord[1]+")", 2000);
				String s="";
				switch(b[0])
				{
				case Protocole.RATE:
					s = "coup dans l'eau";
					break;
				case Protocole.TOUCHE:
					s = "touché !";
					break;
				case Protocole.COULE:
					s = "coulé !";
					break;
				case Protocole.FINI:
					fini=true;
					s = "partie finie";
					break;
				case Protocole.INVALIDE:
					s = "tir invalide";
					break;
				}
				affiche(s, 2000);
				
				
				if(b[0]==Protocole.FINI) // recevoir les infos sur le positionnement des bateaux ennemis
				{
					try {
						while(!connection.ready())
						{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						b = new byte[connection.getReceiveMTU()];
						int n = connection.receive(b);
						for(int i=0; i<n-1;i+=2)
						{
							ennemis[b[i]][b[i+1]]=OCCUPE;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			monTour = !monTour;
		}
		setMode(FINI_SHOW_ENNEMI);
	}
	
	private void coule(int x, int y) {
		if(x<0 || y<0 || x>=ennemis.length || y>=ennemis[0].length|| ennemis[x][y]!=TOUCHE)
			return;
		
		ennemis[x][y]=COULE;
		
		coule(x+1,y); coule(x-1,y); coule(x,y-1); coule(x,y+1);
		
	}

	private int[] recevoirTirEnnemi() {
		setMode(TIR_ENEMI);
		try {
			while(!connection.ready())
			{
				Thread.sleep(15);
			}
			byte[] b = new byte[3];
			connection.receive(b);
			while(!(b[0]==Protocole.TIR))
			{
				if(b[0]==Protocole.CIBLE)
				{
					coordEnemi[0]=b[1];
					coordEnemi[1]=b[2];
				}
				repaint();
				while(!connection.ready())
				{
					Thread.sleep(15);
				}
				connection.receive(b);
			}
			return new int[]{b[1],b[2]};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private int[] obtenirCoordonneCible() {
		
		setMode(TIR);
		aChoisiCible = false;
		while(!aChoisiCible)
		{
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
		return cible;
		
	}
	
	private int tirer(int[] coord)
	{
		byte[] b = new byte[3];
		b[0]=Protocole.TIR;
		b[1]=(byte)coord[0];
		b[2]=(byte)coord[1];
		try {
			
			connection.send(b);
			while(!connection.ready())
			{
				Thread.sleep(15);
			}
			b = new byte[connection.getReceiveMTU()];
			connection.receive(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return b[0];
	}

	private void placerLesNavires() {
		setMode(PLACEMENT);
		curNavire = (Navire) naviresAPlacer.elementAt(0);
		while(naviresAPlacer.size() > 0)
		{
			repaint();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setMode(int mode)
	{
		this.mode = mode;
		repaint();
	}
	
	protected void keyPressed(int keyCode) {
		if(keyCode==-8)
		{
			bn.notifyDestroyed();
		}
		
		switch(mode)
		{
		case TIR:
			if(aChoisiCible)return;
			boolean moved = false;
			if (getGameAction(keyCode) == Canvas.FIRE)
			{
				if(ennemis[cible[0]][cible[1]]==INCONNU)
				{
					aChoisiCible=true;
				}
			}
			else if(getGameAction(keyCode)==Canvas.UP)
			{
				if(cible[1]>0)
				{
					cible[1]--; 
					moved = true;
				}
				
			}
			else if (getGameAction(keyCode)==Canvas.DOWN)
			{
				if(cible[1]<ennemis[0].length-1)
				{
					cible[1]++;
					moved = true;
				}
					
			}
			else if (getGameAction(keyCode)==Canvas.LEFT)
			{
				if(cible[0]>0)
				{
					cible[0]--;
					moved = true;
				}
			}
			else if (getGameAction(keyCode)==Canvas.RIGHT)
			{
				if(cible[0]<ennemis.length-1)
				{
					cible[0]++;
					moved = true;
				}
			}
			
			if(moved)
			{
				try {
					connection.send(new byte[]{Protocole.CIBLE,(byte)cible[0],(byte)cible[1]});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case PLACEMENT:
			if(getGameAction(keyCode)==Canvas.UP)
			{
				if(yPlacement>0)yPlacement--;
			}
			else if (getGameAction(keyCode)==Canvas.DOWN)
			{
				if( (dirPlacement==Grille.HORIZONTAL && yPlacement<grille.getHeight()-1) || 
						(dirPlacement==Grille.VERTICAL && yPlacement+curNavire.taille()<grille.getHeight()))
					yPlacement++;
			}
			else if (getGameAction(keyCode)==Canvas.LEFT)
			{
				if(xPlacement>0)
					xPlacement--;
			}
			else if (getGameAction(keyCode)==Canvas.RIGHT)
			{
				if( (dirPlacement==Grille.VERTICAL && xPlacement<grille.getWidth()-1) || 
						(dirPlacement==Grille.HORIZONTAL && xPlacement+curNavire.taille()<grille.getWidth()))
					xPlacement++;
			}
			else if (getGameAction(keyCode) == Canvas.FIRE)
			{
				if(grille.estUnPlacementPossible(curNavire, xPlacement, yPlacement, dirPlacement))
				{
					grille.place(curNavire, xPlacement, yPlacement, dirPlacement);
					naviresAPlacer.removeElement(curNavire);
					if (naviresAPlacer.size()>0)
					{
						curNavire = (Navire) naviresAPlacer.elementAt(0);
						xPlacement = 0;
						yPlacement = 0;
						dirPlacement = Grille.HORIZONTAL;
					}
				}
				
			}
			else if (getGameAction(keyCode) == Canvas.KEY_STAR || getGameAction(keyCode)==Canvas.GAME_A)
			{
				if(dirPlacement==Grille.HORIZONTAL)
				{
					
					dirPlacement=Grille.VERTICAL;
					if(yPlacement+curNavire.taille()>grille.getHeight())
					{
						yPlacement = grille.getHeight()-curNavire.taille();
					}
				}
				else
				{
					dirPlacement=Grille.HORIZONTAL;
					if(xPlacement+curNavire.taille()>grille.getWidth())
					{
						xPlacement = grille.getWidth()-curNavire.taille();
					}
				}
			}
			break;
		case FINI_SHOW_ENNEMI:
			if (getGameAction(keyCode) == Canvas.FIRE)
			{
				bn.notifyDestroyed();
			}
			else if (getGameAction(keyCode) == Canvas.LEFT || getGameAction(keyCode) == Canvas.RIGHT)
			{
				setMode(FINI_SHOW_US);
			}
			break;
		case FINI_SHOW_US:
			if (getGameAction(keyCode) == Canvas.FIRE)
			{
				bn.notifyDestroyed();
			}
			else if (getGameAction(keyCode) == Canvas.LEFT || getGameAction(keyCode) == Canvas.RIGHT)
			{
				setMode(FINI_SHOW_ENNEMI);
			}
			break;
		}
		
			
	}
	
	
	

	
	private void drawCible(int[] coord, Graphics g)
	{
		int widthCell = g.getClipWidth()/ennemis.length;
		int heightCell = g.getClipHeight()/ennemis[0].length;
		g.setColor(255, 100, 100);
		g.fillArc(coord[0]*widthCell, coord[1]*heightCell, widthCell, heightCell, 0, 360);
		g.setColor(0,0,0);
		g.drawArc(coord[0]*widthCell, coord[1]*heightCell, widthCell, heightCell, 0, 360);
	}
	
	public void paint(Graphics g) {
		Graphics h = buffer.getGraphics();
		h.setColor(COLOR_OCEAN[0],COLOR_OCEAN[1],COLOR_OCEAN[2]);
		h.fillRect(0, 0, h.getClipWidth(), h.getClipHeight());
		h.translate((h.getClipWidth()%ennemis.length)/2, (h.getClipHeight()%ennemis[0].length)/2);
		switch(mode)
		{
			case TIR:
				draw(ennemis,h);
				drawCible(cible,h);
				break;
			case TIR_ENEMI:
				draw(grille,h);
				drawCible(coordEnemi,h);
				break;
			case PLACEMENT:
				draw(grille,h);
				if (curNavire!=null)
				{
					draw(curNavire, xPlacement, yPlacement, dirPlacement,h);
				}
				break;
			case FINI_SHOW_ENNEMI:
				draw(ennemis,h);
				text = true;
				affichage  = "fini : flotte ennemie";
				break;
			case FINI_SHOW_US:
				draw(grille,h);
				text = true;
				affichage  = "fini : votre flotte";
				break;
			case ATTENTE_PLACEMENT_ADVERSE:
				draw(grille,h);
				text = true;
				affichage  = "Attente adversaire";
		}
		
		if(text)
		{
			h.setColor(200, 0, 0);
			h.drawString(affichage, h.getClipWidth()/2, h.getClipHeight()/2, Graphics.BOTTOM|Graphics.HCENTER);
		}
		
		buffer.flushGraphics(h);
		h.translate(-(h.getClipWidth()%ennemis.length)/2, -(h.getClipHeight()%ennemis[0].length)/2);
	}
	
	public void draw(int[][] ennemis, Graphics g)
	{
		int widthCell = g.getClipWidth()/ennemis.length;
		int heightCell = g.getClipHeight()/ennemis[0].length;
		for(int i=0; i<ennemis.length; i++)
		{
			for(int j=0; j<ennemis[i].length;j++)
			{
				switch(ennemis[i][j])
				{
				case INCONNU:
					g.setColor(COLOR_OCEAN[0], COLOR_OCEAN[1], COLOR_OCEAN[2]);
					break;
				case VIDE :
					g.setColor(COLOR_VIDE[0], COLOR_VIDE[1], COLOR_VIDE[2]);
					break;
				case TOUCHE :
					g.setColor(COLOR_TOUCHE[0], COLOR_TOUCHE[1], COLOR_TOUCHE[2]);
					break;
				case COULE :
					g.setColor(COLOR_COULE[0], COLOR_COULE[1], COLOR_COULE[2]);
					break;
				case OCCUPE :
					g.setColor(COLOR_NAVIRE[0], COLOR_NAVIRE[1], COLOR_NAVIRE[2]);
					break;
				}
				g.fillRect(i*widthCell, j*heightCell, widthCell, heightCell);
			}
		}
		drawQuadrillage(g);
	}
	
	private void draw(Navire curNavire, int xPlacement, int yPlacement, int dirPlacement, Graphics g) {
		int widthCell = g.getClipWidth()/ennemis.length;
		int heightCell = g.getClipHeight()/ennemis[0].length;
		g.setColor(COLOR_NAVIRE[0], COLOR_NAVIRE[1], COLOR_NAVIRE[2]);
		if(dirPlacement==grille.HORIZONTAL)
		{
			for(int i=0; i<curNavire.taille(); i++)
			{
				g.fillRect((xPlacement+i)*widthCell, yPlacement*heightCell, widthCell, heightCell);
			}
		}
		else
		{
			for(int i=0; i<curNavire.taille(); i++)
			{
				g.fillRect(xPlacement*widthCell, (yPlacement+i)*heightCell, widthCell, heightCell);
			}
		}
	}

	// affiche un texte pour un temps limité
	private void affiche(String s, long delais)
	{
		affichage = s;
		text = true;
		long debut = System.currentTimeMillis();
		repaint();
		while(System.currentTimeMillis()-debut < delais)
		{
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		text = false;
	}
	
	public void draw(Grille grille, Graphics g)
	{
		int widthCell = g.getClipWidth()/ennemis.length;
		int heightCell = g.getClipHeight()/ennemis[0].length;
		for(int i=0; i<grille.getWidth(); i++)
		{
			for(int j=0; j<grille.getHeight(); j++)
			{
				Case c = grille.getCase(i, j);
				if(c.bombardee() && c.estOccupee())
				{
					if(c.occupant().estCoule())
						g.setColor(COLOR_COULE[0],COLOR_COULE[1],COLOR_COULE[2]);
					else	
						g.setColor(COLOR_TOUCHE[0], COLOR_TOUCHE[1], COLOR_TOUCHE[2]);
				}
				else if (c.bombardee() && ! c.estOccupee())
				{
					g.setColor(COLOR_VIDE[0], COLOR_VIDE[1], COLOR_VIDE[2]);
				}
				else if (!c.bombardee() && c.estOccupee())
				{
					g.setColor(COLOR_NAVIRE[0], COLOR_NAVIRE[1], COLOR_NAVIRE[2]); 
				}
				else if (!c.bombardee() && !c.estOccupee())
				{
					g.setColor(COLOR_OCEAN[0], COLOR_OCEAN[1], COLOR_OCEAN[2]);
				}
				g.fillRect(i*widthCell, j*heightCell, widthCell, heightCell);
			}
		}
		drawQuadrillage(g);
	}
	
	public void drawQuadrillage(Graphics g)
	{
		int widthCell = g.getClipWidth()/ennemis.length;
		int heightCell = g.getClipHeight()/ennemis[0].length;
		g.setColor(0,0,0);
		for(int i=0; i<=ennemis.length; i++)
		{
			g.drawLine(i*widthCell, 0,i*widthCell, heightCell*ennemis[0].length);
		}
		for(int i=0; i<=ennemis[0].length; i++)
		{
			g.drawLine(0, i*heightCell,widthCell*ennemis.length, i*heightCell);
		}
	}
	
	public boolean flotteCoulee()
	{
		for(int i=0; i<navires.size();i++)
		{
			if(!((Navire)navires.elementAt(i)).estCoule())
			{
				return false;
			}
		}
		return true;
	}
}
