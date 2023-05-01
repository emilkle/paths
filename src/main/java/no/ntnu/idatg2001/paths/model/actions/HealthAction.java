package no.ntnu.idatg2001.paths.model.actions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import no.ntnu.idatg2001.paths.model.units.Player;

/**
 * The HealthAction class represents an action that gives the player health.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 */
@Entity
public class HealthAction implements Action {
  @Id @GeneratedValue private Long id;
  private int health;
  private boolean isPositive;

  /**
   * Constructor for the HealthAction class.
   *
   * @param health the amount of health to give the player
   */
  public HealthAction(int health, boolean isPositive) {
    this.health = health;
    this.isPositive = isPositive;
  }

  public HealthAction() {}

  /**
   * Returns whether the action is positive or not.
   *
   * @return whether the action is positive or not
   */
  public boolean getIsPositive() {
    return isPositive;
  }

  /**
   * Returns the amount of health the action gives.
   *
   * @return the amount of health the action gives
   */
  public int getHealth() {
    return health;
  }

  /**
   * Adds health to the action.
   *
   * @param health the amount of health to add
   */
  public void addHealth(int health) {
    this.health += health;
  }

  /**
   * Executes the action.
   *
   * @param player the player who is performing the action
   */
  @Override
  public void execute(Player player) {
    player.addHealth(health);
  }
}
