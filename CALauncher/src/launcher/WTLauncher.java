package launcher;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tasks.ProgressTask;

/**
 * The Launcher for the CashAssets Application. This will check for an Update for the 
 * Application and, if one is found, asks the User to do an Update.
 * @author Haeldeus
 * @version 1.0
 */
public class WTLauncher extends Application {

  private static final double version = 0.5;
  
  private Label updatesLabel;
  
  private BorderPane bp;
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    writeVersion();
    bp = new BorderPane();
    /*
     * Sets the Size of the Scene, it's restrictions and the Stylesheet. Afterwards, it displays 
     * the primaryStage to the User.
     */
    Scene scene = new Scene(bp, 600, 250);
//    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(270);
    primaryStage.setMinWidth(620);
    primaryStage.show();
  }
  
  private void writeVersion() {
    try {
      String path = Paths.get("").toAbsolutePath().toString();
      if (path.substring(path.length() - 5).contains("app")) {
        path = path.concat("/Version.txt");
      } else {
        path = path.concat("/app/Version.txt");
      }
      System.out.println(path);
      FileWriter fw = new FileWriter(path);
      fw.write("" + version);
      fw.close();
    } catch (IOException e) {
      showUpdateFailed("Fehler beim Schreiben der Version! Bitte dem Entwickler melden.");
    }
  }
  
  public void showUpdateFailed(String text) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        bp.setBottom(null);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        updatesLabel.setText(text);
        grid.add(updatesLabel, 0, 0, 2, 1);
        Button btRetry = new Button("Wiederholen" + System.lineSeparator() + "(Empfohlen)");
        btRetry.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent arg0) {
            startCheckingTask();
          }          
        });
        grid.add(btRetry, 0, 1);
        
        Button btStart = new Button("Ohne Update starten" + System.lineSeparator() 
            + "(Nicht Empfohlen)");
        btStart.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent ae) {
            startWithoutCheck();
          }
        });
        grid.add(btStart, 1, 1);
        grid.setAlignment(Pos.CENTER);
        bp.setCenter(grid);
      }      
    });
  }  
  
  private void startCheckingTask() {
    ProgressIndicator pi = new ProgressIndicator();
    updatesLabel = new Label();
    bp.setBottom(this.updatesLabel);
    bp.setCenter(pi);
    
    ProgressTask pt = new ProgressTask(this.updatesLabel, this);
    pi.progressProperty().bind(pt.progressProperty());
    new Thread(pt).start();
  }
  
  private void startWithoutCheck() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
         showUpdateFailed("Noch nicht implementiert!");
         //TODO: HANDLE THIS!
      }
    });
  }
  
  public static void main(String[] args) {
    WTLauncher.launch(args);
  }
}