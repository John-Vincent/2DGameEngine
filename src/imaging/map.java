package imaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import game.HitBox;
import imaging.Tile.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.WritablePixelFormat;
import script.Entity;

public class map {
	private final byte[][] tiles;
	private final byte[][] tiles2;
	private final Entity[] Entities;
	private final int width;
	private final int height;

	public map(String mapPath, int width, int height){
		Entities = null;
		tiles2 = null;
		this.width = width;
		this.height = height;
		//have to use toString with fx:Image
		Image image = new Image(game.game.DirRoot+mapPath);
		int[] pixels = new int[width*height];
		tiles = new byte[width][height];

		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels, 0, width);
		for(int i =0; i<width; i++){
			for(int j = 0; j<height; j++){
				tiles[i][j] = (byte)pixels[i+j*(width)];
			}
		}
	}

	public map(String mapPath, String map2Path, int width, int height){
    Entities = null;
		this.width = width;
		this.height = height;
    FileInputStream map1 = null;
    FileInputStream map2 = null;
    try{
      map1 = new FileInputStream(new File(game.game.DirRoot+mapPath));
      map2 = new FileInputStream(new File(game.game.DirRoot+map2Path));
    } catch(FileNotFoundException e){
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
		Image image = new Image(map1);
		int[] pixels1 = new int[width*height];
		int[] pixels2 = new int[width*height];
		tiles = new byte[width][height];
		tiles2 = new byte[width][height];

		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels1, 0, width);
		image = new Image(map2);
		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels2, 0, width);
		for(int i =0; i<width; i++){
			for(int j = 0; j<height; j++){
				tiles[i][j] = (byte)pixels1[i+j*(width)];
				int pix = pixels2[i+j*(width)];
				if(pix!= 0)
					tiles2[i][j] = (byte)pix;
				else
					tiles2[i][j] = (byte) 0xff;
			}
		}
	}

	public map(String mapPath, String map2Path, String EntityPath, int width, int height){
		this.width = width;
		this.height = height;
    FileInputStream map1 = null;
    FileInputStream map2 = null;
    FileInputStream entityMap = null;

    try{
      map1 = new FileInputStream(new File(game.game.DirRoot+mapPath));
      map2 = new FileInputStream(new File(game.game.DirRoot+map2Path));
      entityMap = new FileInputStream(new File(game.game.DirRoot+EntityPath));
    } catch(FileNotFoundException e){
      e.printStackTrace();
      System.exit(-1);
    }

		int[] pixels1 = new int[width*height];
		int[] pixels2 = new int[width*height];
		int[] pixels3 = new int[width*height];
		Image image = new Image(map1);
		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels1, 0, width);
		image = new Image(map2);
		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels2, 0, width);
		image = new Image(entityMap);
		image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), pixels3, 0, width);

		ArrayList<Entity> ents = new ArrayList<Entity>();
		tiles = new byte[width][height];
		tiles2 = new byte[width][height];
		for(int i =0; i<width; i++){
			for(int j = 0; j<height; j++){
				tiles[i][j] = (byte)pixels1[i+j*(width)];
				int pix = pixels2[i+j*(width)];
				if(pix!= 0)
					tiles2[i][j] = (byte)pix;
				else
					tiles2[i][j] = (byte) 0xff;
				pix = pixels3[i+j*(width)];
				if(pix!= 0)
					ents.add(new Entity(i*game.game.BASE*game.game.SCALE,j*game.game.BASE*game.game.SCALE, (byte)pix, (pix&0x0000ff00)>>8, 0));
			}
		}

		Entities = new Entity[ents.size()];
		ents.toArray(Entities);
	}

	public byte getTile(int x, int y){
		return tiles[x%width][y%height];
	}

	public byte getTile2(int x, int y){
		return tiles2[x%width][y%height];
	}

	public Entity[] getEntities(){
		return Entities;
	}

	public int getWidth(){
		return width*game.game.BASE*game.game.SCALE;
	}

	public int getHeight(){
		return height*game.game.BASE*game.game.SCALE;
	}

	public boolean hasTwoLayers(){
		return (tiles2!=null);
	}

	public boolean hasEntities(){
		return (Entities!=null);
	}


	public ArrayList<HitBox> getHitBoxes(HitBox box, GraphicsManager g, int x, int y, int tick){
		ArrayList<HitBox> boxes = new ArrayList<HitBox>();
		//collect all hitboxes for tiles touched by the entity as it moves
		boolean forward=false, down=false;
		if(x>0) forward = true;
		if(y>0) down = true;
		int end1 = ((forward) ? ((box.getXMax()+x)/game.game.BASE/game.game.SCALE):(box.getXMax()/game.game.BASE/game.game.SCALE)),
				end2 = ((down) ? ((box.getYMax()+y)/game.game.BASE/game.game.SCALE): (box.getYMax()/game.game.BASE/game.game.SCALE));

		for(int i = (forward) ? (box.getXMin()/game.game.BASE/game.game.SCALE):((box.getXMin()+x)/game.game.BASE/game.game.SCALE); i<= end1; i++){
			for(int j =(down) ? (box.getYMin()/game.game.BASE/game.game.SCALE): ((box.getYMin()+y)/game.game.BASE/game.game.SCALE); j<= end2; j++){
				if(i>0 &&i<tiles.length && j>0 && j<tiles[i].length){
					Tile t = g.getTile(tiles[i][j]);
					if(t.isSolid(tick))
						boxes.add(t.getHitBox(i*game.game.BASE*game.game.SCALE, j*game.game.BASE*game.game.SCALE, game.game.SCALE, tick));
					if(tiles2 != null && tiles2[i][j]!=(byte)0xff){
						t = g.getTile(tiles2[i][j]);
						if(t.isSolid(tick))
							boxes.add(t.getHitBox(i*game.game.BASE*game.game.SCALE, j*game.game.BASE*game.game.SCALE, game.game.SCALE, tick));
					}
				}
			}
		}
		if(Entities!=null)
			for(Entity e: Entities){
				HitBox b = g.getTile(e.getTile()).getHitBox(e.getX(), e.getY(), e.getScale(), tick);
				if(box.isClose(b))
					boxes.add(b);
			}

		return boxes;
	}

	public ArrayList<Byte> getTiles(HitBox box){
		ArrayList<Byte> answer = new ArrayList<Byte>();
		for(int i = box.getXMin()/game.game.BASE/game.game.SCALE; i<= box.getXMax()/game.game.BASE/game.game.SCALE; i++){
			for(int j =(box.getYMin()/game.game.BASE/game.game.SCALE); j<= box.getYMax()/game.game.BASE/game.game.SCALE; j++){
				if(i>0 &&i<tiles.length && j>0 && j<tiles[i].length){
					answer.add(tiles[i][j]);
					if(tiles2 != null && tiles2[i][j]!=(byte)0xff)
						answer.add(tiles2[i][j]);
				}
			}
		}
		return answer;
	}

}
