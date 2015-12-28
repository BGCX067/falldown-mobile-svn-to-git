import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.game.GameCanvas;



/**
 * Cette classe représente un joueur distant lorsque la partie est server
 * Une fonction de mise à jour de la position du joueur se fait parrallement dans un thread
 * lorsque la mis
 * 
 * @author lherbin
 *
 */
public class RemotePlayer extends Player implements Runnable{
	static final byte LEFT = (byte) 1;
	static final byte RIGHT = (byte) 2;
	static final byte STAY = (byte) 3;
	static final byte CONNECTED = (byte) 4;
	static final byte BEGIN = (byte) 5;
	
	
	
    private final static String UUID ="2ac61b50d7fd11dba17f00e04cf92edd";
    private final static String URL ="btl2cap://localhost:" + UUID + ";ReceiveMTU=48;TransmitMTU=48";
	L2CAPConnection connection=null;
	byte[] data;
	PongCanvas pc;
	
	boolean connected = false;
	boolean updated;
	byte commande = STAY;
	boolean fini = false;
	
	public RemotePlayer(Raquette[] r, int sens, PongCanvas pc)
	{
		super(r, sens);
		this.pc = pc;
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * TODO changer la valeur de retour vers bool
	 * TODO pour signaler si la position a été mise à jour
	 */
	public void input(int keystate)
	{
		/*if(!updated) return; // a changer
		else
		{*/
			switch(commande)
			{
			case LEFT:
				super.input(GameCanvas.LEFT_PRESSED);
				break;
			case RIGHT:
				super.input(GameCanvas.RIGHT_PRESSED);
				break;
			/*}
			updated=false;*/
		}
	}
	
	/*
	 * obtenir une connexion avec un client distant
	 * obligatoire avant de démarrer une partie
	 */
	public void obtainConnection()
	{
		LocalDevice local = null;
		L2CAPConnectionNotifier service=null;
		connection = null;
		try {
			local = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e1) {e1.printStackTrace();}
		try
		{	
			local.setDiscoverable(DiscoveryAgent.GIAC);
			service = (L2CAPConnectionNotifier) Connector.open(URL);
			connection = service.acceptAndOpen();
			data = new byte[connection.getReceiveMTU()];
		} catch (IOException e) {		e.printStackTrace();}
	}
	
	public boolean connected()
	{
		try
		{
			connection.receive(data);
			if(data[0]==CONNECTED)
			{	
				connected = true;
			}
		}catch (Exception e){}

		return connected;
		
		
	}

	public void run() 
	{
		obtainConnection();
		while(!fini)
		{
			try
			{
				synchronized(data)
				{
						while(connection.ready())
						{
							connection.receive(data);
							commande = data[0];
						}
				}
			}
			catch(IOException e){e.printStackTrace();}
			
			
			// rendre la main
			try 
			{
				Thread.sleep(30);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void actualise()
	{
		synchronized(data)
		{
			data[0]=(byte)pc.state;			
			data[1]=(byte)(pc.balle.getX()-128);	
			data[2]=(byte)(pc.balle.getY()-128);
			data[3]=(byte)(pc.p1.raquettes[0].getX()-128);
			data[4]=(byte)(pc.p1.raquettes[1].getX()-128);
			data[5]=(byte)(pc.p2.raquettes[0].getX()-128);
			data[6]=(byte)(pc.p2.raquettes[1].getX()-128);
			data[7]=(byte)(pc.p1.score-128);
			data[8]=(byte)(pc.p2.score-128);
			
			
			try {connection.send(data);} 
			catch (IOException e) {e.printStackTrace();}

		}
	}
	
	public void fini()
	{
		fini=true;
	}
	
	
}
