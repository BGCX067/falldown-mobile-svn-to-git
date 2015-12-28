import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.L2CAPConnection;


public class Serveur implements Runnable {
	Vector connections = new Vector(5);
	boolean fini = false;
	
	public Serveur()
	{
		new ServeurWaiterConnection(this);
		new Thread(this).start();
	}
	
	public void add(L2CAPConnection connection)
	{
		connections.addElement(connection);
	}
	
	public void run()
	{
		
		try {
			while(!fini)
			{
				/*
				 * 1) Voir si on reçoit des messages de chacune des connection
				 *     2) si oui, renvoyer le message à tout le monde
				 */
				for(int i=0; i<connections.size(); i++)
				{
						L2CAPConnection c = (L2CAPConnection)connections.elementAt(i);
						try {
							byte[] b = new byte[c.getReceiveMTU()];
							while(c.ready())
							{
								int n = c.receive(b);
								byte[] b2 = new byte[n];
								for(int j=0; j<b2.length; j++)
								{
									b2[j]=b[j];
								}
								for(int j=0; j<connections.size();j++)
								{
									L2CAPConnection c2 = (L2CAPConnection) connections.elementAt(j);
									try {
										c2.send(b2);
									} catch (IOException e) {
										e.printStackTrace();
										connections.removeElement(c2);
									}
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							connections.removeElement(c);
						}
					
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			System.err.println("Serveur Plante");
			e.printStackTrace();
		}
	}
	

}
