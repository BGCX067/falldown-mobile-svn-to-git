import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import Bluetooth.Protocole;
import Bluetooth.ServerConnexions;


public class ServerGame extends Game
{
	private ServerConnexions serverConnexions;
	private L2CAPConnection[] connections;
	byte[] buffer = new byte[300];
	String vainqueur;

	public ServerGame(GamePlay gp, Player localPlayer, ServerConnexions serverConnexions) {
		this.gamePlay = gp;

		this.serverConnexions = serverConnexions;
		this.connections = serverConnexions.getConnections();


		this.players = new Player[connections.length+1];
		players[0]=localPlayer;
		for(int i=1;i<players.length;i++)
		{
			players[i]= new RemotePlayer(connections[i-1]);
		}

		
		
		
		
		
		try {

			for(int i=0; i<connections.length;i++)
			{
				/*while(!connections[i].ready())
				{
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					dos.writeInt(Protocole.PING);
					byte[] b = baos.toByteArray();
					connections[i].send(b);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				connections[i].receive(buffer);*/
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeInt(Protocole.GAMEPLAY); // TODO factoriser les traitements similaires (seul change le 4eme write) 
				dos.writeInt(gp.getId());
				dos.writeInt(connections.length+1);
				dos.writeInt(i+1);
				byte[] b = baos.toByteArray();
				connections[i].send(b);
				System.out.println("Gameplay send to : " + i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		init();
	}


	public void run()
	{

		long lastUpdate = System.currentTimeMillis();
		while(!isOver())
		{
			long now = System.currentTimeMillis();
			int numUpdatesLate = 1;
			if(now-lastUpdate+retard<delais)
			{
				//System.out.println("sleep time : "+(delais-retard-(now-lastUpdate)));
				try {
					Thread.sleep(delais-(now-lastUpdate+retard));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				retard = 0;
			}
			else
			{
				numUpdatesLate =(int) ((now - lastUpdate + retard) / delais);
				//System.out.println("en retard / uplates: " + numUpdatesLate );
				retard = (int) ((now - lastUpdate + retard) % delais);
			}

			lastUpdate = System.currentTimeMillis();



			//for(int i=0; i<numUpdatesLate || i<2;i++)
			//{
				receiveBallPos();
				fdc.repaint();
				update();
				sendInfoGame();
			//}


			

		}
	}

	private void receiveBallPos()
	{
		for(int i=0; i<connections.length;i++)
			try 
		{
				while( !connections[i].ready()) 
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				int len = connections[i].receive(buffer);
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer,0,len);
				DataInputStream dis = new DataInputStream(bais);
				while(dis.available()>0)
				{
					int type = dis.readInt();	
					if(type== Protocole.BALL)
					{
						int playerId = dis.readInt();
						players[playerId].getBall().setX(dis.readInt());
						players[playerId].getBall().setY(dis.readInt());
					}
				}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private void sendInfoGame()
	{

		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {

			dos.writeInt(Protocole.TICK);
			dos.writeInt((int)getTicks());	
			for(int i=0; i<players.length;i++)
			{
				dos.writeInt(Protocole.BALL);
				dos.writeInt(i);
				dos.writeInt((int)players[i].getBall().getX());
				dos.writeInt((int)players[i].getBall().getY());
			}
			if(newFloorThisFrame)
			{
				dos.writeInt(Protocole.NEWFLOOR);
				Floor f = (Floor) floors.elementAt(floors.size()-1);
				dos.writeDouble(f.speed);
				dos.writeInt(f.height);
				dos.writeFloat(f.getBlockWidth());
				dos.writeDouble(f.y);
				boolean[] gaps = f.getGaps(); 
				dos.writeInt(gaps.length);
				for(int i=0; i<gaps.length; i++)
				{
					dos.writeBoolean(gaps[i]);
				}
			}
			for(int i=0; i<players.length;i++)
			{
				if(players[i].isOver())
				{
					dos.writeInt(Protocole.PLAYER_DEAD);
					dos.writeInt(i);
				}
			}
			if(isOver())
			{
				dos.writeInt(Protocole.FINI);
				int i = 0;
				while(i<players.length && players[i].isOver())
				{
					i++;
				}
				if(i==0)
				{
					LocalDevice local = LocalDevice.getLocalDevice();
					vainqueur = local.getFriendlyName();
				}else if(i==players.length)
				{
					vainqueur = "MATCH NUL"; // TODO envoyer un message particulier
				}
				else
					vainqueur = players[i].getName();
				dos.writeInt(vainqueur.length());
				dos.writeChars(vainqueur);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}


		for(int i=0; i<connections.length;i++)
		{
			try {
				byte[] b = baos.toByteArray();
				connections[i].send(b);
				/*if(isOver())
				{
					while(!connections[i].ready())
					{
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					connections[i].close();
					serverConnexions.close();
				}*/

			} catch (IOException e) {
				over = true;
				try {
					connections[i].close();
					serverConnexions.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void checkOver() {
		int playerAlive = 0;
		for(int i=0; i<players.length; i++)
		{
			if(!players[i].isOver())
			{
				if(players[i].getBall().getY()>Game.gamePlay.getHeight())
				{
					players[i].setOver();
					if(i==0)
					{
						fdc.localPlayerIsOver();
					}
					//over = true; // TODO delete this line
				}
				else
				{
					playerAlive++;
				}
			}

		}
		this.over = playerAlive <= 1;
	}

	public void close()
	{
		for(int i=0;i<connections.length;i++)
		{
			try {
				connections[i].close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
