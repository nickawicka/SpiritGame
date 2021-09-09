package base;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	
	//private static int WIDTH = 800;
	//private static int HEIGHT = 600;
	//final Color screen_color = Color.rgb(12, 20, 69);
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        Scene scene = new Scene(root);

        KeyPolling.getInstance().pollScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Spirit Game");

        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/EdenCodingIcon.png")));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
	/*
	public void initialize() {
		
	}
	
	public void update() {
		
	}
	
	void render(GraphicsContext gc) {
		gc.setFill(screen_color);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	@Override
	public void start(Stage primary_stage) throws Exception {
		initialize();
		
		Group root = new Group();  
		Scene scene = new Scene(root, 600, 600);
		Handlers handler = new Handlers();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		root.getChildren().add(canvas);
		
		handler.setHandlers(scene); // more changes more
		
		primary_stage.setTitle("Blank");
		primary_stage.setScene(scene);
		primary_stage.show();
	}	
	*/
}
