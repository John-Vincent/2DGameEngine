package imaging;

import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.HitBox;
import imaging.Tile.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class GraphicsManager {
	final public Tile[] tiles;

	public GraphicsManager(String path, Map<String,Tile> tiles){
    FileInputStream istream = null;

    try{
      istream = new FileInputStream(new File(game.game.DirRoot+path));
    } catch(FileNotFoundException e){
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
    
		Image image = new Image(istream);
		PixelReader r = image.getPixelReader();
		this.tiles = new Tile[tiles.size()];
		int i = 0;
		for(Tile t: tiles.values()){
			this.tiles[i++]=t;
			t.setRGB(r);
		}
	}

	public void draw(int[] data, int x, int y, int scale, byte tile, int xoffset, int yoffset, int tick, boolean debug){
		int width = tiles[tile].getWidth(tick), height = tiles[tile].getHeight(tick);
		int[][] RGB = tiles[tile].getRGB(tick);

		for(int i = 0; i< width; i++){
			for(int j = 0; j < height; j++){
				int rgb = RGB[i][j];
				if(rgb>>24!=0){
					for(int a = 0; a<scale; a++ ){
						for(int b = 0; b<scale; b++){
							int newx = x-xoffset+i*scale+a;
							int newy = y+j*scale+b-yoffset;
							if(newx<game.game.WIDTH && newy<game.game.HEIGHT&&newx>0&&newy>0){
								data[newx+newy*game.game.WIDTH]=rgb;
							}
						}
					}
				}
			}
		}
		if(debug&&tiles[tile].hasHitBox(tick))
			this.drawHitBox(tiles[tile].getHitBox(x-xoffset, y-yoffset, scale, tick), data);
	}

	public void drawCharacter(int[] data, int x, int y, byte tile, int xoffset, int yoffset, boolean invertx, boolean inverty, int scale, int tick, boolean debug){
		int rgb, width = tiles[tile].getWidth(tick), height = tiles[tile].getHeight(tick);
		if(x>xoffset+game.game.WIDTH||y>yoffset+game.game.HEIGHT|| x+width*scale<xoffset|| y+height*scale<yoffset)
			return;
		int[][] rgbArray = tiles[tile].getRGB(tick);

		for(int i = 0; i< width; i++){
			for(int j = 0; j < height; j++){
				if (invertx && inverty)
					rgb = rgbArray[width-i-1][height-j-1];
				else if(inverty)
					rgb = rgbArray[i][width-1-j];
				else if(invertx)
					 rgb = rgbArray[width-i-1][j];
				else
					 rgb = rgbArray[i][j];
				if(rgb>>24!=0){
					for(int a = 0; a<scale; a++ ){
						for(int b = 0; b<scale; b++){
							int newx = x-xoffset+i*scale+a;
							int newy = y+j*scale+b-yoffset;
							if(newx<game.game.WIDTH && newy<game.game.HEIGHT&&newx>0&&newy>0){
								data[newx+newy*game.game.WIDTH]=rgb;
							}
						}
					}
				}
			}
		}
		if(debug&&tiles[tile].hasHitBox(tick))
			this.drawHitBox(tiles[tile].getHitBox(x-xoffset, y-yoffset, scale, tick), data);
	}

	public Tile getTile(int x){
		return tiles[x];
	}

	public void drawHitBox(HitBox rec, int[] data) {
		int xmin = rec.getXMin();
		int xmax = rec.getXMax();
		int ymin = rec.getYMin();
		int ymax = rec.getYMax();

		for(int i = xmin; i<=xmax; i++){
			if(i>0&&i<game.game.WIDTH&&ymin>0&&ymin<game.game.HEIGHT)
				data[i+ymin*game.game.WIDTH] = 0xff0080;
			if(i>0&&i<game.game.WIDTH&&ymax>0&&ymax<game.game.HEIGHT)
				data[i+ymax*game.game.WIDTH] = 0xff0080;
		}
		for(int i = ymin; i<=ymax; i++){
			if(i>0&&i<game.game.HEIGHT&&xmin>0&&xmin<game.game.WIDTH)
				data[xmin+i*game.game.WIDTH] = 0xff0080;
			if(i>0&&i<game.game.HEIGHT&&xmax>0&&xmax<game.game.WIDTH)
				data[xmax+i*game.game.WIDTH] = 0xff0080;
		}

	}


}
