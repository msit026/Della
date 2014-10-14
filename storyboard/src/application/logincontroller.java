package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

import persistence.DBManager;
import persistence.DataManager;
import users.User;
import della.Main;

public class logincontroller implements ControlledScreen {
	
	
	ScreensController myController;

	
	//login
	@FXML
	private TextField l1;
	
	@FXML
	private PasswordField l2;
	
	@FXML
	private Button login;
	
	
	
	//register
	@FXML
	private TextField r1;
	
	@FXML
	private TextField r2;
	
	@FXML
	private PasswordField r3;
	
	@FXML
	private PasswordField r4;
	
	@FXML
	private Button register;
	
	@FXML
	private Label message;
	
	@FXML
	private void initialize() {
		
		//l1 = new ListView<String>();
		
    }
	
	public logincontroller() {
		
		l1 = new TextField();
		l2 = new PasswordField();
		
		r1 = new TextField();
		r2 = new TextField();
		r3 = new PasswordField();
		r4 = new PasswordField();
		
		login = new Button();
		register = new Button();
		
		message = new Label();
		
	}
	
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		// TODO Auto-generated method stub
		myController = screenPage;		
	}
	
	@FXML
    private void register(ActionEvent event) throws SQLException{
       
		System.out.println("Register Called");
		
		if(!r3.getText().equals(r4.getText())) {
			message.setText("Enter same Password in both password fields");
			return;
		}
		
		Connection conn = db_connect.connect();
		PreparedStatement ps = conn.prepareStatement
			      ("insert into login values(?,?,?)");
		ps.setString(1, r2.getText());
		ps.setString(2, r3.getText());
		ps.setString(3, r1.getText());
		
		
	    if(ps.executeUpdate() == 1) 
	    	message.setText("Registration Done");
	    else
	    	message.setText("Registration Failed");
	    
	    conn.close();
		
    }
	@FXML
    private void login(ActionEvent event) throws SQLException{
		
		if(DBManager.login(l1.getText(), l2.getText())) {
			controller.user = new User(l1.getText(), l2.getText());
			DBManager.readData();
			Main.mainContainer.loadScreen(Main.screen1ID, Main.screen1File);
	        System.out.println("3");
	        Main.mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
	        System.out.println("4");
	        Main.mainContainer.loadScreen(Main.screen3ID, Main.screen3File);
	        System.out.println("5");
	        Main.mainContainer.loadScreen(Main.screen4ID, Main.screen4File);
	        System.out.println("6");
			myController.setScreen(Main.screen1ID);			
		}
		else if(DataManager.login(l1.getText(), l2.getText())) {
			controller.user = new User(l1.getText(), l2.getText());
			DataManager.readData();
			myController.setScreen(Main.screen1ID);	
		}
		else
			message.setText("Login Failed");
       
    }

}
