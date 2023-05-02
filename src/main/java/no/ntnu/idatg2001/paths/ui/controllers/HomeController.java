package no.ntnu.idatg2001.paths.ui.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.ntnu.idatg2001.paths.model.Game;
import no.ntnu.idatg2001.paths.model.Link;
import no.ntnu.idatg2001.paths.model.Story;
import no.ntnu.idatg2001.paths.model.database.GameDAO;
import no.ntnu.idatg2001.paths.model.database.PlayerDAO;
import no.ntnu.idatg2001.paths.model.database.StoryDAO;
import no.ntnu.idatg2001.paths.model.units.Player;
import no.ntnu.idatg2001.paths.ui.alerts.ConfirmationAlert;
import no.ntnu.idatg2001.paths.ui.alerts.ExceptionAlert;
import no.ntnu.idatg2001.paths.ui.alerts.WarningAlert;
import no.ntnu.idatg2001.paths.ui.dialogs.NewPlayerDialog;
import no.ntnu.idatg2001.paths.ui.dialogs.NewStoryDialog;
import no.ntnu.idatg2001.paths.ui.handlers.LanguageHandler;
import no.ntnu.idatg2001.paths.ui.views.EditPlayerView;
import no.ntnu.idatg2001.paths.ui.views.EditStoryView;
import no.ntnu.idatg2001.paths.ui.views.StoryView;

public class HomeController {
  // TEXTS
  private final Text pathsGameText;
  private final Text storiesText;
  private final Text playersText;
  private final Text deadLinksText;
  private final Text ongoingGamesText;
  // BUTTONS
  private final Button editStoryButton;
  private final Button newStoryButton;
  private final Button editPlayerButton;
  private final Button newPlayerButton;
  private final Button deleteLinkButton;
  private final Button continueButton;
  private final Button deleteButton;
  // TABLE VIEWS

  private final TableView<Story> storiesTableView;
  private final TableView<Player> playersTableView;
  private final TableView<Link> deadLinksTableView;
  private final TableView<Game> ongoingGamesTableView;
  // TABLE COLUMNS
  private final TableColumn<Game, String> ongoingGamesTableColumn;
  private final TableColumn<Story, String> storiesTableColumn;
  private final TableColumn<Player, String> playersTableColumn;
  private final TableColumn<Link, String> deadLinksTableColumn;

  private final Stage primaryStage;
  private ResourceBundle resources;

  public HomeController(
      Text pathsGameText,
      Text storiesText,
      Text playersText,
      Text deadLinksText,
      Text ongoingGamesText,
      Button editStoryButton,
      Button newStoryButton,
      Button editPlayerButton,
      Button newPlayerButton,
      Button deleteLinkButton,
      Button continueButton,
      Button deleteButton,
      TableView<Story> storiesTableView,
      TableView<Player> playersTableView,
      TableView<Link> deadLinksTableView,
      TableView<Game> ongoingGamesTableView,
      TableColumn<Story, String> storiesTableColumn,
      TableColumn<Player, String> playersTableColumn,
      TableColumn<Link, String> deadLinksTableColumn,
      TableColumn<Game, String> ongoingGamesTableColumn,
      Stage primaryStage) {
    // TEXTS
    this.pathsGameText = pathsGameText;
    this.storiesText = storiesText;
    this.playersText = playersText;
    this.deadLinksText = deadLinksText;
    this.ongoingGamesText = ongoingGamesText;

    // BUTTONS
    this.editStoryButton = editStoryButton;
    this.newStoryButton = newStoryButton;
    this.editPlayerButton = editPlayerButton;
    this.newPlayerButton = newPlayerButton;
    this.deleteLinkButton = deleteLinkButton;
    this.continueButton = continueButton;
    this.deleteButton = deleteButton;

    // TABLE VIEWS
    this.storiesTableView = storiesTableView;
    this.playersTableView = playersTableView;
    this.deadLinksTableView = deadLinksTableView;
    this.ongoingGamesTableView = ongoingGamesTableView;

    // TABLE COLUMNS
    this.ongoingGamesTableColumn = ongoingGamesTableColumn;
    this.storiesTableColumn = storiesTableColumn;
    this.playersTableColumn = playersTableColumn;
    this.deadLinksTableColumn = deadLinksTableColumn;
    this.primaryStage = primaryStage;

    // Observes when the language is changed, then calls updateLanguage()
    LanguageHandler.getObservableIntegerCounter()
        .addListener((obs, oldValue, newValue) -> updateLanguage());

    // gets the correct resource bundle, depending on the current language in database
    resources =
        ResourceBundle.getBundle(
            "home", Locale.forLanguageTag(LanguageHandler.getCurrentLanguage().getLocalName()));
  }

