package eu.mihosoft.tutorial.javafx01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        webView.getEngine().load("http://github.com");

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 1024, 768);

        primaryStage.setTitle("Hello Web!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
