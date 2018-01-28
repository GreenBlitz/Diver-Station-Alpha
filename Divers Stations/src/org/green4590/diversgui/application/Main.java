package org.green4590.diversgui.application;
	 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setFullScreen(true);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
			Scene scene = new Scene(loader.load(),1360,768);
			MainController controller = loader.getController();
			controller.initiateRadio();
			controller.initiateConsole();
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
