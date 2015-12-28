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
	BatailleNavale bn;
	Display display;
	Form form = new Form("Connection au serveur", new Item[] { new StringItem("","Recherche de serveur ...") });
	
	
	public ClientConnection(BatailleNavale bn) {
		this.bn = bn;
		display = Display.getDisplay(bn);
		form.addCommand(BatailleNavale.cmdQuit);
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
			byte[] b = new byte[1];
			while(!connection.ready())
			{
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("CLIENT : boucle");
			}
			connection.receive(b);
			boolean commence = b[0]==0;
			Combat c = new Combat(bn, connection,commence);
			c.start();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void commandAction(Command c, Displayable d) {
		if (c==BatailleNavale.cmdQuit)
		{
			bn.notifyDestroyed();
		}
	}
	
}
