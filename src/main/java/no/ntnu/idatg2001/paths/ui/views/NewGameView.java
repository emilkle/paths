package no.ntnu.idatg2001.paths.ui.views;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import no.ntnu.idatg2001.paths.ui.standardObjects.StandardMenuBar;

public class NewGameView {

  private final Stage primaryStage;

  public NewGameView(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("New Game");

    // Create a borderpane and a standard menubar
    BorderPane root = new BorderPane();
    StandardMenuBar menuBar = new StandardMenuBar(primaryStage);
    root.setTop(menuBar);
    AnchorPane rootAnchorPane = new AnchorPane();

    Scene scene = new Scene(root, 800, 800);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}