package no.ntnu.idatg2001.paths.ui.views;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.ntnu.idatg2001.paths.model.Game;
import no.ntnu.idatg2001.paths.model.Link;
import no.ntnu.idatg2001.paths.model.Story;
import no.ntnu.idatg2001.paths.model.database.GameDAO;
import no.ntnu.idatg2001.paths.model.database.PlayerDAO;
import no.ntnu.idatg2001.paths.model.database.StoryDAO;
import no.ntnu.idatg2001.paths.model.units.Player;
import no.ntnu.idatg2001.paths.ui.controllers.HomeController;
import no.ntnu.idatg2001.paths.ui.controllers.SettingsController;
import no.ntnu.idatg2001.paths.ui.handlers.MusicHandler;
import no.ntnu.idatg2001.paths.ui.standardObjects.StandardMenuBar;

public class HomeView extends Application {
  private HomeController homeController;
  private SettingsController settingsController;
  private ResourceBundle resources;
  private Button startNewGameButton;
  private Text pathsGameText;
  private Text storiesText;
  private Text deadLinksText;

  public static void mainApp(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Home");
    primaryStage.setOnCloseRequest(
        event -> {
          event.consume();
          GameDAO.getInstance().close();
          StoryDAO.getInstance().close();
          PlayerDAO.getInstance().close();
          Platform.exit();
        });

    BorderPane root = new BorderPane();
    root.setTop(new StandardMenuBar(primaryStage));

    VBox mainVBox = new VBox(50);

    mainVBox.setAlignment(Pos.TOP_CENTER);
    AnchorPane.setTopAnchor(mainVBox, 0.0);
    AnchorPane.setBottomAnchor(mainVBox, 0.0);
    AnchorPane.setLeftAnchor(mainVBox, 0.0);
    AnchorPane.setRightAnchor(mainVBox, 0.0);

    HBox storiesAndPlayersHBox = new HBox();
    storiesAndPlayersHBox.setAlignment(Pos.CENTER);
    HBox startNewGameHBox = new HBox();
    startNewGameHBox.setAlignment(Pos.CENTER);
    HBox deadLinksAndOngoingGamesHBox = new HBox();
    deadLinksAndOngoingGamesHBox.setAlignment(Pos.CENTER);

    // VBOXES
    VBox storiesVBox = new VBox();
    VBox playersVBox = new VBox();
    VBox deadLinksVBox = new VBox();
    VBox ongoingGamesVBox = new VBox();

    // TABLEVIEWS
    TableView<Story> storiesTableView = new TableView<>();
    TableColumn<Story, String> storiesTableColumn = new TableColumn<>();
    storiesTableView.getColumns().add(storiesTableColumn);

    TableView<Player> playersTableView = new TableView<>();
    TableColumn<Player, String> playersTableColumn = new TableColumn<>();
    playersTableView.getColumns().add(playersTableColumn);

    TableView<Link> deadLinksTableView = new TableView<>();
    TableColumn<Link, String> deadLinksTableColumn = new TableColumn<>();
    deadLinksTableView.getColumns().add(deadLinksTableColumn);

    TableView<Game> ongoingGamesTableView = new TableView<>();
    TableColumn<Game, String> ongoingGamesTableColumn = new TableColumn<>();
    ongoingGamesTableView.getColumns().add(ongoingGamesTableColumn);

    // TEXTS
    Text pathsGameText = new Text();
    Text storiesText = new Text();
    Text playersText = new Text();
    Text deadLinksText = new Text();
    Text ongoingGamesText = new Text();

    // BUTTONS
    Button editStoryButton = new Button();
    Button newStoryButton = new Button();
    Button deleteStoryButton = new Button();
    HBox storiesButtonsHBox = new HBox(editStoryButton, newStoryButton, deleteStoryButton);

    Button editPlayerButton = new Button();
    Button newPlayerButton = new Button();
    Button deletePlayerButton = new Button();
    HBox playersButtonsHBox = new HBox(editPlayerButton, newPlayerButton, deletePlayerButton);

    Button deleteLinkButton = new Button();
    Button updateDeadLinksButton = new Button();
    HBox deadLinksButtonsHBox = new HBox(deleteLinkButton, updateDeadLinksButton);

    Button continueButton = new Button();
    Button deleteButton = new Button();
    HBox ongoingGamesButtonsHBox = new HBox(continueButton, deleteButton);

    // ADDING TO VBOXES
    storiesVBox.getChildren().addAll(storiesText, storiesTableView, storiesButtonsHBox);
    playersVBox.getChildren().addAll(playersText, playersTableView, playersButtonsHBox);
    deadLinksVBox.getChildren().addAll(deadLinksText, deadLinksTableView, deadLinksButtonsHBox);
    ongoingGamesVBox
        .getChildren()
        .addAll(ongoingGamesText, ongoingGamesTableView, ongoingGamesButtonsHBox);

    // ADDING TO HBOXES
    storiesAndPlayersHBox.getChildren().addAll(storiesVBox, playersVBox);
    deadLinksAndOngoingGamesHBox.getChildren().addAll(deadLinksVBox, ongoingGamesVBox);

    // START NEW GAME BUTTON
    startNewGameButton = new Button();
    startNewGameHBox.getChildren().add(startNewGameButton);

    // ADD TO MAINVBOX
    mainVBox
        .getChildren()
        .addAll(
            pathsGameText, storiesAndPlayersHBox, startNewGameHBox, deadLinksAndOngoingGamesHBox);

    root.setCenter(mainVBox);

    // CONTROLLER
    homeController =
        new HomeController(
            pathsGameText,
            storiesText,
            playersText,
            deadLinksText,
            ongoingGamesText,
            editStoryButton,
            newStoryButton,
            deleteStoryButton,
            editPlayerButton,
            newPlayerButton,
            deletePlayerButton,
            deleteLinkButton,
            updateDeadLinksButton,
            continueButton,
            deleteButton,
            startNewGameButton,
            storiesTableView,
            playersTableView,
            deadLinksTableView,
            ongoingGamesTableView,
            storiesTableColumn,
            playersTableColumn,
            deadLinksTableColumn,
            ongoingGamesTableColumn,
            primaryStage);

    homeController.configureButtons();
    homeController.configureTableColumns();

    homeController.updateLanguage();
    homeController.updateAllTables();

    Scene scene = new Scene(root, 800, 800);
    scene.getStylesheets().add("cssfiles/home.css");
    primaryStage.setScene(scene);
    primaryStage.show();
    MusicHandler.playMusic("Alabama.mp3");
  }
}
