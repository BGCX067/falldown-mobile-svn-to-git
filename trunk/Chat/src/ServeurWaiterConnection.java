import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;


public class ServeurWaiterConnection implements Runnable{
	
	private final static String UUID ="2E067df048ff11dc9c6c00e04cf92edd";    
	private final static String URL ="btl2cap://localhost:" + UUID + ";ReceiveMTU=512;TransmitMTU=512";

	Serveur serveur;
	boolean fini = false;
	
	public ServeurWaiterConnection(Serveur s)
	{
		this.serveur = s;
		(new Thread(this)).start();
	}
	
	public void run ()
	{
		try
		{
			LocalDevice local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			L2CAPConnectionNotifier service = (L2CAPConnectionNotifier)Connector.open(URL);
			while(!fini)
			{
				L2CAPConnection c = service.acceptAndOpen();
				if(c!=null)
				{
					serveur.add(c);
					System.out.println("Nouvelle connection ! " + c.toString());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
