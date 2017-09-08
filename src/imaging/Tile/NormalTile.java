package imaging.Tile;

import java.awt.geom.Rectangle2D;

import game.HitBox;
import javafx.scene.image.PixelReader;

public class NormalTile implements Tile {
	
	int rgb[][];
	boolean Set = false;
	
	final boolean isSolid;
	final int[] box;
	
	final int width;
	final int height;
	private final int x,y;
	
	public NormalTile(int x, int y, int width, int height){
		rgb = new int[width][height];
		this.width = width;
		this.height= height;
		this.x = x;
		this.y = y;
		box = null;
		isSolid = false;
	}
	
	public NormalTile(int x,int y, int width, int height, int hx, int hy, int hwidth, int hheight, boolean isSolid){
		rgb = new int[width][height];
		this.width = width;
		this.height= height;
		this.x = x;
		this.y = y;
		this.isSolid = isSolid;
		box = new int[] {hx,hy,hwidth,hheight};
	}
	
	
	public void setRGB(PixelReader ir){
		Set = true;
		for(int i = 0; i<width; i++){
			for(int j = 0; j<height; j++){
				rgb[i][j] = ir.getArgb(i+x, j+y);
			}
		}
	}
	
	
	public boolean hasHitBox(int tick){
		return (box!=null);
	}
	
	//returns hitbox with current scale modified to it.
	public HitBox getHitBox(int x, int y, int scale, int tick){
		return new HitBox(x+box[0]*scale,y+box[1]*scale,box[2]*scale,box[3]*scale, isSolid);
	}

	@Override
	public int[][] getRGB(int tick) {
		return rgb;
	}

	@Override
	public int getWidth(int tick) {
		return width;
	}

	@Override
	public int getHeight(int tick) {
		return height;
	}

	
	protected boolean isSet(){
		return this.Set;
	}

	@Override
	public boolean isSolid(int tick) {
		return isSolid;
	}
	
}
