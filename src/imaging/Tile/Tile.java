package imaging.Tile;

import game.HitBox;
import javafx.scene.image.PixelReader;

public interface Tile {
	
	public int[][] getRGB(int tick);
	
	public int getWidth(int tick);
	
	public int getHeight(int tick);
	
	public void setRGB(PixelReader r);
	
	public HitBox getHitBox(int x, int y, int scale, int tick);
	
	public boolean isSolid(int tick);
	
	public boolean hasHitBox(int tick);
}
