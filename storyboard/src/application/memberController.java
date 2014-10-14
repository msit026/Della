package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import della.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class memberController implements ControlledScreen,Initializable {
	
	ArrayList<String> members;

	ScreensController myController;
	
	Connection conn;
	
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
		// TODO Auto-generated method stub
		conn = db_connect.connect();
		setData();
		
	}
	
	public memberController() {
		
		f1 = new TextField();
		l1 = new ListView<String>();
		l2 = new ListView<String>();
		l3 = new ListView<String>();
		
	}
	
	public void setData()  {	
		
		members = new ArrayList<String>();
		

		PreparedStatement ps;
		try {
			ps = conn.prepareStatement
				      ("select name from members;");
		
		ResultSet rs = ps.executeQuery();
	    
		while ( rs.next() ) {
	     members.add(rs.getString("name"));
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
		l1.setItems(FXCollections.observableList(members));
	//	t1.setText("Fix the mouse");
		
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
    private void goToScreen4(ActionEvent event){
       myController.setScreen(Main.screen4ID);
    }
    
    
    @FXML
    private void addMember(ActionEvent event) throws SQLException{
       
    	System.out.println(f1.getText() + "-->");
    	
		PreparedStatement ps = conn.prepareStatement
			      ("insert into members values(?)");
		ps.setString(1, f1.getText());
	
		
	    if(ps.executeUpdate() == 1) {
	    	
	    	setData();

	    }

    	
    }
    
    @FXML
    private void removeMember(ActionEvent event) throws SQLException{
       
    	System.out.println(f1.getText() + "-->");
    	
    	PreparedStatement ps = conn.prepareStatement
			      ("delete from associatedMT where member=?");
		ps.setString(1, l1.getSelectionModel().getSelectedItem());

	
		
	    ps.executeUpdate();
	    	
	    	ps = conn.prepareStatement
				      ("delete from members where name=? ");
			ps.setString(1, l1.getSelectionModel().getSelectedItem());
		
			
		    if(ps.executeUpdate() == 1) {
		    	
		    	setData();

		    }
	    	
	    }
    

    	
    
    
    @FXML
    private void addToCurrentTeams(ActionEvent event) throws SQLException{
       
    	System.out.println("from current");
    	ArrayList<String> currentTeams = new ArrayList<String>();
    	ArrayList<String> availableTeams = new ArrayList<String>();

    	
		PreparedStatement ps = conn.prepareStatement
			      ("insert into associatedMT values(?,?)");
		ps.setString(1, l1.getSelectionModel().getSelectedItem());
		ps.setString(2, l2.getSelectionModel().getSelectedItem());
	
		
	    if(ps.executeUpdate() == 1) {
	    	
	    	ps = conn.prepareStatement
				      ("select team from associatedMT where member=?");
			ps.setString(1, l1.getSelectionModel().getSelectedItem());
		
			ResultSet rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				currentTeams.add(rs.getString("team"));
			}
			
			
			l3.setItems(FXCollections.observableList(currentTeams));
			
			ps = conn.prepareStatement
				      ("select name from teams");
  	
			rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				availableTeams.add(rs.getString("name"));
			}
			
			availableTeams.removeAll(currentTeams);
			l2.setItems(FXCollections.observableList(availableTeams));

	    }

    	
    }
    
    @FXML
    private void removeFromCurrentTeams(ActionEvent event) throws SQLException{
       
    	System.out.println("from remove current");
    	ArrayList<String> currentTeams = new ArrayList<String>();
    	ArrayList<String> availableTeams = new ArrayList<String>();

    	
		PreparedStatement ps = conn.prepareStatement
			      ("delete from associatedMT where member=? and team=?");
		ps.setString(1, l1.getSelectionModel().getSelectedItem());
		ps.setString(2, l3.getSelectionModel().getSelectedItem());
	
		
	    if(ps.executeUpdate() == 1) {
	    	
	    	ps = conn.prepareStatement
				      ("select team from associatedMT where member=?");
			ps.setString(1, l1.getSelectionModel().getSelectedItem());
		
			ResultSet rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				currentTeams.add(rs.getString("team"));
			}
			
			
			l3.setItems(FXCollections.observableList(currentTeams));
			
			ps = conn.prepareStatement
				      ("select name from teams");
  	
			rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				availableTeams.add(rs.getString("name"));
			}
			
			availableTeams.removeAll(currentTeams);
			l2.setItems(FXCollections.observableList(availableTeams));

	    }

    	
    }
    
    @FXML 
    public void handleMouseClick(MouseEvent arg0) {
       
    	
    	ArrayList<String> currentTeams = new ArrayList<String>();
    	ArrayList<String> availableTeams = new ArrayList<String>();
    	
    	PreparedStatement ps;
		try {
			
			ps = conn.prepareStatement
				      ("select team from associatedMT where member=?");
			ps.setString(1, l1.getSelectionModel().getSelectedItem());
		
			ResultSet rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				currentTeams.add(rs.getString("team"));
			}
			
			
			l3.setItems(FXCollections.observableList(currentTeams));
			
			ps = conn.prepareStatement
				      ("select name from teams");
    	
			rs = ps.executeQuery();
		    
			while ( rs.next() ) {
				availableTeams.add(rs.getString("name"));
			}
			
			availableTeams.removeAll(currentTeams);
			l2.setItems(FXCollections.observableList(availableTeams));
			
    }
	catch(Exception e){
		e.printStackTrace();
	}
    


    
 
    }

}
