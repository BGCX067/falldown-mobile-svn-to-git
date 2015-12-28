import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;


abstract public class Buffer 
{

	public static Buffer getBuffer(GameCanvas gc,Graphics g)
	{
		if(System.getProperty("microedition.platform").toLowerCase().startsWith("nokia"))
		{
			return new NokiaBuffer(gc);
		}
		else
		{
			return new GameCanvasBuffer(gc, g);
		}
	}
	
	abstract public void flushGraphics(Graphics g);
	
	abstract public Graphics getGraphics();
}
