import javax.microedition.lcdui.game.GameCanvas;


public class LocalPlayer extends Player {

	public static final int LEFT = 1, RIGHT = 2, NOT_ATTRIBUED = 3;
	private int leftKey, rightKey;
	GameCanvas fdc;

	public LocalPlayer(GameCanvas fdc, int leftKey, int rightKey)
	{
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.fdc = fdc;
	}

	public int keyValue(int key)
	{
		if(key==leftKey)
			return LocalPlayer.LEFT;
		else if(key == rightKey)
			return LocalPlayer.RIGHT;
		else
			return LocalPlayer.NOT_ATTRIBUED;
	}
	

	public int getLeftKey() {
		return leftKey;
	}

	public int getRightKey() {
		return rightKey;
	}

	public int getMove() {
		int keyState = fdc.getKeyStates();
		//for(int i=0; i<game.getBall().length;i++)
		//{
		if((keyState & getLeftKey()) != 0)
		{
			return Ball.LEFT;
		}
		else if((keyState & getRightKey()) != 0)
		{
			return Ball.RIGHT;
		}
		else
		{
			return Ball.STAY;
		}
	}

	public String getName() {
		return "local player";
	}

}