  public void configureButtons() {
    editStoryButton.setOnAction(
        event -> {
          try {
            Story story = storiesTableView.getSelectionModel().getSelectedItem();
            if (story == null) {
              throw new NullPointerException("No story selected");
            }
            EditStoryView editStoryView = new EditStoryView();
            editStoryView.start(primaryStage, story);
          } catch (NullPointerException e) {
            ExceptionAlert exceptionAlert = new ExceptionAlert(e);
            exceptionAlert.showAndWait();
          }
        });

    newStoryButton.setOnAction(
        event -> {
          NewStoryDialog newStoryDialog = new NewStoryDialog();
          newStoryDialog.initOwner(primaryStage);

          Optional<Story> result = newStoryDialog.showAndWait();
          result.ifPresent(
              story -> {
                // @TODO: FIX THIS
                // ADD STORY TO W/E
                // UPDATE THIS W/E IN DB
                // UPDATE VIEW
              });
        });

    editPlayerButton.setOnAction(
        event -> {
          try {
            Player player = playersTableView.getSelectionModel().getSelectedItem();
            if (player == null) {
              throw new NullPointerException("No player selected");
            }
            EditPlayerView editPlayerView = new EditPlayerView();
            editPlayerView.start(primaryStage, player);
          } catch (NullPointerException e) {
            ExceptionAlert exceptionAlert = new ExceptionAlert(e);
            exceptionAlert.showAndWait();
          }
        });

    newPlayerButton.setOnAction(
        event -> {
          System.out.println("players in db: " + PlayerDAO.getInstance().getAll());
          NewPlayerDialog newPlayerDialog = new NewPlayerDialog();
          newPlayerDialog.initOwner(primaryStage);

          Optional<Player> result = newPlayerDialog.showAndWait();
          result.ifPresent(
              player -> {
                // ADD PLAYER TO W/E
                // UPDATE THIS W/E IN DB
                // UPDATE VIEW
                PlayerDAO.getInstance().add(player);
                updatePlayerTable();
              });
        });

    deleteLinkButton.setOnAction(
        event -> {
          if (deadLinksTableView
              .getSelectionModel()
              .isSelected(deadLinksTableView.getSelectionModel().getSelectedIndex())) {
            Link deadLink =
                deadLinksTableView
                    .getItems()
                    .get(deadLinksTableView.getSelectionModel().getSelectedIndex());

            ConfirmationAlert confirmationAlert =
                new ConfirmationAlert(
                    "Delete link", "Are you sure you want to delete this link?\n" + deadLink);

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
              // Remove the link from the story
              // @TODO Fix this
              deadLinksTableView
                  .getItems()
                  .removeAll(deadLinksTableView.getSelectionModel().getSelectedItems());
              // Remove the link from the database
              // Or update the story in the database
            } else {
              confirmationAlert.close();
            }
          } else {
            WarningAlert warningAlert = new WarningAlert("Please Select a item to Delete");
            warningAlert.showAndWait();
          }
        });

    continueButton.setOnAction(
        event -> {
          // FIND SELECTED PLAYER
          // FIND SELECTED STORY
          // START GAME WITH SELECTED PLAYER AND STORY

          // Player selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
          // Story selectedStory = storiesTableView.getSelectionModel().getSelectedItem();
          // Game game = new Game(selectedPlayer, selectedStory, null);
          // @TODO Fix this, add to DB and set active

          StoryView storyView = new StoryView();
          storyView.start(primaryStage);
        });

    deleteButton.setOnAction(
        event -> {
          if (ongoingGamesTableView.getSelectionModel().isEmpty()) {
            WarningAlert warningAlert =
                new WarningAlert("Delete Game", "You must select a game to delete");
            warningAlert.showAndWait();
          } else {
            ConfirmationAlert confirmationAlert =
                new ConfirmationAlert("Delete Game", "Are you sure you want to delete this game?");
            confirmationAlert.showAndWait();
          }
        });
  }

  public void updateLanguage() {
    resources =
        ResourceBundle.getBundle(
            "home", Locale.forLanguageTag(LanguageHandler.getCurrentLanguage().getLocalName()));
    pathsGameText.setText(resources.getString("title"));
    storiesText.setText(resources.getString("storiesText"));
    playersText.setText(resources.getString("playersText"));
    deadLinksText.setText(resources.getString("deadLinksText"));
    ongoingGamesText.setText(resources.getString("ongoingGamesText"));
    editStoryButton.setText(resources.getString("editStoryButton"));
    newStoryButton.setText(resources.getString("newStoryButton"));
    editPlayerButton.setText(resources.getString("editPlayerButton"));
    newPlayerButton.setText(resources.getString("newPlayerButton"));
    deleteLinkButton.setText(resources.getString("deleteLinkButton"));
    continueButton.setText(resources.getString("continueButton"));
    deleteButton.setText(resources.getString("deleteButton"));
    ongoingGamesTableColumn.setText(resources.getString("ongoingGamesTableColumn"));
    storiesTableColumn.setText(resources.getString("storiesTableColumn"));
    playersTableColumn.setText(resources.getString("playersTableColumn"));
    deadLinksTableColumn.setText(resources.getString("deadLinksTableColumn"));
  }

  public void updateAllTables() {
    updatePlayerTable();
    updateStoryTable();
    updateGameTable();
    updateDeadLinkTable();
  }

  public void updatePlayerTable() {
    List<Player> playerList = PlayerDAO.getInstance().getAll();
    // turn playerList into observable list
    ObservableList<Player> observablePlayerList = FXCollections.observableArrayList(playerList);
    playersTableView.setItems(observablePlayerList);
  }

  public void updateStoryTable() {
    List<Story> storyList = StoryDAO.getInstance().getAll();
    // turn storyList into observable list
    ObservableList<Story> observableStoryList = FXCollections.observableArrayList(storyList);
    storiesTableView.setItems(observableStoryList);
  }

  public void updateGameTable() {
    List<Game> gameList = GameDAO.getInstance().getAll();
    // turn gameList into observable list
    ObservableList<Game> observableGameList = FXCollections.observableArrayList(gameList);
    ongoingGamesTableView.setItems(observableGameList);
  }

  public void updateDeadLinkTable() {
    // do something
  }

  public void configureTableColumns() {
    ongoingGamesTableColumn.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
    ongoingGamesTableColumn.setPrefWidth(250);

    storiesTableColumn.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle()));
    storiesTableColumn.setPrefWidth(250);

    playersTableColumn.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
    playersTableColumn.setPrefWidth(250);

    deadLinksTableColumn.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().toString()));
    deadLinksTableColumn.setPrefWidth(250);
  }
}
