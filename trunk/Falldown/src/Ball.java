

public class Ball
{
	public static final int LEFT = 1, RIGHT = 2, STAY = 3;
	
	private double x,y;
	private int width,height;
	private double vSpeed, hSpeed;
	private int hMove = Ball.STAY;
	
	public Ball(double x, double y, int width, int height,double hSpeed, double vSpeed)
	{
		this.setX(x); this.setY(y); this.setWidth(width); this.setHeight(height);
		this.hSpeed = hSpeed;
		this.vSpeed = vSpeed;
	}

	public void update()
	{
		double dx = 0;
		switch(hMove)
		{
			case Ball.LEFT:
				dx= - this.hSpeed;
				break;
			case Ball.RIGHT:
				dx = this.hSpeed;
				break;
		}
		double dy = vSpeed;
		Game.getMoveHandler().move(this, dx, dy);
	}
	
	
	public void move(int direction)
	{
		hMove = direction;
	}
	
	// Accessors
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
