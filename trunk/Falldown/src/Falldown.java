import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import Bluetooth.ConnectToServer;
import Bluetooth.ServerConnexions;


public class Falldown extends MIDlet implements CommandListener, Runnable{

	static {
		String locale = System.getProperty("microedition.locale");
		if(locale == null || locale.startsWith("en"))
		{
			Texts.setTexts("english");
		}
		else if(locale.startsWith("fr"))
		{
			Texts.setTexts("french");
		}
		else
		{
			Texts.setTexts("english");
		}
		
	} // TODO change this
	
	public static String UUID = "c196f185-0e6a-4cad-85d6-1b3c3635f30b";


	Display display;

	public static final int GAME=1, MENU=2, HIGHSCORE=3, SERVERWAITING = 4, 
	CLIENTWAITING = 5, SERVER_GP = 6, SESSION_MULTI_SERVEUR = 7,SESSION_MULTI_CLIENT = 8;
	private int state=MENU;
	FalldownCanvas fdc = new FalldownCanvas();
	Game game;

	int selectedGamePlay = 0;

	public final static Command cmdQuitter = new Command(Texts.get(0), Command.EXIT,1);
	public final  static Command cmdBack = new Command(Texts.get(1), Command.BACK, 1);
	public final static Command cmdCancel = new Command(Texts.get(2), Command.CANCEL, 2);
	public final static Command cmdAdd = new Command(Texts.get(3),Command.ITEM,2);
	public final static Command cmdOk = new Command(Texts.get(4), Command.OK,1);
	public final static Command cmdStart = new Command(Texts.get(5), Command.OK,1);

	List menuPrincipal = new List(Texts.get(6),List.IMPLICIT);
	Form formAuteur = new Form(Texts.get(7),new Item[]{new StringItem("",Texts.get(8))});

	TextField txtFieldEnterName = new TextField(Texts.get(9),"",30,TextField.ANY);
	Form formEnterName = new Form(Texts.get(10),new Item[] {txtFieldEnterName});

	StringItem itemResultat = new StringItem("","");
	Form formResultats = new Form(Texts.get(11),new Item[]{itemResultat});

	GamePlay[] gameplays;
	List menuGamePlay ;

	Form formHighScore = new Form(Texts.get(12));
	StringItem StrHighScore;

	ServerConnexions serverConnexions;
	ConnectToServer connectToServer;

	private List menuServerOrClient = new List(Texts.get(13),List.IMPLICIT, new String[]{Texts.get(14),Texts.get(15)},null);

	private List sessionServeur = new List(Texts.get(16),List.IMPLICIT);
	private Form sessionClient = new Form(Texts.get(17),new Item[]{new StringItem("",Texts.get(18))});


	public Falldown()
	{
		int numGamePlays = GamePlay.NUM_GAMEPLAY;


		gameplays =  new GamePlay[numGamePlays];
		String[] gamePlayNames = new String[numGamePlays];

		for(int i=0; i<numGamePlays ;i++ )
		{
			gameplays[i]=GamePlay.getGamePlay(i+1);
			gamePlayNames[i]=gameplays[i].nom;
		}

		menuGamePlay = new List(Texts.get(19),  List.IMPLICIT,gamePlayNames,null);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		menuPrincipal.append(Texts.get(20),null);
		menuPrincipal.append(Texts.get(21), null);
		menuPrincipal.append(Texts.get(22), null);
		menuPrincipal.append(Texts.get(23),null);
		menuPrincipal.append(Texts.get(24),null);


		menuPrincipal.addCommand(cmdQuitter);
		display.setCurrent(menuPrincipal);
		menuPrincipal.setCommandListener(this);

		menuGamePlay.setCommandListener(this);

		formAuteur.addCommand(cmdBack);
		formAuteur.setCommandListener(this);
		formResultats.addCommand(cmdOk);
		formResultats.setCommandListener(this);

		StrHighScore=new StringItem("",null);
		formHighScore.append(StrHighScore);
		formHighScore.addCommand(cmdBack);
		formHighScore.setCommandListener(this);

		formEnterName.addCommand(cmdOk);
		formEnterName.setCommandListener(this);

		menuServerOrClient.setCommandListener(this);

		sessionServeur.addCommand(cmdStart);
		sessionServeur.addCommand(cmdQuitter);
		sessionClient.addCommand(cmdQuitter);
		sessionServeur.setCommandListener(this);
		sessionClient.setCommandListener(this);

	}

