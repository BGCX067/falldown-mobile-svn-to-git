


public class GamePlay 
{
	public static final int NUM_GAMEPLAY = 5; 
	private static final GamePlay[] gp = new GamePlay[NUM_GAMEPLAY];
	
	public String nom;
	private int height = 600;
	private int width = 400;

	private double ballVSpeed;
	private double ballHSpeed;
	private int ballWidth ,ballHeight;
	
	private int blockHeight;
	
	
	
	private Level[] levels;
	private int deltaTime;
	private double deltaSpeed;
	private Delta[] deltas;
	
	MeilleursScores meilleursScores;
	
	int id;
	
	
	
	private GamePlay()
	{
		this(1);
	}
	
	private GamePlay(int type)
	{ 
		id = type;
		
		switch (type)
		{
		case 1:
			nom = Texts.get(32);
			height = 600;
			ballVSpeed = -10.0;
			ballHSpeed = 25.0;
			ballWidth  = 14 ;
			ballHeight = 14; 
			blockHeight=18;
			levels = new Level[]{new Level(20,new int[]{0,20,35,35,10,0,0},6,12),}; //new Level(20,9,1,6,6,12)
			deltaTime = 20;
			deltaSpeed = 0.05;
			deltas = new Delta[]{new Delta(20,0.5,400),new Delta(20,0.1,400),new Delta(20,0.01,-1)};
			
			/*ballVSpeed = -9.0;
			ballHSpeed = 20.0;
			ballWidth  = 14 ;
			ballHeight = 14; 
			blockHeight=18;
			levels = new Level[]{new Level(20,8,1,4,6,12),}; //new Level(20,9,1,6,6,12)
			deltaTime = 20;
			deltaSpeed = 0.05;*/
			break;
		case 2:
			nom = Texts.get(33);
			height = 600;
			ballVSpeed = -6.0;
			ballHSpeed = 20.0;
			ballWidth  = 14 ;
			ballHeight = 14;
			blockHeight=20;
			levels = new Level[]{
						new Level(10,6,1,1,1,20.0),
						};
			deltaTime = 10;
			deltaSpeed = 0.4;
			deltas = new Delta[]{new Delta(10,0.4,200),new Delta(10,0.1,100),new Delta(10,0.05,-1)};
			break;
		case 3:
			nom = Texts.get(34);
			height = 600;
			ballVSpeed = -9.0;
			ballHSpeed = 20.0;
			ballWidth  = 14 ;
			ballHeight = 14;
			blockHeight=20;
			levels = new Level[]{
						new Level(40,1,1,1,5,15.0),
						};
			deltaTime = 20;
			deltaSpeed = 0.2;
			deltas = new Delta[]{new Delta(20,0.2,300),new Delta(20,0.1,100),new Delta(20,0.05,-1)};
			break;
		case 4:
			nom = Texts.get(35);
			height = 600;
			ballVSpeed = -14.0;
			ballHSpeed = 30.0;
			ballWidth  = 14 ;
			ballHeight = 14;
			blockHeight=20;

			levels = new Level[]{
						new Level(40,5,1,4,4,19.0),
						};
			deltaTime = 20;
			deltaSpeed = 0.1;
			deltas = new Delta[]{new Delta(20,0.1,200),new Delta(20,0.02,300),new Delta(20,0.01,-1)};
			break;
		case 5:
			nom = Texts.get(36);
			height = 600;
			ballVSpeed = -14.0;
			ballHSpeed = 25.0;
			ballWidth  = 14 ;
			ballHeight = 14;
			blockHeight=20;
			levels = new Level[]{
						new Level(40,5,3,5,4,2.0),
						};
			deltaTime = 20;
			deltaSpeed = 0.15;
			deltas = new Delta[]{new Delta(20,0.15,-1)};
			break;
		}
		meilleursScores = new MeilleursScores(this.nom);
	}
	


	public int getWidth()
	{
		return width;
	}
	
	public int getBallWidth() {
		return ballWidth;
	}

	public int getBallHeight() {
		return ballHeight;
	}

	public LinkedList getLevels() {
		LinkedList l = new LinkedList();
		for (int i=0; i<levels.length;i++)
		{
			l.addLast(new Level(levels[i]));
		}
		return l;
	}

	public double getBallVSpeed() {
		return ballVSpeed;
	}

	public double getBallHSpeed() {
		return ballHSpeed;
	}

	public int getBlockWidth() {
		return (int) Math.ceil((double)width/levels[0].getNumBlocks());
	}

	public int getBlockHeight() {
		return blockHeight;
	}

	public int getHeight() {
		return height;
	}

	public int getDeltaTime() {
		return deltaTime;
	}

	public double getDeltaSpeed() {
		return deltaSpeed;
	}
	
	public int getId()
	{
		return id;
	}
	
	public Delta[] getDeltas()
	{
		return deltas;
	}
	
	public static GamePlay getGamePlay(int id)
	{
		if(gp[id-1]==null)
		{
			gp[id-1]=new GamePlay(id);
		}
		return gp[id-1];
		
	}

	
}
