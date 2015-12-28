import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;


public class ClientConnection implements Runnable, CommandListener {
	Puissance4 p4;
	Display display;
	Form form = new Form("Connection au serveur", new Item[] { new StringItem("","Recherche de serveur ...") });
	
	
	public ClientConnection(Puissance4 p4) {
		this.p4 = p4;
		display = Display.getDisplay(p4);
		form.addCommand(p4.cmdQuitter);
		form.setCommandListener(this);
	}

	public void start()
	{
		display.setCurrent(form);
		new Thread(this).start(); // obtenir la connection
	}
	
	public void run()
	{
		// obtenir une connection
		try 
		{
			System.out.println("CLIENT : debut");
			L2CAPConnection connection = null;
			LocalDevice local = LocalDevice.getLocalDevice();
			DiscoveryAgent dAgent = local.getDiscoveryAgent();
			String target = null;
			while(target == null)
			{
				UUID uuid = new UUID(Protocole.UUID,false);
				target = dAgent.selectService(uuid, 0, false);
			}
			connection = (L2CAPConnection) Connector.open(target);
			//System.out.println("CLIENT : connecte au serveur");
			p4.client(connection);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void commandAction(Command c, Displayable d) {
		if (c==p4.cmdQuitter)
		{
			p4.notifyDestroyed();
		}
	}
	
}
