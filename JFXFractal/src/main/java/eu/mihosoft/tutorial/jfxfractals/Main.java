package eu.mihosoft.tutorial.jfxfractals;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("MainFXML.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        Parent root = (Parent) fxmlLoader.getRoot();

        Scene scene = new Scene(root, 1024, 768);

        primaryStage.setTitle("JFXFract-Lab (2015 by Michael Hoffer <info@michaelhoffer.de>)");
        primaryStage.setScene(scene);
        primaryStage.show();

        

    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

}
