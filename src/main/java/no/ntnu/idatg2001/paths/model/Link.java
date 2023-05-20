package no.ntnu.idatg2001.paths.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idatg2001.paths.model.actions.Action;

/**
 * A class that makes it possible to move from one passage to another, by using links between the
 * different parts of the story's plot.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 * @version 2023.02.02
 */
@Entity
public class Link {
  @OneToMany(mappedBy = "link", cascade = CascadeType.ALL)
  private final List<Action> actions = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String text;
  private String reference;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "story_id")
  private Story story;

  public Story getStory() {
    return story;
  }

  public void setStory(Story story) {
    this.story = story;
  }

  /**
   * A constructor that initializes the declared fields for text, reference and actions.
   *
   * @param text A string value that indicates the choices the player can make at a certain point in
   *     the story.
   * @param reference A string value that represents a unique passage identifier.
   */
  public Link(String text, String reference) {
    this.text = text;
    this.reference = reference;
  }

  public Link() {}



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * An accessor method that returns the text field's String value.
   *
   * @return the player's choices.
   */
  public String getText() {
    return text;
  }

  /**
   * A mutator method that sets the text field's string value.
   *
   * @param text Represents the player's choices.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * An accessor method that returns the reference field's string value.
   *
   * @return the string identifier for the chosen passage.
   */
  public String getReference() {
    return reference;
  }

  /**
   * A mutator method that sets the reference field's string value.
   *
   * @param reference Represents the string identifier for the chosen passage.
   */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /**
   * A method that adds a action to the actions list.
   *
   * @param action Represents the action that gets added to the list of actions.
   */
  public void addAction(Action action) {
    action.setLink(this);
    actions.add(action);
  }

  /**
   * A method that returns the actions the makes it possible to influence the player's attributes.
   *
   * @return the actions in the actions list.
   */
  public List<Action> getActions() {
    return actions;
  }

  @Override
  public final int hashCode() {
    int result = 17;
    // only using reference, because a passage can have multiple links
    if (reference != null) {
      result = reference.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "Link{"
        + "text='"
        + text
        + '\''
        + ", reference='"
        + reference
        + '\''
        + ", actions="
        + actions
        + '}';
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) return true;
    if (!(object instanceof Link other)) return false;
    return (this.reference == null && other.reference == null)
        || (this.reference != null && this.reference.equals(other.reference));
  }
}
