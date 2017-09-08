package script;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import game.SnapShot;
import input.InputHandler;

public class ScriptManager extends Thread {

	ScriptEngine engine;

	Exchanger<SnapShot> exchanger;

	SnapShot shot = new SnapShot();

	ArrayList<Entity> ents = new ArrayList<Entity>();

	InputHandler keys;


	public ScriptManager(Exchanger<SnapShot> ex, InputHandler keys){
		exchanger = ex;
		this.keys = keys;
		ScriptEngineManager a = new ScriptEngineManager();
		engine = a.getEngineByExtension("js");

		try {
			engine.eval("var AnimatedTile = Java.type('imaging.Tile.AnimatedTile')");
			engine.eval("var GraphicsManager = Java.type('imaging.GraphicsManager'); var NormalTile = Java.type('imaging.Tile.NormalTile'); var JavaEntity = Java.type('script.Entity'); var Map = Java.type('imaging.map');");
			engine.put("JavaEntities", ents);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

  @SuppressWarnings("unchecked")
	public Map<String, Object> getWindow(){
		Map<String, Object> window = null;
		try {
			InputStream stream = new FileInputStream(game.game.DirRoot+"init.js");
			Reader r = new InputStreamReader(stream);
			window = (Map<String, Object>)engine.eval(r);
			r.close();
			stream.close();
		} catch (ScriptException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return window;
	}


	@Override
	public void run(){
		boolean running = true;
		try{
			engine.put("SnapShot", shot);
			String[] allkeys = keys.getkeys();
			engine.eval("var activeKeys = '"+allkeys[0]+"'; var pressedKeys = '"+allkeys[1]+ "'; init();");
			shot = exchanger.exchange(shot);
		} catch(ScriptException e){
			e.printStackTrace();
			running = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(running){
			try{
				engine.put("SnapShot", shot);
                String[] allkeys = keys.getkeys();
                if(allkeys[1].contains(" Backspace,"))
                	running = false;
                engine.eval("var activeKeys = '"+allkeys[0]+"'; var pressedKeys = '"+allkeys[1]+ "'; EntryPoint();");
				shot = exchanger.exchange(shot);

			} catch(ScriptException e){
				running = false;
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}


}
