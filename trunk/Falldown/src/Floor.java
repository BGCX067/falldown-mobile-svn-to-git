
public class Floor {
	public static double speed = 0.25;
	public static int height = 5;
	double y = -10;
	private boolean[] gaps;
	float blockWidth;
	
	public Floor(boolean gaps[])
	{
		this.gaps = gaps;
		this.blockWidth = Game.gamePlay.getBlockWidth();
		this.height = Game.gamePlay.getBlockHeight();
	}
	
	public void update()
	{
		Game.getMoveHandler().move(this,speed);
	}
	
	public double getY()
	{
		return y;
	}
	
	public boolean collideWith(Ball b)
	{
		if (b.getY()+b.getHeight() <= this.y || this.y + this.height <= b.getY())
			return false;
		
		if( gaps[getBlockNumber(b.getX())] && gaps[getBlockNumber(b.getX()+b.getWidth()-1)])
			return false;
		
		return true;
	}
	
	public int getBlockNumber(double x)
	{
		return (int) (x/(int)getBlockWidth());
	}
	
	public float getBlockWidth()
	{
		return blockWidth;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public boolean isHigherThan(Ball b)
	{
		return b.getY()+b.getHeight()<=this.getY();
	}

	public boolean[] getGaps() {
		return gaps;
	}

	public void setGaps(boolean[] gaps) {
		this.gaps = gaps;
	}

}
