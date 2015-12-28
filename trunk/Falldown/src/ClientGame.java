import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.L2CAPConnection;

import Bluetooth.Protocole;


public class ClientGame extends Game
{
	byte[] buffer = new byte[300];
	L2CAPConnection connection;
	int localPlayerId;
	String vainqueur;

	boolean notifyOver;
	boolean test = false;

	public ClientGame(Player localPlayer, L2CAPConnection connection) {
		System.out.println("client");
		this.connection = connection;
		// recevoir du serveur le type de gamePlay
		int len;
		try {

			//boolean start = false;
			while(!connection.ready())
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			len = connection.receive(buffer);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer,0,len);
			DataInputStream dis = new DataInputStream(bais);

			int type = dis.readInt();
			if(type == Protocole.GAMEPLAY)
			{
				System.out.println("gamePlay received");
				int gpId = dis.readInt();
				this.gamePlay = GamePlay.getGamePlay(gpId);
				int numPlayer = dis.readInt();
				localPlayerId = dis.readInt();

				this.players = new Player[numPlayer];
				//System.out.println("numPlayers: "+numPlayer);
				for(int i=0;i<players.length;i++)
				{
					if(i!=localPlayerId)
					{
						players[i]= new RemotePlayer(connection); // TODO bof bof, la connection ne vaut que pour le server
					}
					else
					{
						players[i]=localPlayer;
					}
				}
				//System.out.println("gamePlay is read");
				//start = true;
			}

			/*else if (type == Protocole.PING)
					{
						//System.out.println("ping received");
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						DataOutputStream dos = new DataOutputStream(baos);
						dos.writeInt(Protocole.READY);
						byte[] b = baos.toByteArray();
						connection.send(b);
					}
					else
					{
						//System.err.println("Probleme : should receive gameplay. Type : " + type);// probleme
						if( type == Protocole.TICK)
						{
							System.err.println("ticks : ");
							discardTick(dis);
						}
					}
				}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		notifyOver = false;
		init();
		test = true;
		System.out.println("Client game created");
	}


	public void run()
	{
		while(!isOver())
		{
			try
			{
				fdc.repaint();
				update();
				sendPos();
				receiveInfoGame();
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void update()
	{
		handleMoves();
		floorsUpdate();
		updateBalls();
		if(players[localPlayerId].getBall().getY()>gamePlay.getHeight())
		{
			players[localPlayerId].setOver();
			notifyOver = true;
		}
	}

	private void receiveInfoGame()
	{
		try {
			while(!connection.ready())
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			int len = connection.receive(buffer);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer,0,len);
			DataInputStream dis = new DataInputStream(bais);
			while(dis.available()>0)
			{
				int type = dis.readInt();
				if (type == Protocole.TICK)
					readTick(dis);
				else
					System.err.println("unknown message : "+ type);
			}


		} catch (IOException e) {
			//close();
			e.printStackTrace();
		}
	}

	private void sendPos()
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(Protocole.BALL);
			dos.writeInt(localPlayerId);
			dos.writeInt((int)players[localPlayerId].getBall().getX());
			dos.writeInt((int)players[localPlayerId].getBall().getY());
			byte[] b = baos.toByteArray();
			connection.send(b);
		} catch (IOException e) {
			close();
			e.printStackTrace();
		}
	}

	void close()
	{
		over = true;
		try {
			connection.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private int readInt(int i, byte[] buffer) {
		return buffer[i]<<24 | (buffer[i+1]&0xff)<<16 | (buffer[i+2]&0xff)<<8 | (buffer[i+3]&0xff);
	}


	void checkOver() {
		// nothing to do, it's the server who calculate it
	}

	private void readTick(DataInputStream dis)
	{
		try {
			int newTicks = dis.readInt();
			if(ticks+1 != newTicks)
			{
				System.out.println("missed Frame : " + (newTicks - ticks - 1));
			}
			ticks = newTicks;
			while(dis.available()>0)
			{
				int type = dis.readInt();
				if(type== Protocole.BALL)
				{
					readBall(dis);
				}
				else if(type == Protocole.FINI)
				{
					readFini(dis);
				}
				else if(type==Protocole.NEWFLOOR)
				{
					readFloor(dis);
				}
				else if(type == Protocole.PLAYER_DEAD)
				{
					readPlayerDead(dis);
				}
				else
				{
					System.err.println("Unknown msg : " +type);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readPlayerDead(DataInputStream dis) {
		try {
			int playerId = dis.readInt();
			players[playerId].setOver();
			if(playerId == localPlayerId)
			{
				fdc.localPlayerIsOver();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void readBall(DataInputStream dis) {
		try {
			int ballId = dis.readInt();
			if(ballId!=localPlayerId)
			{	
				players[ballId].getBall().setX(dis.readInt());
				players[ballId].getBall().setY(dis.readInt());
			}
			else
			{
				dis.readInt();dis.readInt();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void readFini(DataInputStream dis) throws IOException {
		try {
			over = true;
			int length = dis.readInt();
			char[] vainqueurName = new char[length];
			for(int i=0; i<length; i++)
			{
				vainqueurName[i]=dis.readChar();
			}

			vainqueur = new String(vainqueurName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void readFloor(DataInputStream dis) throws IOException {
		try {
			double speed = dis.readDouble();
			int height = dis.readInt();
			float blockWidth = dis.readFloat();
			double y = dis.readDouble();
			int length = dis.readInt();
			boolean[] gaps = new boolean[length];
			for(int i=0; i<gaps.length;i++)
			{
				gaps[i]=dis.readBoolean();
			}
			Floor f = new Floor(gaps);
			f.speed = speed;
			f.height = height;
			f.y = y;
			f.blockWidth = blockWidth;
			floors.addLast(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void discardTick(DataInputStream dis)
	{
		try {
			System.out.println("ticks: " + dis.readInt());
			while(dis.available()>0)
			{
				int type = dis.readInt();
				System.out.println("ticks type" + type);
				if(type== Protocole.BALL)
				{
					dis.readInt();dis.readInt();dis.readInt();
				}
				else if(type == Protocole.FINI)
				{
					dis.readInt();
				}
				else if(type==Protocole.NEWFLOOR)
				{
					dis.readDouble();
					dis.readInt();
					dis.readFloat();
					dis.readDouble();
					int length = dis.readInt();
					for(int i=0; i<length;i++)
					{
						dis.readBoolean();
					}
				}
				else if(type == Protocole.PLAYER_DEAD)
				{
					dis.readInt();
				}
				else
				{
					System.err.println("Unknown msg in discard : " +type);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}