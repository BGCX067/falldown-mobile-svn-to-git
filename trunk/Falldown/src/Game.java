

abstract public class Game implements Runnable
{
	public static GamePlay gamePlay;

	public static int WIDTH,HEIGHT;
	//private Ball[] balls;
	long ticks;
	LinkedList floors;
	private FloorGenerator floorGenerator;
	private static MoveHandler moveHandler;
	int floorsPassed;
	boolean over;
	public Player[] players;
	boolean newFloorThisFrame;

	long lastUpdate;
	long delais = 30;
	int retard = 0;

	FalldownCanvas fdc;

	Game()
	{

	}

	public Game(GamePlay gp)
	{
		Game.gamePlay = gp;
	}

	public void init()
	{
		Game.WIDTH = gamePlay.getWidth();
		Game.HEIGHT = gamePlay.getHeight();
		//balls = new Ball[players.length];
		int gap = Game.WIDTH / (players.length + 1);
		for (int i=0; i<players.length; i++)
		{
			players[i].setBall(new Ball(gap*(i+1) - gamePlay.getBallWidth()/2,Game.HEIGHT-50, gamePlay.getBallWidth(), gamePlay.getBallHeight(),gamePlay.getBallHSpeed(),gamePlay.getBallVSpeed()));
		}
		ticks = 0;
		floors = new LinkedList();
		floorGenerator = new FloorGenerator(this);
		moveHandler= new MoveHandler(this,players,floors);
		floorsPassed=0;
		newFloorThisFrame = false;
		lastUpdate = System.currentTimeMillis();
	}

	public void start()
	{
		new Thread(this).start();
	}

	public void update()
	{
		handleMoves();
		updateBalls();
		floorsUpdate();
		newFloorThisFrame = floorGenerator.update();
		checkOver();
		ticks++;
	}

	void handleMoves() {
		for(int i=0; i<players.length; i++)
		{
			players[i].getBall().move(players[i].getMove());
		}
	}

	abstract void checkOver();


	void updateBalls()
	{
		for (int i = 0; i < players.length; i++) {
			players[i].getBall().update();
		}
	}

	void floorsUpdate() {
		Iterator it = floors.iterator();
		while(it.hasNext())
		{
			((Floor) it.next()).update();
		}
		removeOldFloors();
	}

	private void removeOldFloors()
	{

		while(! floors.isEmpty())
		{
			if(((Floor)floors.getFirst()).getY() > Game.HEIGHT)
			{		
				floors.removeFirst();

				floorsPassed++;

			}
			else
				break;
		}

	}


	public long getTicks() {
		return ticks;
	}

	public void add(Floor f)
	{
		floors.addLast(f);
	}

	public static MoveHandler getMoveHandler()
	{
		return moveHandler;
	}

	public Player[] getPlayers() {
		return players;
	}

	public LinkedList getFloors() {
		return floors;
	}

	public int getFloorsPassed() {
		return floorsPassed;
	}

	public boolean isOver() {
		return over;
	}

	public int getLevel()
	{
		return floorGenerator.getLevel();
	}

	public int getScore()
	{
		return floorsPassed * floorsPassed;
	}

	public void run() {
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

			//System.out.println("updatesLate : "+ numUpdatesLate);

			for(int i=0; i<numUpdatesLate;i++)
			{
				update();
			}

			fdc.repaint();

		}
	}

	public void setCanvas(FalldownCanvas canvas)
	{
		this.fdc = canvas;
		fdc.setGame(this);
	}

}
