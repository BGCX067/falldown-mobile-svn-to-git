import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Chat extends MIDlet implements CommandListener, Runnable {

	List menu = new List("Serveur, Client",List.IMPLICIT) ;
	
	List menuChoixServeur = new List("A quel serveur se connecter ?", List.IMPLICIT);
	
	Command cmdSend = new Command("Send",Command.OK,2);
	Command cmdExit = new Command("exit", Command.EXIT,1);
	Command cmdOk = new Command("ok", Command.OK,1);
	
	Display display;
	
	ClientDisplay clientDisplay;
	Client client;
	Serveur serveur;
	
	TextBox txtBoxName = new TextBox("Enter your name :","", 13,TextField.ANY);
	
	StringItem strServeur = new StringItem("Serveur","");
	Form formServeur = new Form("Serveur", new Item[]{strServeur});

	
	protected void startApp() throws MIDletStateChangeException 
	{
		menu.append("Client", null);
		menu.append("Serveur", null);
		menu.setCommandListener(this);
		display = Display.getDisplay(this);
		display.setCurrent(menu);
		
		formServeur.addCommand(cmdExit);
		formServeur.setCommandListener(this);
		
		txtBoxName.addCommand(cmdOk);
		txtBoxName.setCommandListener(this);
		clientDisplay = new ClientDisplay(this,client);
		
		menuChoixServeur.setCommandListener(this);
	}
	
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {	
	}

	protected void pauseApp() {	
	}


	public void commandAction(Command c, Displayable d) {	
		if(d==menu)
		{
			if(c==List.SELECT_COMMAND)
			{
				switch(menu.getSelectedIndex())
				{
				case 0:	
					client = new ClientDistant();
					((ClientDistant) client).searchServices();
					display.setCurrent(menuChoixServeur);
					new Thread(this).start();
					break;
				case 1:
					serveur = new Serveur();
					client = new ClientLocal(serveur);
					display.setCurrent(txtBoxName);
					break;
				}
			}
		}
		if(d==txtBoxName)
		{
			try {
				while(client==null)
				{
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("plop");
				client.setNom(txtBoxName.getString());
				clientDisplay.client = client;
				new Thread(clientDisplay).start();
				display.setCurrent(clientDisplay.formClient);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(d==menuChoixServeur)
		{
			if(c==List.SELECT_COMMAND)
			{
				int n = menuChoixServeur.getSelectedIndex();
				try {
					((ClientDistant) client).connect(n);
					display.setCurrent(txtBoxName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void run() {
		// TODO Auto-generated method stub
		while(display.getCurrent()==menuChoixServeur)
		{
			Vector v = ((ClientDistant)client).targets;
			while(menuChoixServeur.size()<v.size())
			{
				menuChoixServeur.append((String) v.elementAt(menuChoixServeur.size()),null);
				
				System.out.println("boucle actualisation affichage choix");
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}



}
