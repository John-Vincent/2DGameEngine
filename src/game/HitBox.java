package game;

import java.util.ArrayList;

public class HitBox {
	int x,y,width,height;
	boolean Solid;
	
	public HitBox(int x, int y, int width, int height, boolean solid){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		Solid = solid;
	}
	
	public int getXMin(){
		return x;
	}
	
	public int getXMax(){
		return x+width;
	}
	
	public int getYMin(){
		return y;
	}
	
	public int getYMax(){
		return y+height;
	}
	
	protected static boolean contains(int min1, int max1, int min2, int max2){
		return (min1<max2 && min1>min2)||(max1>min2&&max1<max2) || (min2>min1&&min2<max1) || (max2>min1 && max2<max1) || (min1==min2);
	}
	
	public HitBox getTranslation(int x, int y){
		return new HitBox(this.x+x, +this.y+y, this.width, this.height, this.Solid);
	}
	
	public boolean isSolid(){
		return Solid;
	}
	
	public boolean isClose(HitBox b){
		if(Math.abs(b.getXMin()-this.getXMax())<100 && Math.abs(b.getYMin()-this.getYMax())<100)
			return true;
		if(Math.abs(b.getXMax()-this.getXMin())<100&& Math.abs(b.getYMax()-this.getYMin())<100)
			return true;
		return false;
	}
	
	public static boolean rightCollision(HitBox x, ArrayList<HitBox> list){
		for(HitBox h: list){
			if(x.getXMax() >= h.getXMin() && h.getXMin()>= x.getXMin() && contains(x.y,x.getYMax(), h.y, h.getYMax()))
				return true;
		}
		return false;
	}
	
	public static boolean leftCollision(HitBox x, ArrayList<HitBox> list){
		for(HitBox h: list){
			if(x.getXMin() <= h.getXMax() && h.getXMax()<= x.getXMax() && contains(x.y, x.getYMax(), h.y, h.getYMax()))
				return true;
		}
		return false;
	}
	
	public static boolean topCollision(HitBox x, ArrayList<HitBox> list){
		for(HitBox h: list){
			if(x.getYMin() <= h.getYMax()&& h.getYMax() <= x.getYMax()  && contains(x.x, x.getXMax(), h.x, h.getXMax()))
				return true;
		}
		return false;
	}
	
	public static boolean bottomCollision(HitBox x, ArrayList<HitBox> list){
		for(HitBox h: list){
			if(x.getYMax()>= h.getYMin() && h.getYMin() >= x.getYMax() && contains(x.x, x.getXMax(), h.x, h.getXMax()))
				return true;
		}
		return false;
	}
	
}
