import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class BatailleNavale extends MIDlet implements CommandListener {

	List menuServeurClient = new List("Bataille Navale",List.IMPLICIT);
	public static final Command cmdQuit = new Command("Quitter",Command.EXIT,1);
	public static final Command cmdCancel = new Command("Annuler",Command.CANCEL,1);
	Display display;

	public BatailleNavale() {
		
		display = Display.getDisplay(this);
		
		menuServeurClient.addCommand(cmdQuit);
		menuServeurClient.append("Serveur",null);
		menuServeurClient.append("Client", null);
		menuServeurClient.setCommandListener(this);
		
		display.setCurrent(menuServeurClient);
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		
	}

	public void commandAction(Command c, Displayable d) 
	{
		if(c==cmdQuit)
		{
			notifyDestroyed();
		}
		else if(d==menuServeurClient)
		{
			if(c == List.SELECT_COMMAND)
			{
				switch(menuServeurClient.getSelectedIndex())
				{
				case 0:// server
					new ServerConnections(this).obtenirConnection();
					break;
				case 1:// client
					new ClientConnection(this).start();
					break;
				}
			}
		}
		
	}

}
