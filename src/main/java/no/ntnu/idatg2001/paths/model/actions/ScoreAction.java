package no.ntnu.idatg2001.paths.model.actions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import no.ntnu.idatg2001.paths.model.units.Player;

/**
 * The ScoreAction class represents an action that gives the player score.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 */
@Entity
public class ScoreAction implements Action {
  private boolean isPositive;
  @Id @GeneratedValue private Long id;
  private int points;

  /**
   * Constructor for the ScoreAction class.
   *
   * @param points the amount of points to give the player
   */
  public ScoreAction(int points, boolean isPositive) {
    this.points = points;
    this.isPositive = isPositive;
  }

  public ScoreAction() {}

  /**
   * Returns whether the action is positive or not.
   *
   * @return whether the action is positive or not
   */
  public boolean getIsPositive() {
    return isPositive;
  }

  /**
   * Returns the amount of points the action gives.
   *
   * @return the amount of points the action gives
   */
  public int getPoints() {
    return points;
  }

  /**
   * Adds points to the action.
   *
   * @param points the amount of points to add
   */
  public void addPoints(int points) {
    this.points += points;
  }

  /**
   * Executes the action.
   *
   * @param player the player who is performing the action
   */
  @Override
  public void execute(Player player) {
    player.setScore(player.getScore() + points);
  }
}
