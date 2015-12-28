import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;


public class FalldownCanvas extends GameCanvas
{

	LinkedList toWipe = new LinkedList(20);

	double rapportWidth;
	double rapportHeight;

	Game game;
	Graphics graphics;
	Buffer buffer;

	boolean showBest = true;

	Color back  = new Color(200,200,255);

	Color[] ballColors = new Color[]{Color.red,new Color(10,220,10),new Color(0,0,0),new Color(0,0,255)};

	private boolean localPlayerIsOver = false;

	public FalldownCanvas()
	{
		super(true);
		setFullScreenMode(true);
		buffer = Buffer.getBuffer(this, getGraphics());
		graphics = buffer.getGraphics();
	}

	public void setGame(Game g)
	{
		game = g;
		setFullScreenMode(true);
		rapportWidth = (double) graphics.getClipWidth()/game.gamePlay.getWidth();
		rapportHeight = (double)graphics.getClipHeight() / game.gamePlay.getHeight();
		localPlayerIsOver = false;
	}

	protected FalldownCanvas(boolean suppressKeyEvents) {
		super(suppressKeyEvents);
	}

	public void paint(Graphics g)
	{


		graphics.setColor(back.r,back.g,back.b); // repartir avec un buffer propre pour la prochaine part
		graphics.fillRect(0, 0, graphics.getClipWidth(),graphics.getClipHeight());

		Player[] p = game.getPlayers();
		for(int i=0; i<p.length;i++)
		{
			if(!p[i].isOver() || p[i] instanceof LocalPlayer)
			{
			graphics.setColor(ballColors[i].r, ballColors[i].g,ballColors[i].b);
			graphics.fillArc( (int) (p[i].getBall().getX()*rapportWidth), (int) ((p[i].getBall().getY() + ((Game.HEIGHT/2-p[i].getBall().getY())*2-p[i].getBall().getHeight()))*rapportHeight),(int)(p[i].getBall().getWidth()*rapportWidth),(int)(p[i].getBall().getHeight()*rapportHeight),0,360);
			}
		}
		

		graphics.setColor(0,0,255);
		drawFloors();

		graphics.setColor(0,0,0);
		graphics.drawString(""+game.getScore(), 10,15,Graphics.BASELINE|Graphics.LEFT);

		if(showBest)
		{
			//graphics.setColor(255,0,0);
			int i=4;
			while(i>=0 && game.getScore() >= game.gamePlay.meilleursScores.scores[i])
			{	
				i--;
			}
			String s="";
			if(i<4)
			{
				s += (i+2) + "   ";
			}
			if(i>=0)
			{
				s+=game.gamePlay.meilleursScores.scores[i];
			}
			graphics.drawString(s,10,35,Graphics.BASELINE|Graphics.LEFT);
		}
		
		if(localPlayerIsOver)
		{
			g.setColor(Color.red.r,Color.red.g,Color.red.b);
			graphics.drawString("YOU ARE TERMINATED",10,70,Graphics.BASELINE|Graphics.LEFT);
		}
		
		buffer.flushGraphics(g);

	}

	private void drawFloors()
	{
		Iterator it = game.getFloors().iterator();
		while(it.hasNext())
		{
			Floor f = (Floor) it.next();

			float blockWidth = (float)Game.WIDTH / f.getGaps().length;

			for(int i=0; i<f.getGaps().length;i++)
			{	
				if(! f.getGaps()[i])
					graphics.fillRect((int)(i*blockWidth*rapportWidth), (int) ((f.getY() + ((Game.HEIGHT/2-f.getY())*2-Floor.height))*rapportHeight) ,(int) Math.ceil(blockWidth * rapportWidth),(int) (f.getHeight()*rapportHeight));
			}
		}
	}

	public boolean update() 
	{
		game.update();
		return game.isOver();

	}

	public void notifyModelChanged() {
		// TODO Auto-generated method stub
		
	}

	public void localPlayerIsOver() {
		localPlayerIsOver =true	;
		}



}
