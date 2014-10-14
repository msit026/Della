package application;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import persistence.DBManager;
import persistence.DataManager;
import control.Controller;
import della.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class actionController  implements ControlledScreen {
	
	ScreensController myController;
	String errorMsg = ""; // error message for popup if there is any error
	Controller theController;
	
	
	@FXML
	private ComboBox<String> sortingDir;	
	@FXML
	private ComboBox<String> firstSortingOrder;
	@FXML
	private ComboBox<String> secondSortingOrder;
	@FXML
	private ComboBox<String> inclusionFactor;	
	@FXML
	private ListView<String> actionItemList; 
	@FXML
	private TextField name;
	@FXML
	private TextArea description;
	@FXML
	private TextArea resolution;
	@FXML
	private ComboBox<String> status;
	@FXML
	private ComboBox<String> assignedTo;
	@FXML
	private ComboBox<String> team;
	@FXML 
	private DatePicker dueDate;
	@FXML
	private Label creationDate;
	@FXML
	private Button createButton;
	
	
	@FXML
	private void initialize() {
		
		//l1 = new ListView<String>();
		
    }
	
	public actionController() {
		System.out.println("In action Controller!!");
		theController = Controller.getInstance();
		System.out.println("Controller instance obtained!");
		sortingDir = new ComboBox<String>();
		firstSortingOrder = new ComboBox<String>();
		secondSortingOrder = new ComboBox<String>();
		inclusionFactor = new ComboBox<String>();		
		actionItemList = new ListView<String>();
		name = new TextField();
		description = new TextArea();
		resolution = new TextArea();
		status = new ComboBox<String>();
		assignedTo = new ComboBox<String>();
		team = new ComboBox<String>();
		createButton = new Button();
		//setData();
		System.out.println("In action Controller:: Data set");
	}
	
	public void setData() {	
		System.out.println("In setData!");
		sortingDir.setItems(FXCollections.observableList(Arrays.asList("Small to Large","Large to Small")));
		firstSortingOrder.setItems(FXCollections.observableList(Arrays.asList("None")));
		secondSortingOrder.setItems(FXCollections.observableList(Arrays.asList("None")));
		inclusionFactor.setItems(FXCollections.observableList(Arrays.asList("All Action Items")));
		List<String> values = theController.getActionItemManager().getActionItemNames();
		System.out.println("In setData: actionitems retrieved : " + values.size());
		actionItemList.setItems(FXCollections.observableList(values));	
		status.setItems(FXCollections.observableList(Arrays.asList("Open","Close")));
		status.setValue("Open");
		assignedTo.setItems(FXCollections.observableList(theController.getMemberList()));
		System.out.println("In setData: members retrieved : " + theController.getMemberList().get(0));
		team.setItems(FXCollections.observableList(theController.getTeams()));
//		assignedTo.setItems(FXCollections.observableList(DataStructure.getMemberList()));
//		team.setItems(FXCollections.observableList(DataStructure.getTeamList()));
//		description.setText("Mouse Pointer issue");
//		resolution.setText("Done");
		
		System.out.println("Out of set data!!");
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
		setData();
		
	}
	
	@FXML
    private void goToScreen1(ActionEvent event){
       myController.setScreen(Main.screen1ID);
    }
    
    @FXML
    private void goToScreen3(ActionEvent event){
       myController.setScreen(Main.screen3ID);
    }
    
    @FXML
    private void goToScreen4(ActionEvent event){
       myController.setScreen(Main.screen4ID);
    }
    
    @FXML
    private void create(ActionEvent event)
    {
    	try
    	{
    		if(theController != null && theController.getActionItemManager().createActionItem(name.getText(), description.getText(), resolution.getText(), status.getValue(), dueDate.getValue().toString(), assignedTo.getValue(), team.getValue()) == null)
    		{
    			final Stage dialog = createPopup("Connection to the database cannot be established.\n "
    					+ "Please check your connection and try again.");
                dialog.show();
    		}
    		else
    			System.out.println("Noooooooooooooooooo");
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error in create of action controller");
    		e.printStackTrace();
    		final Stage dialog = createPopup(e.getMessage());
            dialog.show();
    	}
    }
    
//    @FXML
//    private void create(ActionEvent event)
//    {
//    	// reset the values of error message and flag value to default values
//    	errorMsg = "";
//    	
//    	// create an action item object with the entered details
//    	ActionItem item = new ActionItem();
//    	item.setActionItemName(name.getText());
//    	if(description.getText() != null)
//    		item.setDescription(description.getText());
//    	if(resolution.getText() != null)
//    		item.setResolution(resolution.getText());
//    	item.setStatus(status.getValue());
//    	item.setCreatedDate(new Date());
//    	item.setAssignedMember(assignedTo.getValue());
//    	item.setAssignedTeam(team.getValue());
//    	
//    	// check of the name of the action item is entered
//    	if(name.getText().equals("") || name.getText() == null)
//    	{
//    		errorMsg += "-> Please enter the name of ActionItem!\n\n"; // if name is not entered, write the error message
//    	}
//    	
//    	// validate due date
//    	if(dueDate.getValue() != null)
//    	{
//    		Date due = convertToDate(dueDate.getValue().toString());
//    		Date created = convertToDate(creationDate.getText());
//    		
//    		//check if the due date is after created date
//    		if(due.before(created))
//    		{
//    			errorMsg += "-> Enter valid due date. Due date should not be before creation date!"; // if due date is before creation date, set up error message
//    		}
//    		else
//    		{
//    			item.setDueDate(due);
//    		}
//    	}
//    	else
//    		item.setDueDate(new Date());
//    	
//    	// If there are no errors in the title of the actionitem and the due date, set the flag;
//    	if(!errorMsg.equals(""))
//    	{
//    		final Stage dialog = createPopup(errorMsg);
//            dialog.show();
//	    	return;
//    	}
//    		
//    	DBManager.writeData(item);
//    }
    
    @FXML
    private void update(ActionEvent event)
    {
//    	ActionItem item = new ActionItem();
//    	item.setActionItemName(name.getText());
//    	if(description.getText() != null)
//    		item.setDescription(description.getText());
//    	if(resolution.getText() != null)
//    		item.setResolution(resolution.getText());
//    	item.setStatus(status.getValue());
//    	item.setCreatedDate(convertToDate(creationDate.getText()));
//    	//item.setCreatedDate(new Date());
//    	if(dueDate.getValue() != null)
//    	{
//    		System.out.println("------------>>>DUE DATE: " + dueDate.getValue().toString());
//    		Date due = convertToDate(dueDate.getValue().toString());
//    		Date created = convertToDate(creationDate.getText());
//    		if(due.before(created))
//    		{
//    			System.out.println("Due date before created");
//    			createButton.setOnAction(
//    	    	        new EventHandler<ActionEvent>() {
//    	    	            @Override
//    	    	            public void handle(ActionEvent event) {
//    	    	                final Stage dialog = createPopup("Enter valid due date. Due date should not be before creation date!");
//    	    	                dialog.show();
//    	    	            }
//    	    	         });
//    		}
//    		else
//    		{
//    			System.out.println("Due date after created");
//    			item.setDueDate(due);
//    		}
//    	}
//    	else
//    	{
//    		System.out.println("Due date is null");
//    		item.setDueDate(new Date());
//    	}
//    	
//    	item.setAssignedMember(assignedTo.getValue());
//    	item.setAssignedTeam(team.getValue());
//    	DBManager.writeData(item);
    }
    
    private Stage createPopup(String string) {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(string +"\n\n"));
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        return dialog;
	}

//	private Date convertToDate(String dateStr)
//    {
//    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
//     
//    	try {
//     
//    		Date date = formatter.parse(dateStr);
//    		return date;
//     
//    	} catch (ParseException e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    	
//    }

}
