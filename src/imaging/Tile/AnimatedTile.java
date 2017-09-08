package imaging.Tile;

import java.util.ArrayList;

import game.HitBox;
import javafx.scene.image.PixelReader;

public class AnimatedTile implements Tile {

	ArrayList<NormalTile> tiles = new ArrayList<NormalTile>();
	int time;
	
	public AnimatedTile(int time){
		this.time = time;
	}
	
	public void addTile(NormalTile... t){
		for(NormalTile a: t){
			tiles.add(a);
		}
	}
	
	//public AnimatedTile(int[][] t, int time){
		//for(int i = 0; i<t.length; i++){
		//	tiles.add(new NormalTile(t[i]));
		//}
		//this.time = time;
	//}
	
	@Override
	public int[][] getRGB(int tick) {
		return tiles.get(tick%(time*tiles.size())/time).rgb;
	}


	public HitBox getHitBox(int x, int y, int scale, int tick) {
		return tiles.get((tick%(time*tiles.size()))/time).getHitBox(x, y, scale, tick);
	}

	@Override
	public int getWidth(int tick) {
		return tiles.get((tick%(time*tiles.size()))/time).getWidth(tick);
	}

	@Override
	public int getHeight(int tick) {
		return tiles.get((tick%(time*tiles.size()))/time).getHeight(tick);
	}

	@Override
	public void setRGB(PixelReader r) {
		for(NormalTile t: tiles){
			if(t.isSet()==false)
				t.setRGB(r);
		}
	}

	@Override
	public boolean isSolid(int tick) {
		return tiles.get((tick%(time*tiles.size()))/time).isSolid;
	}

	@Override
	public boolean hasHitBox(int tick) {
		return tiles.get((tick%(time*tiles.size()))/time).hasHitBox(tick);
	}
	
	
}
