package sk.upjs.nosql.redischat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainSceneController controller = new MainSceneController();
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        fXMLLoader.setController(controller);

        Scene scene = new Scene(fXMLLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Redis chat");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
