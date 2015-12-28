package Bluetooth;



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
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;


public class ConnectToServer implements Runnable,CommandListener,DiscoveryListener
{
	private static final int CONNECTE=1, SEARCHING = 2;

	int state = SEARCHING;


	L2CAPConnection connection;
	List list = new List("Connexion au serveur",List.IMPLICIT);
	Vector devices = new Vector(3);
	Vector services = new Vector(3);
	boolean commence = false;
	Form form = new Form("Connexion avec le serveur");
	StringItem str = new StringItem("Connecté","");
	LocalDevice local;
	DiscoveryAgent service;
	Display display;
	RemoteDevice deviceCandidat;

	private boolean enquiry=false;

	public ConnectToServer()
	{
		form.append(str);
		list.setCommandListener(this);
		try {
			local = LocalDevice.getLocalDevice();
			service = local.getDiscoveryAgent();
			start();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}

	public void start()
	{
		new Thread(this).start();
	}


	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			service.startInquiry(DiscoveryAgent.GIAC, this);
		} catch (BluetoothStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(state == SEARCHING)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(service.retrieveDevices(DiscoveryAgent.CACHED).length);
		}

		if (connection == null)
		{
			return;
		}

		//connect();
		display.setCurrent(form);
		str.setText("Attente du lancement de la partie par " + list.getString(list.getSelectedIndex()));

	
		try {
			while(!connection.ready())
			{
				try {Thread.sleep(15);} catch (InterruptedException e) {e.printStackTrace();}
			}
			connection.receive(new byte[connection.getReceiveMTU()]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str.setText("Partie lancee");
		commence = true;


	}

	public boolean isCommence()
	{
		return commence;
	}

	public L2CAPConnection getConnection()
	{
		return connection;
	}

	public void displayOn(Display d)
	{
		display = d;
		d.setCurrent(list);
	}

	public void commandAction(Command c, Displayable d) {
		if(enquiry)
		{
			service.cancelInquiry(this);
		}
		try {
			service.searchServices(null, new UUID[]{new UUID(ServerConnexions.UUID,false)}, (RemoteDevice)devices.elementAt(list.getSelectedIndex()), this);
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void deviceDiscovered(RemoteDevice rm, DeviceClass dc) {
		
		for(int i=0; i<devices.size();i++)
		{
			RemoteDevice d = (RemoteDevice)devices.elementAt(i);
			if(d.getBluetoothAddress().equals(rm.getBluetoothAddress()))
				return;
		}
		
		try {
			list.append(rm.getFriendlyName(true), null);
			devices.addElement(rm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			deviceCandidat = rm;
			service.searchServices(null, new UUID[]{new UUID(ServerConnexions.UUID,false)}, rm, this);
			enquiry = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void inquiryCompleted(int arg0) {

	}

	public void serviceSearchCompleted(int arg0, int arg1) {

	}

	public void servicesDiscovered(int transId, ServiceRecord[] servRecords) {
		try {
			connection = (L2CAPConnection) Connector.open(servRecords[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT , false));
			service.cancelInquiry(this);
			service.cancelServiceSearch(DiscoveryAgent.CACHED);
			state = CONNECTE;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}


/*
 	public void connect()
	{
		try {
			LocalDevice local = LocalDevice.getLocalDevice();
			DiscoveryAgent service = local.getDiscoveryAgent();
			String target = null;

			UUID uuid;
			try {
				uuid = new UUID(ServerConnexions.UUID,false);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw e;
			}
			while(target == null)
			{
				target = service.selectService(uuid, 0, false);
			}
			System.out.println("" + target);
			connection = (L2CAPConnection) Connector.open(target);

		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 */