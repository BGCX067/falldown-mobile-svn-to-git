abstract public class Player 
{
	Ball ball;
	boolean over = false;
	
	

	abstract public int getMove();

	abstract public String getName();
	
	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
	
	public boolean isOver() {
		return over;
	}

	public void setOver() {
		this.over = true;
	}
	

	
}
