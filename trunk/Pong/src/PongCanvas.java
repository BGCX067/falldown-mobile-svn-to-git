import java.io.IOException;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;


public class PongCanvas extends GameCanvas implements Runnable
{
	
	public static int SINGLE = 1;
	public static int SERVER = 2;
	public static int CLIENT = 3;
	
	private static int GAME = 1;
	private static int SCORE = 2;
	private static int FIN = 3;
	private static int WAITING = 4;
	
	private int typeDePartie;
	
	
	boolean gameRunning=false;
	int timeStep = 20; 
	Thread gameThread;
	Pong midlet;
	
	Terrain terrain;
	TiledLayer murs;
	Balle balle;
	
	Image imTerrain;
	Image imRaquettePl1;
	Image imRaquettePl2;
	Image imBalle;
	Image imGoal; 
	
	Player p1;
	Player p2;

	int state;
	long scoreBegin;
	long dureeScore = 1000;
	long dureeFin = 2000;
	
	int keyState;
	
	// variables utilisÃ©es si l'application est client
	Client client;
	
	PongCanvas(Pong midlet)
	{
		super(false);
		this.midlet = midlet;
	}

	public void start()
	{
		gameRunning = true;
		state = GAME;
		gameThread = new Thread(this);
		System.out.println("thread créé");
		gameThread.start();
	}
	
	public void run() 
	{
		Graphics g = getGraphics();
		
		if(typeDePartie==SERVER)
		{
			state=WAITING;
		}
		else if (typeDePartie == CLIENT)
		{
			client = new Client(this);
			timeStep=timeStep/2-1;
			state = WAITING;
		}
		
		while (gameRunning)
		{
			keyState = getKeyStates();
			if((keyState & GameCanvas.GAME_B_PRESSED) != 0)
			{
				
				if(state==WAITING) // TODO stopper les threads de connection
				{
					if (typeDePartie == SERVER)
					{
						//remotePlayer.stop()   // ou un truc du genre
					}
					else
					{
						// client.stop()   // ou un truc du genre
					}
				}
				gameRunning = false;
				midlet.finPartie(null);
			}
			
			if(typeDePartie!=CLIENT)
			{
				if (state==GAME)
				{
					tick();
					input();
				}
				else if (state == SCORE)
				{
					if (System.currentTimeMillis() - scoreBegin > dureeScore)
					{
						state = GAME;
						engagement();
					}
				}
				else if (state == FIN)
				{
					if (System.currentTimeMillis() - scoreBegin > dureeFin)
					{
						gameRunning = false;
						Player p=p2;
						if (p1.score>p2.score)p=p1;
						midlet.finPartie(p);
					}
				}
				if(typeDePartie == SERVER)
				{
					if(state == WAITING)
					{
						RemotePlayer pr = (RemotePlayer) p2;
						if(pr.connected())
						{
							state = GAME;
						}
					}
					else
					{
						((RemotePlayer) p2).actualise();
					}
				}
			}
			else  //on est un client
			{
				if(state==WAITING)
				{
					if(client.connected())
					{
						state = GAME;
					}
				}
				else
				{
					client.sendInput();
					client.actualiseGame();
				}
			}
			
			render(g);
			
			try
			{
				Thread.sleep(timeStep);
			}
			catch (InterruptedException e){ stop();}
		}
		
		if(state==SERVER)
		{
			((RemotePlayer) p2).fini();
		}
	}
	
	public void stop()
	{
		
	}
	
	public void tick()
	{
		balle.move();
	}
	
	public void input()
	{
		if(typeDePartie == SINGLE)
		{
			p1.input(keyState);
			p2.input(0);
		}
		else if (typeDePartie == SERVER)
		{
			p1.input(keyState);
			p2.input(0);
		}
		
	}
	
