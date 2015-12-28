import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;


public class GameCanvasBuffer extends Buffer{

	private GameCanvas gameCanvas;
	private Graphics graphics;
	
	public GameCanvasBuffer(GameCanvas gc,Graphics graphics)
	{
		this.gameCanvas = gc;
		this.graphics = graphics;
	}
	
	public void flushGraphics(Graphics g) {
		// TODO Auto-generated method stub
		gameCanvas.flushGraphics();
	}

	public Graphics getGraphics() {
		return graphics;
	}
	

}