	public void commandAction(Command c, Displayable d) {

		if(c.getCommandType()==Command.EXIT)
		{
			notifyDestroyed();
		}
		else if(d == menuPrincipal)
		{	
			int i = menuPrincipal.getSelectedIndex();
			switch (i)
			{
			case 0: 
				// new game
				display.setCurrent(menuGamePlay);
				break;
			case 1:
				display.setCurrent(menuServerOrClient);
				break;
			case 2:
				state = HIGHSCORE;
				display.setCurrent(menuGamePlay);
				break;
			case 3:
				display.setCurrent(formAuteur);
				break;
			case 4:
				notifyDestroyed();
			}			
		}
		else if (d==menuGamePlay)
		{
			if(state == HIGHSCORE)
			{
				StrHighScore.setText(gameplays[ menuGamePlay.getSelectedIndex()].meilleursScores.toString());
				display.setCurrent(formHighScore);
				state=MENU;
			}
			else if (state == SERVER_GP)
			{
				serverConnexions = new ServerConnexions();
				display.setCurrent(serverConnexions.form);
				serverConnexions.start();
				state = SERVERWAITING;
				new Thread(this).start();
			}
			else
			{
				startGame();
			}
		}
		else if(d == formResultats)
		{
			if(state==SESSION_MULTI_SERVEUR)
			{
				display.setCurrent(sessionServeur);
			}
			else if(state == SESSION_MULTI_CLIENT)
			{
				display.setCurrent(sessionClient);
			}
			else
			{
				display.setCurrent(menuPrincipal);
			}
		}
		else if(d== menuServerOrClient)
		{
			switch(menuServerOrClient.getSelectedIndex())
			{
			case 0:
				state = SERVER_GP;
				display.setCurrent(menuGamePlay);
				break;
			case 1:
				connectToServer = new ConnectToServer();
				connectToServer.displayOn(display);
				state = CLIENTWAITING;
				new Thread(this).start();
				break;
			}

		}
		else if (d==formEnterName)
		{
			String playerName = txtFieldEnterName.getString();
			int pos = gameplays[selectedGamePlay].meilleursScores.add(playerName,fdc.game.getScore());
			formResultats.setTitle(Texts.get(25) + gameplays[selectedGamePlay].meilleursScores.recordStoreName );
			itemResultat.setText(Texts.get(26)+ playerName + Texts.get(27) + pos + Texts.get(28)+ fdc.game.getScore()  );
			display.setCurrent(formResultats);
		}
		else if (d==sessionServeur)
		{
			if(c == cmdStart)
			{
				startServerGame();
			}
			else if(c == cmdQuitter)
			{
				display.setCurrent(menuPrincipal);
			}
		}
		else if(d==sessionClient)
		{
			
		}
		else if(c.getCommandType()==Command.BACK)
		{
			display.setCurrent(menuPrincipal);
		}
	}



	public void run() {
		while(state == GAME)
		{
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(fdc.game.isOver())
			{
				if(fdc.game instanceof ClientGame)
				{
					ClientGame g =(ClientGame) fdc.game;
					itemResultat.setText(Texts.get(29) + g.vainqueur +Texts.get(30) + g.getScore());
					g.close();
					state=MENU;
					display.setCurrent(formResultats);
					startClientGame();
				}
				else if(fdc.game instanceof ServerGame)
				{
					ServerGame g =(ServerGame) fdc.game;
					itemResultat.setText(Texts.get(29) + g.vainqueur +Texts.get(30) + g.getScore());
					g.close();
					//state=SESSION_MULTI_SERVEUR;
					state=MENU;
					display.setCurrent(formResultats);
				}
				else 
				{
					if(gameplays[selectedGamePlay].meilleursScores.isHigh(fdc.game.getScore()) )
					{
						display.setCurrent(formEnterName);
					}

					else
					{
						itemResultat.setText(Texts.get(31) + fdc.game.getScore());
						display.setCurrent(formResultats);
					}
					
					state = MENU;
				}
			}

		}
		while(state==SERVERWAITING)
		{
			if(serverConnexions.isFini())
			{
				startServerGame();
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(state==CLIENTWAITING)
		{
			if(connectToServer.isCommence())
			{
				startClientGame();
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

		}
	}

	private void startGame() {
		selectedGamePlay = menuGamePlay.getSelectedIndex();
		state=GAME;
		game =new LocalGame(gameplays[selectedGamePlay], new LocalPlayer(fdc,GameCanvas.LEFT_PRESSED,GameCanvas.RIGHT_PRESSED));
		game.setCanvas(fdc);
		display.setCurrent(fdc);
		Thread t = new Thread(this);
		t.start();
		game.start();
	}

	private void startClientGame() {
		Game game = new ClientGame(
				new LocalPlayer(fdc,GameCanvas.LEFT_PRESSED,GameCanvas.RIGHT_PRESSED)
				,
				connectToServer.getConnection());
		state=GAME;
		game.setCanvas(fdc);
		display.setCurrent(fdc);
		Thread t = new Thread(this);
		t.start();
		game.start();
	}

	public void startServerGame()
	{
		Game game = new ServerGame(GamePlay.getGamePlay(menuGamePlay.getSelectedIndex()+1),
				new LocalPlayer(fdc,GameCanvas.LEFT_PRESSED,GameCanvas.RIGHT_PRESSED),
				serverConnexions);
		state=GAME;
		game.setCanvas(fdc);
		display.setCurrent(fdc);
		Thread t = new Thread(this);
		t.start();
		game.start();
	}

}