	// TODO improvment : ne pas redessiner le terrain. Juste effacer les elements mobiles
	public void render(Graphics g)
	{
		g.setColor(255,255,255);
		g.fillRect(0, 0, getWidth(), getHeight());
		murs.paint(g);
		p1.goal.paint(g);
		p2.goal.paint(g);
		
		Raquette[] r = p1.getRaquettes();
		for(int i=0; i<r.length;i++)
		{
			r[i].paint(g);
		}
		r = p2.getRaquettes();
		for(int i=0; i<r.length;i++)
		{
			r[i].paint(g);
		}
		balle.paint(g);
		
		if (state == SCORE)
		{
			printScore(g);
		}
		if(state == FIN)
		{
			printWinner(g);
			printScore(g);
		}
		if(state == WAITING)
		{
			g.setColor(255,125,75);
			g.drawString("WAITING",176/2, 220/2, Graphics.BASELINE | Graphics.HCENTER);
		}
		
		flushGraphics();
		
	}
	
	private void printWinner(Graphics g)
	{
		g.setColor(255,125,75);
		if(p1.score>p2.score)
			g.drawString("Player 1 won",176/2, 220/2-35, Graphics.BASELINE | Graphics.HCENTER);
		else
			g.drawString("Player 2 won",176/2, 220/2-35, Graphics.BASELINE | Graphics.HCENTER);
	}
	
	private void printScore(Graphics g)
	{
		g.setColor(255,125,75);
		g.drawString(""+p1.getScore()+" - "+p2.getScore(), 176/2, 220/2, Graphics.BASELINE | Graphics.HCENTER);
	}
	
	
	
	
	
	public Layer checkCollision(Balle b)
	{
		// collision avec terrain
		if (b.collidesWith(murs, false)) return murs;
		
		// collision avec goal
		if (b.collidesWith(p1.goal, true)) {if(b==balle)goal(p1);return p1.goal;}
		if (b.collidesWith(p2.goal, true)) {if(b==balle)goal(p2);return p2.goal;}
		
		//collision avec raquettes
		Raquette[] r = p1.getRaquettes();
		for (int i=0; i<r.length;i++)
		{
			if(b.collidesWith(r[i], true)) return r[i];
		}
		r = p2.getRaquettes();
		for (int i=0; i<r.length;i++)
		{
			if(b.collidesWith(r[i], true)) return r[i];
		}
		
		
		return null;
	}
	
	public Layer checkCollision(Raquette r)
	{
		if (r.collidesWith(murs, false)) return murs;
		else if(r.collidesWith(balle, true)) return balle;
		else return null;
	}
	
	public Layer checkCollision(Sprite s)
	{
		if (s.collidesWith(murs, false)) return murs;
		else return null;
	}
	
	public void goal(Player p)
	{
		p.marque();
		state = SCORE;
		scoreBegin = System.currentTimeMillis();
		if (p.getScore()==5)
		{
			winner(p);
		}
	}
	
	private void winner(Player p)
	{
		// do winner things
		state = FIN;
		/*gameRunning = false;
		midlet.finPartie(p);*/
	}
	
	private void engagement()
	{
		terrain.repositionne();
		balle.speed = balle.min_speed;
	}
	
	public void exit()
	{
		gameRunning=false;
	}
	
	public void newGame(Player p1, Player p2, int typeDePartie)
	{
		System.out.println(""+typeDePartie);
		this.typeDePartie=typeDePartie;
		this.p1 = new Player(null, Player.UP);
		if(typeDePartie == SINGLE)
		{
			this.p2 = new CPU(null,Player.DOWN,this,p1);
		}
		else if (typeDePartie == SERVER)
		{
			this.p2 = new RemotePlayer(null,Player.DOWN, this);
		}
		else if (typeDePartie == CLIENT)
		{
			this.p2 = new Player(null, Player.DOWN);
		}
		terrain = new Terrain(Terrain.STANDARD,this);
		terrain.build();
		initScore();
		engagement();
		start();
	}
	
	public void initScore()
	{
		p1.score = 0;
		p2.score = 0;
	}
	
	public Graphics getGraphics()
	{
		return super.getGraphics();
	}

	
	// methode auxiliaire qui convertit un int en un tableau de 2 byte
	public static byte[] toByte(int a)
	{
		byte[] result = new byte[2];
		result[0]=(byte)a;
		a=a>>8;
		result[1]=(byte)a;
		
		return null;
	}
	
	public static int toInt(byte[] b)
	{
		int result=0;
		result  += b[1];
		result = result << 8;
		result += b[0];
		return result;
	}
	
}
