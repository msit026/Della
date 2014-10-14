package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import persistence.DBManager;
import persistence.DataManager;
import users.User;
import della.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class controller implements ControlledScreen {
	
	
	ScreensController myController;
	public static User user;
	@FXML
	private ComboBox<String> a1;	
	@FXML
	private ComboBox<String> b1;
	@FXML
	private ComboBox<String> c1;
	@FXML
	private ComboBox<String> d1;	
	@FXML
	private ListView<String> l1; 
	@FXML
	private TextArea t1;
	
	
	@FXML
	private void initialize() {
		
		//l1 = new ListView<String>();
		
    }
	
	public controller() {
		
		a1 = new ComboBox<String>();
		b1 = new ComboBox<String>();
		c1 = new ComboBox<String>();
		d1 = new ComboBox<String>();		
		l1 = new ListView<String>();
		t1 = new TextArea();
		setData();
	}
	
	public void setData()  {	
		
//		ArrayList<String> members = new ArrayList<String>();
//		
//		Connection conn = db_connect.connect();
//		PreparedStatement ps;
//		try {
//			ps = conn.prepareStatement
//				      ("select username from login;");
//		
//		ResultSet rs = ps.executeQuery();
//	    
//		while ( rs.next() ) {
//	     members.add(rs.getString("username"));
//		}
//		
//		
//		
//		
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
		ArrayList<String> members = DBManager.getAllMembers();
		if(members == null)
			members = DataManager.getAllMembers();
		if(members == null)
			members = new ArrayList<String>();
		a1.setItems(FXCollections.observableList(members));
		b1.setItems(FXCollections.observableList(Arrays.asList("None")));
		c1.setItems(FXCollections.observableList(Arrays.asList("None")));
		d1.setItems(FXCollections.observableList(Arrays.asList("All Action Items")));
		List<String> values = Arrays.asList("Fix Bug #12", "Fix Bug #13", "Fix Bug #30");
		l1.setItems(FXCollections.observableList(values));
		t1.setText("Fix the mouse");
		
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		// TODO Auto-generated method stub
		myController = screenPage;
		setData();
		
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
    private void goToScreen4(ActionEvent event){
       myController.setScreen(Main.screen4ID);
    }
	

}
