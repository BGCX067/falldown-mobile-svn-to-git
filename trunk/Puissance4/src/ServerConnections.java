import java.io.IOException;
import java.util.Random;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;


public class ServerConnections  implements Runnable, CommandListener
{
	Puissance4 p4;
	Display display;
	
	StringItem str = new StringItem("","Waiting for connections");
	Form form = new Form("Serveur",new Item[]{str});
	
	
	private final static String URL ="btl2cap://localhost:" + Protocole.UUID + ";ReceiveMTU=512;TransmitMTU=512";

	
	public ServerConnections(Puissance4 p4)
	{
		this.p4 = p4;
		display = Display.getDisplay(p4);
		form.addCommand(p4.cmdQuitter);
		form.setCommandListener(this);
	}
	
	public void obtenirConnection()
	{
		display.setCurrent(form);
		new Thread(this).start();
	}
	
	public void run()
	{
		try {
			LocalDevice local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			L2CAPConnectionNotifier connectionNotifier = (L2CAPConnectionNotifier)Connector.open(URL);
			L2CAPConnection connection = null;
			while(connection == null)
			{
				connection = connectionNotifier.acceptAndOpen();
			}
			System.out.println("SERVEUR : client connecte");
			p4.server(connection);
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
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
