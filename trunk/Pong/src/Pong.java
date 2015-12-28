import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Pong extends MIDlet implements CommandListener{
	PongCanvas pongCanvas;
	Display display;
	List menu;
	List menuType;
	Form credits;
	Command returnCom;

	protected void startApp() throws MIDletStateChangeException 
	{
		
		
		pongCanvas = new PongCanvas(this);
		pongCanvas.setFullScreenMode(true);
		display = Display.getDisplay(this);
		
		menu = new List("PONG 2007",List.IMPLICIT);
		menu.append("Nouvelle partie", null);
		menu.append("Crédits", null);
		menu.append("Quitter", null);
		menu.setCommandListener(this);
		display.setCurrent(menu);
		
		menuType = new List("Type de partie",List.IMPLICIT);
		menuType.append("single", null);
		menuType.append("server", null);
		menuType.append("join", null);
		menuType.setCommandListener(this);
		
		credits = new Form("Crédits");
		returnCom = new Command("menu",Command.BACK,1);
		credits.append(new StringItem("Realiser par L.Herbin",null));
		credits.addCommand(returnCom);
		credits.setCommandListener(this);
		
		//Command exit = new Command("Exit", Command.EXIT, 1);
		//pongCanvas.addCommand(exit);
		//pongCanvas.setCommandListener(this);
	}

	protected void pauseApp() 
	{
		
		
	}
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException 
	{
		notifyDestroyed();
		
	}
	
	public void finPartie(Player p)
	{
		display.setCurrent(menu);
	}

	public void commandAction(Command c, Displayable d) {
		if(d.equals(menu))
		{
			int i = menu.getSelectedIndex();
			switch (i)
			{
				case 0: 
					display.setCurrent(menuType);
					break;
				case 1:
					display.setCurrent(credits);
					break;
				case 2:
					notifyDestroyed();
			}
		}
		else if(d.equals(menuType))
		{
			int i = menuType.getSelectedIndex();
			switch (i)
			{
				case 0: 
					pongCanvas.newGame(null, null,PongCanvas.SINGLE);
					display.setCurrent(pongCanvas);
					break;
				case 1:
					pongCanvas.newGame(null, null,PongCanvas.SERVER);
					display.setCurrent(pongCanvas);
					break;
				case 2 :
					pongCanvas.newGame(null, null,PongCanvas.CLIENT);
					display.setCurrent(pongCanvas);
					break;
			}
		}
		else if(c.equals(returnCom))
		{
			display.setCurrent(menu);
		}
		
	}
	
	
}
