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
	BatailleNavale bn;
	Display display;
	
	StringItem str = new StringItem("","Waiting for connections");
	Form form = new Form("Serveur",new Item[]{str});
	
	
	private final static String URL ="btl2cap://localhost:" + Protocole.UUID + ";ReceiveMTU=512;TransmitMTU=512";

	
	public ServerConnections(BatailleNavale b)
	{
		bn = b;
		display = Display.getDisplay(b);
		form.addCommand(BatailleNavale.cmdQuit);
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
			int commence = new Random().nextInt(2);
			connection.send(new byte[]{(byte)commence});
			System.out.println("SERVEUR : qui commence envoyé");
			Combat c = new Combat(bn,connection,commence==1);
			c.start();
			
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
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
