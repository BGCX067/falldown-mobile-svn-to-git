package Bluetooth;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;


public class ServerConnexions implements Runnable, CommandListener
{
	public static String UUID = "c196f1850e6a4cad85d61b3c3635f30b";
	private final static String URL ="btl2cap://localhost:" + UUID + ";ReceiveMTU=512;TransmitMTU=512";
	Vector connexions = new Vector(2);
	
	boolean fini = false;
	Command cmdDemarrer = new Command("Demarrer",Command.BACK,2);
	public Form form = new Form("Attente des connexions");
	L2CAPConnectionNotifier service;
	
	public ServerConnexions()
	{
		//this.configurateur = configurateur;
		form.addCommand(cmdDemarrer);
		form.setCommandListener(this);
	}

	
	public void start()
	{
		(new Thread(this)).start();
	}
	
	public void run()
	{
		try
		{
			LocalDevice local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			service = (L2CAPConnectionNotifier)Connector.open(URL);
			while(!fini)
			{
				L2CAPConnection c = service.acceptAndOpen();
				if(c!=null)
				{
					connexions.addElement(c);
					RemoteDevice rd = RemoteDevice.getRemoteDevice(c);
					form.append(""+rd.getFriendlyName(false));
				}
			}
			service.close();
			System.out.println("Service closed");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}




	public void commandAction(Command c, Displayable d) {
		if(c==cmdDemarrer)
		{
			if(connexions.isEmpty())
				return;
			
			fini=true;
			byte[] b = new byte[]{Protocole.START};
			for(int i=0; i<connexions.size();i++)
			{
				try {
					((L2CAPConnection)connexions.elementAt(i)).send(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public L2CAPConnection getConnection()
	{
		return (L2CAPConnection) connexions.elementAt(0);
	}
	
	public L2CAPConnection[] getConnections()
	{
		L2CAPConnection[] array = new L2CAPConnection[connexions.size()];
		for(int i=0; i<connexions.size();i++)
		{
			array[i] = (L2CAPConnection)connexions.elementAt(i);
		}
		
		return array;
	}
	
	public boolean isFini()
	{
		return fini;
	}
	
	public void close()
	{
		try {
			service.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public Joueur[] getJoueurs()
	{
		
		Joueur[] joueurs = new Joueur[connexions.size()];
		for(int i=0; i<connexions.size(); i++)
		{
			joueurs[i] = new JoueurDistant((L2CAPConnection)connexions.elementAt(i));
		}
		
		return joueurs;
	}*/
	

}
