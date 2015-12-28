import java.util.Random;


public class FloorGenerator 
{
	private Game game;
	private Random random = new Random();
	private Level curLevel;
	public LinkedList levels= new LinkedList( );
	public int floorsLeft;
	private int level = 0;
	private int ticks = 0;
	private GamePlay gamePlay;
	
	int deltasPos = 0;
	int deltaFirstTick = -1;
	Delta currentDelta = null;
	
	public FloorGenerator(Game g)
	{
		this.game = g;
		this.gamePlay = Game.gamePlay;
		levels = Game.gamePlay.getLevels().getCopy();
		setNextLevel();
	}

	private void setNextLevel() {

		curLevel = (Level) levels.removeFirst();

		
		floorsLeft = curLevel.getNumFloorsDuration();
		
		Floor.speed = curLevel.getFloorsSpeed();
		level++;
		
		
		if(levels.size()==0)
		{
			if(currentDelta == null)
			{
				deltaFirstTick = ticks;
				currentDelta =  Game.gamePlay.getDeltas()[deltasPos];
			}
			
			 System.out.println("" + ticks + " "  + deltaFirstTick);
			
			if(currentDelta.getPendant() > 0 && ticks - deltaFirstTick >= currentDelta.getPendant())
			{
				deltasPos++;
				deltaFirstTick = ticks;
				currentDelta= Game.gamePlay.getDeltas()[deltasPos];
				System.out.println("Delta pos : " + deltasPos);
			}
			
			Level newLevel = new Level(curLevel);
			Delta d = Game.gamePlay.getDeltas()[deltasPos];
			newLevel.setNumFloorsDuration(d.getTousLes());
			newLevel.setFloorsSpeed(newLevel.getFloorsSpeed() + d.getVarSpeed());
			levels.addLast(newLevel);
			
			System.out.println("new Level");
		}
	}

	/**
	 * cree un nouvel etage dans la partie si requis par le gameplay
	 * renvoie true si nouvel etage cree et ajoute
	 * renvoie false sinon
	 */
	public boolean update()
	{
		//System.out.println(Floor.speed);
		//System.out.println(tog.HEIGHT/(curLevel.getFloorsOnScreen()*Floor.speed));
		boolean newEtage = false;
		if( ticks % curLevel.getInterFloorSpaces(game) == 0 )
		{
			//System.out.println("creating");
			boolean[] gaps = new boolean[curLevel.getNumBlocks()];
			
			for(int i=0; i<gaps.length;i++)
				gaps[i]=false;
			
			int[] p = curLevel.probas();
			int total = 100;
			int i=0;
			while(i<p.length && total>0&& (random.nextInt(total) >= p[i]))
			{
				 total = total - p[i];
				 i++;
			}
			
			int numGaps = i;
			//System.out.println("num gaps : " + numGaps + " proba : " + p[i] + " total : " + total + "p[O] : " + p[0]);
			//System.out.println(numGaps);
			while(numGaps > 0)
			{
				int block = random.nextInt(gaps.length);
				if(gaps[block]==false)
				{
					gaps[block]=true;
					numGaps--;
				}
			}
			
			game.add(new Floor(gaps));
			floorsLeft--; 
			if(floorsLeft<=0)
			{
				setNextLevel();
			}
			newEtage = true;
		}
		ticks++;
		return newEtage;
	}

	public int getLevel() {
		return level;
	}




}
