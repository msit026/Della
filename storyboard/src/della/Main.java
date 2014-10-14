package della;
	
import control.Controller;
import persistence.DBManager;
import persistence.DataManager;
import application.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static Stage primaryStage;
    private FlowPane rootLayout;

	public static String screen0ID = "login";
    public static String screen0File = "login.fxml";
	
	public static String screen1ID = "main";
    public static String screen1File = "console.fxml";
    
    public static String screen2ID = "actionitem";
    public static String screen2File = "actionitem.fxml";
    
    public static String screen3ID = "members";
    public static String screen3File = "Members.fxml";
    
    public static String screen4ID = "teams";
    public static String screen4File = "teams.fxml";
	
    public static ScreensController mainContainer;
    
	public Main() {
		
		System.out.println("In main Constructor!!");
		mainContainer = new ScreensController();
	}
	

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			/*try {
				
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(Main.class.getResource("console.fxml"));	        
		    	rootLayout = (FlowPane) loader.load();						
		        Scene scene = new Scene(rootLayout);
		        primaryStage.setScene(scene);
		        primaryStage.show();
		        
		        
		        
				
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			 
	       
	       System.out.println("1");
	        mainContainer.loadScreen(Main.screen0ID, Main.screen0File);
	        System.out.println("2");
	        
	        
	        System.out.println("Screens Loaded!");
	        
	        mainContainer.setScreen(Main.screen0ID);
	        
	        Group root = new Group();
	        root.getChildren().addAll(mainContainer);
	        Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
	        this.primaryStage = primaryStage;
	        primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
