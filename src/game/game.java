package game;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.File;

import game.GUI.GameEnder;
import game.SnapShot.ShotEntity;
import input.InputHandler;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import script.ScriptManager;

public class game implements Runnable{
	public static int WIDTH;
	public static int HEIGHT;
	public static int SCALE;
	public static int BASE;
	public static String DirRoot;

	public String[] input;
	protected InputHandler keys = new InputHandler();

	private boolean running = false;
	private boolean debug;
	public ScriptManager js;
	public ImageView root;

	protected int tick=0;

	private int yoffset=0;
	private int xoffset=0;

	SnapShot curview= new SnapShot();
	GameEnder end;

	private Exchanger<SnapShot> exchanger;


	public game(ImageView root, String dirRoot, GameEnder end){
		this.root = root;
		this.end = end;
		DirRoot = dirRoot+ File.separator;
		exchanger = new Exchanger<SnapShot>();
		js = new ScriptManager(exchanger, keys);

		Map window = js.getWindow();

		debug = (boolean)window.get("debug");
		WIDTH = ((Number)window.get("width")).intValue();
		HEIGHT = ((Number)window.get("height")).intValue();
		BASE = ((Number)window.get("base")).intValue();
		SCALE = ((Number)window.get("scale")).intValue();
	}


	public void run(){
		js.start();
		running = true;
		try {
			curview = exchanger.exchange(curview, 1, TimeUnit.SECONDS);
			xoffset = curview.mapCoords[0];
			yoffset = curview.mapCoords[1];
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			Platform.runLater(() ->{
				end.endGame();
				running = false;
			});
			e.printStackTrace();
		}

		double lastTime = System.nanoTime();
		double secondtimer = System.currentTimeMillis();

		double timePerTick = 1000000000D/60D;

		double now = 0;
		double delta = 0;
    double mill = 0;

		ShotEntity[] ents = null;

		int lastTicks = 0;
		int fps=0;
		while(running){
			now = System.nanoTime();
			delta += (now-lastTime)/timePerTick;
			lastTime = now;

			if(delta>=1){
				if(tick-lastTicks<60){
					ents = tick();//Exchanger is called in the tick method allowing both threads to proceed once they have met
					tick++;
				}
				delta-=1;
			}
			fps++;
			render(tick, ents);

      mill = System.currentTimeMillis();
			if(mill-secondtimer >= 1000){
				//System.out.println("FPS:"+fps+" Ticks:"+(tick-lastTicks)+" Keys:"+keys.getkeysforConsol()[0]);
				fps = 0;
				lastTicks = tick;
				secondtimer = mill;
			}


		}
	}

	public ShotEntity[] tick(){
		try {
			curview = exchanger.exchange(curview, 1, TimeUnit.SECONDS);
			xoffset = curview.mapCoords[0];
			yoffset = curview.mapCoords[1];
			curview.tick = this.tick;
			if(xoffset<0)
				xoffset = 0;
			if(xoffset>curview.map.getWidth())
				xoffset = curview.map.getWidth();
			if(yoffset<0)
				yoffset = 0;
			if(yoffset>curview.map.getHeight())
				yoffset = curview.map.getHeight();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			Platform.runLater(() ->{
				end.endGame();
				running = false;
			});
		}
		return curview.getEntities();
	}

	private void render(int tick, ShotEntity[] ents){
		WritableImage image= new WritableImage(WIDTH,HEIGHT);
		int data[] = new int[WIDTH*HEIGHT];
		PixelWriter a = image.getPixelWriter();

		if(!curview.map.hasTwoLayers())
			for(int i = 0; i<WIDTH/BASE/SCALE+3;i++){
				for(int j = 0; j<HEIGHT/BASE/SCALE+3; j++){
					curview.sheet.draw(data, i*BASE*SCALE, j*BASE*SCALE, SCALE, curview.map.getTile(i+xoffset/BASE/SCALE, j+yoffset/BASE/SCALE), xoffset%(BASE*SCALE), yoffset%(BASE*SCALE), tick, debug);
				}
			}
		else
			for(int i = 0; i<WIDTH/BASE/SCALE+3;i++){
				for(int j = 0; j<HEIGHT/BASE/SCALE+3; j++){
					curview.sheet.draw(data, i*BASE*SCALE, j*BASE*SCALE, SCALE, curview.map.getTile(i+xoffset/BASE/SCALE, j+yoffset/BASE/SCALE), xoffset%(BASE*SCALE), yoffset%(BASE*SCALE), tick, debug);
					byte tile = curview.map.getTile2(i+xoffset/BASE/SCALE, j+yoffset/BASE/SCALE);
					if(tile!=(byte)0xff)
						curview.sheet.draw(data, i*BASE*SCALE, j*BASE*SCALE, SCALE, curview.map.getTile2(i+xoffset/BASE/SCALE, j+yoffset/BASE/SCALE), xoffset%(BASE*SCALE), yoffset%(BASE*SCALE), tick, debug);
				}
			}

		if(ents!=null)
			for(ShotEntity ent: ents)
				curview.sheet.drawCharacter(data, ent.data[0], ent.data[1], (byte)ent.data[2], xoffset, yoffset, (ent.data[3]==1), (ent.data[4]==1), ent.data[5], tick, debug);


		a.setPixels(0, 0,  WIDTH,  HEIGHT, PixelFormat.getIntArgbInstance(), data, 0,  WIDTH);
		Platform.runLater(new Runnable(){
			//@Override
			public void run() {
				// TODO Auto-generated method stub
				root.setImage(image);
			}

		});

	}
}
