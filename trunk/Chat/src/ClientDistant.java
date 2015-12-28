import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;


public class ClientDistant extends Client implements Runnable{

	Vector targets = new Vector(3);
	boolean rechercheFinie=false;
	
	public ClientDistant() {}
	int selected;
	
	public void searchServices()
	{
		new Thread(this).start();
	}
	
	public void run()
	{
		// obtenir une connection
		searchServicesHelper();
	}
	
	private void searchServicesHelper()
	{
		try 
		{
			
			LocalDevice local = LocalDevice.getLocalDevice();
			DiscoveryAgent dAgent = local.getDiscoveryAgent();
			UUID uuid = new UUID(UUID,false);
			String target = null;
			while(!rechercheFinie)
			{
				target = dAgent.selectService(uuid, 0, false);
				if(!targets.contains(target))
				{
					System.out.println("une target en plus : " + target);
					targets.addElement(target);
				}
			}
			//connection = (L2CAPConnection) Connector.open(target);
			
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(int n) throws IOException
	{
		this.selected = n;
		new Thread( 
				new Runnable(){
					public void run(){
						try {
							connection = (L2CAPConnection) Connector.open((String)targets.elementAt(selected));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}}).start();
	}
	
	
}
