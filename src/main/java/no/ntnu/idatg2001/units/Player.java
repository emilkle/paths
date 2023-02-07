package no.ntnu.idatg2001.units;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a player with different actions that can be used in a story.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 * @version 2023.02.03
 */
public class Player extends Unit {

  public Player(
      String name, int score, int gold, int health, int mana, int energy, Attributes attributes) {
    this.name = name;
    this. =
  }

  public void dialog() {

  }

  /**
   *
   * @param health
   */
  public boolean addHealth(int health) {
    if (health > 0) {
      super.setHealth(super.getHealth() + health);
      return true;
    } else {
      return false;
    }
  }

  /**
   *
   * @param gold
   */
  public boolean addGold(int gold) {
    if (gold >= 0) {
      super.setGold(super.getGold() + gold);
      return true;
    } else {
      return false;
    }

  }

}
