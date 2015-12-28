
public class Color {
	public int r, g, b;
	
	public final static Color red = new Color(255,0,0);
	public final static Color blue = new Color(0,0,255);
	
	public Color(int[] rgb)
	{
		this(rgb[0],rgb[1],rgb[2]);
	}
	
	public Color(int r, int g, int b)
	{
		this.r = r; this.g = g; this.b = b;
	}
}
