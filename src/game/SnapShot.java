package game;

import java.util.ArrayList;
import java.util.Arrays;

import imaging.GraphicsManager;
import imaging.map;
import imaging.Tile.Tile;
import script.Entity;

public class SnapShot {
	map map;
	int[] mapCoords = new int[2];
	GraphicsManager sheet;
	ShotEntity[] entities;
	ShotEntity[] mapentities;
	int tick;
	
	public SnapShot(){
		
	}
	
	public SnapShot(map m, GraphicsManager sprite, ArrayList<Entity> ents, int[] cords){
		this.map = m;
		this.sheet = sprite;
		this.mapCoords = cords;
		entities = new ShotEntity[ents.size()];
		for(int i = 0; i<ents.size();i++){
			entities[i] = ents.get(i).toSnapshot(this);
		}
		
	}
	public void setMap(map map){
		this.map = map;
		Entity[] temp;
		int i =0;
		if(map.hasEntities()){
			temp = map.getEntities();
			mapentities = new ShotEntity[temp.length];
			for(Entity e: temp){
				mapentities[i++] = e.toSnapshot(this);
			}
			Arrays.sort(mapentities);
		}
		else{
			mapentities = null;
		}
	}
	
	public void setMapCoords(int[] mapCoords){
		this.mapCoords = mapCoords;
	}
	
	public void setEntites(ArrayList<Entity> ents){
		int i = 0;
		
		entities = new ShotEntity[ents.size()];
		for(Entity e: ents){
			entities[i++] = e.toSnapshot(this);
		}
	}
	
	public void setSpriteSheet(GraphicsManager sheet){
		this.sheet = sheet;
	}
	
	public int[] getMapCoords(){
		return mapCoords;
	}
	
	public GraphicsManager getGraphics(){
		return sheet;
	}
	
	public map getMap(){
		return map;
	}
	
	protected ShotEntity[] getEntities(){
		Arrays.sort(entities);
		if(mapentities!=null){
			int limit = entities.length+mapentities.length;
			ShotEntity[] ans = new ShotEntity[limit];
			int i = 0, j = 0, compare=0;
			while(i<entities.length||j<mapentities.length){
				if(i==entities.length)
					compare=1;
				else if(j==mapentities.length)
					compare=-1;
				else
					compare = entities[i].compareTo(mapentities[j]);
				if(compare<0){
					ans[i+j] = entities[i];
					i++;
				}
				else{
					ans[j+i] = mapentities[j];
					j++;
				}
			}
			return ans;
		}
		return entities;
	}
	
	
	public class ShotEntity implements Comparable<ShotEntity> {
		public final int[] data = new int[6];
		
		public ShotEntity(int x, int y, int tile, int xbool, int ybool, int scale){
			data[0] = x;
			data[1] = y;
			data[2] = tile;
			data[3] = xbool;
			data[4] = ybool;
			data[5] = scale;
		}
		
		@Override
		public int compareTo(ShotEntity o) {
			Tile ot = sheet.getTile(o.data[2]), mt = sheet.getTile(data[2]);
			if(o==this)
				return 0;
			if(!ot.hasHitBox(tick)&&!mt.hasHitBox(tick))
				return 0;
			if(!ot.hasHitBox(tick))
				return 1;
			if(!mt.hasHitBox(tick))
				return -1;
			if(ot.getHitBox(o.data[0], o.data[1], o.data[5], tick).getYMin()>mt.getHitBox(data[0], data[1], data[5], tick).getYMin()){
				return -1;
			}
			if(ot.getHitBox(o.data[0], o.data[1], o.data[5], tick).getYMin()<mt.getHitBox(data[0], data[1], data[5], tick).getYMin())
				return 1;
			return 0;
		}

	}
	
	
}
