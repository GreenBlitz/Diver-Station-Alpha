package org.green4590.diversgui.application;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class MainController {
	
	@FXML
	private RadioButton rightBlue;
	
	@FXML
	private RadioButton middleBlue;
	
	@FXML
	private RadioButton leftBlue;
	
	@FXML
	private RadioButton rightRed;
	
	@FXML
	private RadioButton middleRed;
	
	@FXML
	private RadioButton leftRed;
	
	private ToggleGroup group = new ToggleGroup();
	
	public void initiateRadio(){
		rightRed.setToggleGroup(group);
		middleRed.setToggleGroup(group);
		leftRed.setToggleGroup(group);
		rightBlue.setToggleGroup(group);
		middleBlue.setToggleGroup(group);
		leftBlue.setToggleGroup(group);
	}
	
	@FXML
	private MenuButton selected;
	
	@FXML
	private void updateSelected(Event event){
		MenuItem temp = (MenuItem) event.getSource();
		selected.setText(temp.getText());
	}
	
	
	
	
	@FXML
	private TextFlow consoleFlow;
	
	@FXML
	private ScrollPane consolePane;
	
	private volatile Queue<String> linesToAdd = new PriorityQueue<String>();
	
	private consoleService consoleHandler = new consoleService();
	
	private class consoleService implements Runnable{
		TextFlow console;
		
		public void setConsole(TextFlow console){
			this.console = console;
		}
		
		@Override
		public void run() {
			while(!linesToAdd.isEmpty()){
				try {
					FXMLLoader line = new FXMLLoader(getClass().getResource("lineText.fxml"));
					Text text = line.load();
					text.setText(linesToAdd.remove());
					console.getChildren().add(text);
					if(!consolePane.isPressed()){
						consolePane.vvalueProperty().set(1);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private ScrollBar consoleScroll = null;
	
	public void initiateConsole(){
		consoleHandler.setConsole(consoleFlow);
	}
	
	@FXML
	public void consoleUpdater(){
		Platform.runLater(consoleHandler);
	}
	
	public void linesAdder(String lineType, String line){
		switch(lineType){
		case "Timer":
			linesToAdd.add("\n * time has changed to: " + line);
			break;
		default:
			linesToAdd.add("\n * " + line);
		}
	}
	
	@FXML
	private Text timer;
	
	public String getTimer(){
		return timer.getText();
	}
	
	@FXML
	private Button start;
	
	private int minutes = 2;
	private int seconds = 30;
    
	@FXML
	private void initiateTimer(){
		start.setDisable(true);
		Timer delay = new Timer();
		TimerTask consoleUpdate = new TimerTask(){
			@Override
			public void run() {
				consoleUpdater();
			}
		};
		
		TimerTask autonomousEnd = new TimerTask(){

			@Override
			public void run() {
				timer.setFill(Color.GREEN);
				linesAdder("" , "Autonomous Time has ended!");
			}
		};
		
		TimerTask countdown = new TimerTask(){

			@Override
			public void run() {
				if(seconds == 0){
					if(!(minutes == 0)){
						minutes--;
						seconds = 59;
					}
					else{
						linesAdder("" ,"Timer has ended!");
						this.cancel();
					}
				}
				else{
					seconds--;
				}
				if(seconds<10){
					timer.setText(minutes + ":" + "0" + seconds);
				}
				else{
					timer.setText(minutes + ":" + seconds);
				}
				linesAdder("Timer", timer.getText());
			}
			
		};
		
		delay.scheduleAtFixedRate(consoleUpdate , 10 , 1000);
		delay.schedule(autonomousEnd, 15000);
		delay.scheduleAtFixedRate(countdown, 1000, 1000);
	}
	
	
	// webcam
}
