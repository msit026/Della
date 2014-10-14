package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import della.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class teamController implements ControlledScreen,Initializable  {
	
	ScreensController myController;
	Connection conn;
	ArrayList<String> teams;

	
	@FXML
	private TextField f1;		
	@FXML
	private ListView<String> l1; 
	@FXML
	private ListView<String> l2; 
	@FXML
	private ListView<String> l3; 
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		conn = db_connect.connect();
		setData();
	}
	
	
	public teamController() {
			
		f1 = new TextField();
		l1 = new ListView<String>();
		l2 = new ListView<String>();
		l3 = new ListView<String>();
		//setData();
		
	}
	
	public void setData() {		
	
		teams = new ArrayList<String>();
		

		PreparedStatement ps;
		try {
			ps = conn.prepareStatement
				      ("select name from teams;");
		
		ResultSet rs = ps.executeQuery();
	    
		while ( rs.next() ) {
	     teams.add(rs.getString("name"));
		}
		
		
		
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		/*a1.setItems(FXCollections.observableList(members));
		b1.setItems(FXCollections.observableList(Arrays.asList("None")));
		c1.setItems(FXCollections.observableList(Arrays.asList("None")));
		d1.setItems(FXCollections.observableList(Arrays.asList("All Action Items")));
		List<String> values = Arrays.asList("Fix Bug #12", "Fix Bug #13", "Fix Bug #30");*/
		l1.setItems(FXCollections.observableList(teams));
	//	t1.setText("Fix the mouse");
		
		/*// Teams
		l1.setItems(FXCollections.observableList(Arrays.asList("Manoj", "Anil", "Harika")));
		// available members for teams
		l2.setItems(FXCollections.observableList(Arrays.asList("Team-12", "Team-13", "Team-14")));
		// current members for teams
		l3.setItems(FXCollections.observableList(Arrays.asList("Team-13")));*/
		
		
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		// TODO Auto-generated method stub
		myController = screenPage;
		
	}
	
	@FXML
    private void goToScreen1(ActionEvent event){
       myController.setScreen(Main.screen1ID);
    }
    
    @FXML
    private void goToScreen2(ActionEvent event){
       myController.setScreen(Main.screen2ID);
    }
    
    @FXML
    private void goToScreen3(ActionEvent event){
       myController.setScreen(Main.screen3ID);
    }

    @FXML
    private void addTeam(ActionEvent event) throws SQLException{
       
    	System.out.println(f1.getText() + "-->");
    	
		PreparedStatement ps = conn.prepareStatement
			      ("insert into teams values(?)");
		ps.setString(1, f1.getText());
	
		
	    if(ps.executeUpdate() == 1) {
	    	
	    	setData();

	    }
	    
	    
	  
    	
    }
	

}
