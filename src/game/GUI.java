package game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;

public class GUI extends Application {
	public ImageView image;
	Alert alert = new Alert(Alert.AlertType.WARNING);
	Scene bscene;
	Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		this.stage = stage;

		HBox box = new HBox(10);
		box.setPadding(new Insets(10,10,10,10));
		Button LaunchFromWeb = new Button("Download and Run");
		Button Download = new Button("Download");
		Button LaunchFromFile = new Button("Run Game from Files");
		LaunchFromWeb.setDisable(true);
		Download.setDisable(true);
		box.setStyle("-fx-background-color: #101010");
		LaunchFromFile.setOnMouseClicked(EventHandler->{
			DirectoryChooser choose = new DirectoryChooser();
			File start = new File("."+ File.separator +"games");
			if(start.isDirectory()&& start.list().length!=0){
				choose.setInitialDirectory(new File("."+ File.separator +"games"));
				File directory = choose.showDialog(stage);
				if(directory!=null)
					runGame(directory.getAbsolutePath());
			}
			else{
				alert.setContentText("you don't have any games in your Games Folder");
				alert.show();
			}

		});

		HBox.setHgrow(Download, Priority.ALWAYS);
		HBox.setHgrow(LaunchFromWeb, Priority.ALWAYS);
		HBox.setHgrow(LaunchFromFile, Priority.ALWAYS);
		Download.setMaxWidth(Double.MAX_VALUE);
		LaunchFromFile.setMaxWidth(Double.MAX_VALUE);
		LaunchFromWeb.setMaxWidth(Double.MAX_VALUE);
		box.getChildren().addAll(LaunchFromFile, LaunchFromWeb, Download);

		BorderPane root = new BorderPane();
		root.setTop(box);
		root.setCenter(new browser(LaunchFromWeb, Download));
		bscene = new Scene(root,1000,800);
		runBrowser();
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(-1);
			}

		});

	}

	private void runBrowser(){

		stage.setScene(bscene);
		stage.setResizable(true);

	}

	private void runGame(String gameRoot){

		image = new ImageView();
		BorderPane root = new BorderPane();
		root.setCenter(image);

		game g = new game(image, gameRoot, new GameEnder());
		Thread t = new Thread(g);
		root.setPrefSize(game.WIDTH, game.HEIGHT);
		Scene scene = new Scene(root);
		scene.addEventHandler(KeyEvent.ANY, g.keys);

		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		t.start();
	}

	public static void main(String[] args){
		launch(args);
	}

	private class browser extends StackPane{
		private bridge bridge = new bridge();
		private boolean loaded=false;
		public browser(Button a, Button b){
			WebView view = new WebView();
			WebEngine webEngine = view.getEngine();
			 webEngine.getLoadWorker().stateProperty()
		        .addListener((obs, oldValue, newValue) -> {
		          if (newValue == Worker.State.SUCCEEDED) {

		            JSObject jsobj = (JSObject) webEngine.executeScript("window");
		            jsobj.setMember("Java", bridge);
		            loaded=true;
		            a.setDisable(false);
		            b.setDisable(false);
		          }
		        });
			webEngine.load("https://gamesite.collinvincent.com");
			a.setOnMouseClicked(EventHandler ->{
				if(loaded){
					webEngine.executeScript("getFiles();");
					if(bridge.filesSet())
						runGame(bridge.getDir());
				}
			});
			b.setOnMouseClicked(EventHandler ->{
				if(loaded){
					webEngine.executeScript("getFiles();");
					if(bridge.filesSet()){
						alert.setContentText("Download Complete");
						alert.show();
					}
				}

			});
	        getChildren().add(view);
		}
	}

	public class bridge{
		private boolean filesSet=false;
		private String path;

		public void makeTextFile(String path, String data){
			filesSet = true;
			path = path.replace(".txt", ".js");
			File f = new File(path);
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
				FileWriter wr = new FileWriter(f);
				wr.write(data);
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public boolean filesSet(){
			return filesSet;
		}

		public void makeImageFile(String path, String data){
			File f = new File(path);
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
				FileOutputStream wr = new FileOutputStream(f);
				wr.write(DatatypeConverter.parseBase64Binary(data));
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void setDir(String dir){
			path = new File(dir).getAbsolutePath();

		}

		public void filesNotSet(){
			filesSet = false;
			alert.setContentText("No Game Has Been Loaded");
			alert.show();
		}

		public String getDir(){
			return path;
		}

	}

	public class GameEnder{

		public void endGame(){
			runBrowser();
		}

	}

}
