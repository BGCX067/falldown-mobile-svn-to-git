import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.game.GameCanvas;


public class Client implements Runnable
{
	PongCanvas pc;
	L2CAPConnection connection;
	private final static String uuid ="2ac61b50d7fd11dba17f00e04cf92edd";
	byte[] data;
	
	
	public Client(PongCanvas pc)
	{
		this.pc = pc;
		Thread t= new Thread(this);
		t.start();
		
	}
	
	

	public void run() {
		// Chercher le service et obtenir la connection
		String target = null;
		try
		{
			LocalDevice local;
			local = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = local.getDiscoveryAgent();
			target = agent.selectService(new UUID(uuid,false), ServiceRecord.AUTHENTICATE_NOENCRYPT, false); // the client is master or slave
		}
		catch(Exception e){e.printStackTrace();}
		try 
		{
			connection = (L2CAPConnection) Connector.open(target);
		} 
		catch (IOException e) {e.printStackTrace();}
		
		try
		{
			data = new byte[connection.getReceiveMTU()];
			data[0]=RemotePlayer.CONNECTED;
			connection.send(data);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendInput()
	{
		int keyState = pc.getKeyStates();
		if((keyState & GameCanvas.LEFT_PRESSED) != 0)
		{
			data[0]=RemotePlayer.LEFT;
		}
		else if((keyState & GameCanvas.RIGHT_PRESSED) != 0)
		{
			data[0]=RemotePlayer.RIGHT;
		}
		else
		{
			data[0]=RemotePlayer.STAY;
		}
		try
		{
			connection.send(data);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// Attention bloquant !!!!   s'exécute dans le thread principal et boucle
	// jusqu'a recevoir les données...
	public void actualiseGame()
	{
		int i=100;
		try
		{
			while(! connection.ready()){Thread.sleep(10);i--; if(i<=0)return;}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		try
		{
			while(connection.ready())
			{
				connection.receive(data);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		pc.state = data[0];
		pc.balle.setPosition(data[1]+128,data[2]+128);
		pc.p1.raquettes[0].setPosition(data[3]+128, pc.p1.raquettes[0].getY());
		pc.p1.raquettes[1].setPosition(data[4]+128, pc.p1.raquettes[1].getY());
		pc.p2.raquettes[0].setPosition(data[5]+128, pc.p2.raquettes[0].getY());
		pc.p2.raquettes[1].setPosition(data[6]+128, pc.p2.raquettes[1].getY());
		pc.p1.score = data[7]+128;
		pc.p2.score = data[8]+128;
	}
	
	public boolean connected()
	{
		return connection != null;
	}

}
