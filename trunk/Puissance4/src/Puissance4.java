import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Puissance4 extends MIDlet implements CommandListener{

	Command cmdSelect = new Command("Selectionner",Command.OK,1);
	Command cmdQuitter = new Command("Quitter",Command.EXIT,1);
	Command cmdBack = new Command("Retour",Command.BACK,1);
	
	private List menuPrincipal = new List("Puissance 4",List.IMPLICIT);
	private List menuBT = new List("Partie Reseau", List.IMPLICIT);
	Display display;
	
	public Puissance4()
	{
		display = Display.getDisplay(this);
		
		menuPrincipal.append("Chaise tournante", null);
		menuPrincipal.append("Vs CPU", null);
		menuPrincipal.append("Reseau (bluetooth)",null);
		//menuPrincipal.append("Aide", null);
		menuPrincipal.setCommandListener(this);
		menuPrincipal.addCommand(cmdQuitter);
		//menuPrincipal.addCommand(cmdSelect);
		display.setCurrent(menuPrincipal);
		
		menuBT.append("Serveur", null);
		menuBT.append("Client", null);
		menuBT.setCommandListener(this);
		menuBT.addCommand(cmdBack);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		
	}

	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	public void partieFinie()
	{
		display.setCurrent(menuPrincipal);
	}
	
	public void client(L2CAPConnection c)
	{
		try {
			Joueur joueurLocal = new JoueurLocal();
			Partie p = new PartieDistante(joueurLocal,c);
			Affichage a = new Affichage2d(this);
			a.setPartie(p);
			((JoueurLocal)joueurLocal).setAffichage(a);
			display.setCurrent((GameCanvas)a);
			p.demarrer();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void server(L2CAPConnection c)
	{
		try {
			Joueur p1 = new JoueurLocal();
			Joueur p2 = new JoueurDistant(c);
			Regles r = new ReglesClassiques();
			Partie p = new PartieLocale(p1,p2,r);
			Affichage a = new Affichage2d(this);
			a.setPartie(p);
			((JoueurLocal)p1).setAffichage(a);
			display.setCurrent((GameCanvas)a);
			p.demarrer();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void commandAction(Command c, Displayable d) {
		if(c==cmdQuitter)
		{
			notifyDestroyed();
		}
		else if(d==menuPrincipal)
		{
			if(c==List.SELECT_COMMAND || c==cmdSelect)
			{
				switch(menuPrincipal.getSelectedIndex())
				{
				case 0:
					Joueur p1 = new JoueurLocal(); 
					Joueur p2 = new JoueurLocal();
					Regles r = new ReglesClassiques();
					Partie p = new PartieLocale(p1,p2,r);
					Affichage a = new Affichage2d(this);
					((JoueurLocal)p1).setAffichage(a);
					((JoueurLocal)p2).setAffichage(a);
					a.setPartie(p);
					display.setCurrent((GameCanvas)a);
					p.demarrer();
					break;
				case 1:
					p1 = new JoueurLocal(); 
					p2 = new IA();
					r = new ReglesClassiques();
					p = new PartieLocale(p1,p2,r);
					a = new Affichage2d(this);
					((JoueurLocal)p1).setAffichage(a);
					a.setPartie(p);
					display.setCurrent((GameCanvas)a);
					p.demarrer();
					break;
				case 2:
					display.setCurrent(menuBT);
					break;
				case 3:
					
					break;
				}
			}
		}
		else if(d==menuBT)
		{
			if(c==cmdBack)
			{
				display.setCurrent(menuPrincipal);
			}
			else if (c==List.SELECT_COMMAND)
			{
				switch(menuBT.getSelectedIndex())
				{
				case 0:
					new ServerConnections(this).obtenirConnection();
					break;
				case 1:
					new ClientConnection(this).start();
					break;
				}
			}
		}
		
	}

	
}
