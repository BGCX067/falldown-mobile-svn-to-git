
public class Level {
	private int numFloorsDuration, floorsOnScreen;
	private int[] gapProba;
	private double floorsSpeed;

	public Level(int numFloorsDuration, int numBlocks, int minGaps, int maxGaps, int floorsOnScreen, double floorsSpeed)
	{
		this.numFloorsDuration = numFloorsDuration;
		
		this.floorsOnScreen = floorsOnScreen;
		this.floorsSpeed = floorsSpeed;


		int[] gapProba = new int[numBlocks+1];
		this.gapProba = gapProba;
		
		for(int i=0;i<gapProba.length;i++)
		{
			gapProba[i]=0;
		}


		if(maxGaps == minGaps)
		{
			gapProba[maxGaps]=100;
		}
		else
		{
			int numGaps = maxGaps - minGaps+1;
			int proba = 100 / numGaps;
			if(minGaps > 0)
			{
				int i = minGaps;
				while(i<=maxGaps)
				{

					gapProba[i]=proba;


					i++;
				}
				if(100%numGaps > 0)
				{
					gapProba[i-1]+= 100%numGaps;
				}
			}

		}
	}

	public Level(int numFloorsDuration, int[] gapProba, int floorsOnScreen, double floorsSpeed)
	{
		this.numFloorsDuration = numFloorsDuration;
		this.gapProba = gapProba;
		this.floorsOnScreen = floorsOnScreen;
		this.floorsSpeed = floorsSpeed;
	}
	public Level(Level l)
	{
		this.numFloorsDuration = l.numFloorsDuration;
		this.gapProba = l.gapProba;
		this.floorsOnScreen = l.floorsOnScreen;
		this.floorsSpeed = l.floorsSpeed;
	}

	public Level fromDelta(Level delta,Level seuil)
	{
		Level toReturn = new Level(this);


		toReturn.numFloorsDuration = this.numFloorsDuration + delta.numFloorsDuration;
		if(toReturn.numFloorsDuration > seuil.numFloorsDuration)
		{
			toReturn.numFloorsDuration =seuil.numFloorsDuration;
		}

		toReturn.floorsOnScreen = this.floorsOnScreen + delta.floorsOnScreen;
		if(toReturn.floorsOnScreen > seuil.floorsOnScreen)
		{
			toReturn.floorsOnScreen =seuil.floorsOnScreen;
		}

		toReturn.floorsSpeed = this.floorsSpeed + delta.floorsSpeed;
		if(toReturn.floorsSpeed > seuil.floorsSpeed)
		{
			toReturn.floorsSpeed =seuil.floorsSpeed;
		}


		return toReturn;
	}

	public int getNumFloorsDuration() {
		return numFloorsDuration;
	}

	/*	public int getMinGaps() {
		for(int i=0; i<)
		return minGaps;
	}

	public int getMaxGaps() {
		return maxGaps;
	}
	 */
	public int getFloorsOnScreen() {
		return floorsOnScreen;
	}

	public double getFloorsSpeed() {
		return floorsSpeed;
	}

	public int getInterFloorSpaces(Game g){
		return (int) (g.HEIGHT/(getFloorsOnScreen()*floorsSpeed));
	}

	public void setNumFloorsDuration(int numFloorsDuration) {
		this.numFloorsDuration = numFloorsDuration;
	}

	/*	public void setMinGaps(int minGaps) {
		this.minGaps = minGaps;
	}

	public void setMaxGaps(int maxGaps) {
		this.maxGaps = maxGaps;
	}*/

	public void setFloorsOnScreen(int floorsOnScreen) {
		this.floorsOnScreen = floorsOnScreen;
	}

	public void setFloorsSpeed(double floorsSpeed) {
		this.floorsSpeed = floorsSpeed;
	}

	public int getNumBlocks() {
		return gapProba.length;
	}

	public int[] probas()
	{
		return gapProba;
	}

}
