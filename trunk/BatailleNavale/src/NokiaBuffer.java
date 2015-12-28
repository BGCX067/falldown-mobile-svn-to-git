import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;


// this do a double buffering by an other way than the one of
// gameCanvas which cause troubles on nokia phones
public class NokiaBuffer extends Buffer {

	Image offScreenBuffer;
	
	public NokiaBuffer(GameCanvas gc)
	{
		offScreenBuffer=Image.createImage(gc.getWidth(),gc.getHeight());
	}
	
	public void flushGraphics(Graphics graphics) {
		graphics.drawImage(offScreenBuffer, 0, 0, Graphics.LEFT| Graphics.TOP);
	}

	public Graphics getGraphics() {
		return offScreenBuffer.getGraphics();
	}

}
