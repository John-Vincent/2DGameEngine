package script;

import java.util.ArrayList;

import game.HitBox;
import game.SnapShot;
import game.SnapShot.ShotEntity;
import imaging.GraphicsManager;
import imaging.map;
import imaging.Tile.Tile;

public class Entity {
	
	private int x, y, tile, scale, movespeed;
	
	private ScreenScrollBox scroll;
	
	private boolean invertX=false, invertY=false, noClip=false, MapBounded = true, BottomDetection=true, TopDetection=true, LeftDetection = true, RightDetection = true,
			RightCollision = false, LeftCollision = false, TopCollision = false, BottomCollision = false;
	
	public Entity(int x, int y, int tile, int scale, int movespeed){
		this.x = x;
		this.y = y;
		this.tile = tile;
		this.scale = scale;
		this.movespeed = movespeed;
	}
	
	public void setScreenScrollBox(Number xmin, Number ymin, Number xmax, Number ymax, boolean x, boolean y){
		scroll = new ScreenScrollBox(xmin.intValue(), ymin.intValue(), xmax.intValue(), ymax.intValue(), x, y);
	}

	public int getX() {
		return x;
	}
	
	public void setMapBounded(boolean bounded){
		MapBounded = bounded;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isRightCollided() {
		return RightCollision;
	}

	public void setRightCollision(boolean rightCollision) {
		RightDetection = rightCollision;
	}

	public boolean isLeftCollided() {
		return LeftCollision;
	}

	public void setLeftCollision(boolean leftCollision) {
		LeftDetection = leftCollision;
	}

	public boolean isTopCollided() {
		return TopCollision;
	}

	public void setTopCollision(boolean topCollision) {
		TopDetection = topCollision;
	}

	public boolean isBottomCollided() {
		return BottomCollision;
	}

	public void setBottomCollision(boolean bottomCollision) {
		BottomDetection = bottomCollision;
	}

	public void toggleInvertX(){
		invertX = !invertX;
	}
	
	public void toggleInvertY(){
		invertY = !invertY;
	}

	public int getTile() {
		return tile;
	}

	public void setTile(int tile) {
		this.tile = tile;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getMovespeed() {
		return movespeed;
	}

	public void setMovespeed(int movespeed) {
		this.movespeed = movespeed;
	}
	
	public boolean isInvertX() {
		return invertX;
	}

	public void setInvertX(boolean invertX) {
		this.invertX = invertX;
	}

	public boolean isInvertY() {
		return invertY;
	}

	public void setInvertY(boolean invertY) {
		this.invertY = invertY;
	}
	
	public void setNoClip(boolean noClip){
		this.noClip = noClip;
	}
	
	public int[] makeMove(int xspeed, int yspeed, GraphicsManager g, int[] coords, map map, ArrayList<Entity> ents, int tick){
		//todo
		Tile mytile = g.getTile(tile);
		HitBox box = mytile.getHitBox(this.x, this.y, this.scale, tick);
		if(!noClip && box.isSolid()){
			ArrayList<HitBox> boxes= map.getHitBoxes(box, g, xspeed, yspeed, tick);
			for(Entity e: ents){
				if(e!=this){
					Tile t = g.getTile(e.getTile());
					if(t.isSolid(tick))
						boxes.add(t.getHitBox(e.x, e.y, e.scale, tick));
				}
			}
			int[] move = getMaxMove(box, boxes, xspeed, yspeed, map.getWidth(), map.getHeight());
			this.x += move[0];
			this.y += move[1];
		}
		else{
			this.x += xspeed;
			this.y += yspeed;
		}
		if(this.scroll != null){
			coords[1] = scroll.yChange(box, yspeed, coords[1], map.getHeight());
			coords[0] = scroll.xChange(box, xspeed,coords[0], map.getWidth());
		}
		//arrays from Nashorn are actually deep copies so changes will not be reflected script side;
		return coords;
	}
	
	private int[] getMaxMove(HitBox box, ArrayList<HitBox> boxes, double  xspeed, double  yspeed, int mapwidth, int mapheight){
		RightCollision = false; LeftCollision = false; TopCollision = false; BottomCollision = false;
		double  abscontrol = Math.abs(yspeed);
		boolean xpos = xspeed>0, ypos = yspeed>0, xneg = xspeed<0, yneg = yspeed<0, xfound=false, yfound=false;
		int xmax=(int)xspeed, ymax=(int)yspeed;
		if(Math.abs(xspeed)>abscontrol)
			abscontrol = Math.abs(xspeed);
		for(int i = 0; i<=abscontrol; i++){
			int y = (int)(Math.round(i/abscontrol*yspeed));
			int x = (int)(Math.round(i/abscontrol*xspeed));
			HitBox t = box.getTranslation(x,y);
			boolean atMapXEdge = (t.getXMin()<=0&&xneg || t.getXMax()>=mapwidth&&xpos), atMapYEdge = (t.getYMin()<=0&&yneg ||t.getYMax() >= mapheight&&ypos);
			boolean xCollision = (xpos && HitBox.rightCollision(t, boxes) && RightDetection) || (xneg && HitBox.leftCollision(t, boxes) && LeftDetection);
			boolean yCollision = (ypos && HitBox.bottomCollision(t, boxes) && BottomDetection) || (yneg && HitBox.topCollision(t, boxes) && TopDetection);
			if(!xfound && (xCollision  || (atMapXEdge&&this.MapBounded)) ){
				xmax = x;
				xfound = true;
				if(xpos)
					RightCollision = true;
				else
					LeftCollision = true;
			}
			if(!yfound && (yCollision || (atMapYEdge&&this.MapBounded))){
				ymax = y;
				yfound = true;
				if(ypos)
					BottomCollision = true;
				else
					TopCollision = true;
			}		
		}
		return new int[] {xmax, ymax};
	}
	
	public boolean isIn(byte tile, map map, GraphicsManager g, int tick){
		HitBox box = g.getTile(this.tile).getHitBox(this.x, this.y, this.scale, tick);
		ArrayList<Byte> tiles = map.getTiles(box);
		if(tiles.size()==0)
			return false;
		for(byte t: tiles){
			if(t!= tile)
				return false;
		}
		return true;
	}
	
	public boolean isTouching(byte tile, map map, GraphicsManager g, int tick){
		HitBox box = g.getTile(this.tile).getHitBox(this.x, this.y, this.scale, tick);
		ArrayList<Byte> tiles = map.getTiles(box);
		if(tiles.size()==0)
			return false;
		for(byte t: tiles){
			if(t == tile)
				return true;
		}
		return false;
	}
	
	public ShotEntity toSnapshot(SnapShot a){
		int inx = 0, iny = 0;
		if(invertX)
			inx = 1;
		if(invertY)
			iny = 1;
		return a.new ShotEntity(x, y, tile, inx, iny, scale);
	}
	


	private class ScreenScrollBox extends HitBox {
		private boolean xscroll, yscroll;
		public ScreenScrollBox(int xmin, int ymin, int width, int height, boolean x, boolean y){
			super( xmin, ymin, width, height,false);
			this.xscroll = x;
			this.yscroll = y;
		}
	
		public int xChange(HitBox x, int speed, int offset, int mapWidth){
			if(!xscroll)
				return offset;
			if((speed<0&&x.getXMin()-offset<=this.getXMin())||(speed>0&&x.getXMax()-offset>=this.getXMax()))
				offset += speed;
			if(offset+game.game.WIDTH>mapWidth)
				offset = mapWidth-game.game.WIDTH;
			if(offset <0)
				offset = 0;
			return offset;
		}
	
		public int yChange(HitBox y, int speed, int offset, int mapHeight){
			if(!yscroll)
				return offset;
			if((speed<0&&y.getYMin()-offset<=this.getYMin())||(speed>0&&y.getYMax()-offset>=this.getYMax()))
				offset += speed;
			if(offset+game.game.HEIGHT>mapHeight)
				offset = mapHeight-game.game.HEIGHT;
			if(offset <0)
				offset = 0;
			return offset;
		}
	
	}
	
}