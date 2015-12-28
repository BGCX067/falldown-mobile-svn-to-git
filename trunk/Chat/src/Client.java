import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;


abstract public class Client
{
	final static String UUID ="2E067df048ff11dc9c6c00e04cf92edd";
					
	L2CAPConnection connection;
	String nom;
	
	public Client()
	{
	}
	
	
	
	public void send(String s)
	{
		String s2 = nom + " : " +s;
		System.out.println("Envoyer : " + s2);
		byte[] b= s2.getBytes();
		try {
			connection.send(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receive()
	{
		String s = null;
		try {
			if(connection.ready())
			{
				System.out.println("reception");
				byte[] b = new byte[connection.getReceiveMTU()];
				int n = connection.receive(b);
				s= new String(b,0,n);
				System.out.println("reçu : " + s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public void close()
	{
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean ready()
	{
		try {
			return connection.ready();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isConnecte()
	{
		return connection!=null;
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}
}
