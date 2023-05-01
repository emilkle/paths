package no.ntnu.idatg2001.paths.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a Passage that contains a title, content, and a list of links.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 * @version 2023.02.02
 */
public class Passage {
  private final String title;
  private final String content;
  private List<Link> links;

  /**
   * A constructor that initializes the declared fields for title, content and links.
   *
   * @param title the title of the passage, also works as a unique identifier.
   * @param content the content of the passage, typically a part of the story.
   */
  public Passage(String title, String content) {
    this.title = title;
    this.content = content;
    this.links = new ArrayList<>();
  }

  /**
   * This function returns the title of the book
   *
   * @return The title of the book.
   */
  public String getTitle() {
    return title;
  }

  /**
   * This function returns the content of the message.
   *
   * @return The content of the message.
   */
  public String getContent() {
    return content;
  }

  /**
   * Adds a link to an arrayList and return true.
   *
   * @param link The link to be added to the list.
   * @return A boolean value.
   */
  public boolean addLink(Link link) {
    boolean success = false;
    try {
      links.add(link);
      success = true;
    } catch (Exception e) {
      // TODO: replace sout
      System.out.println("Error: " + e.getMessage());
    }
    return success;
  }

  /**
   * This function returns a list of links.
   *
   * @return A list of links.
   */
  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
    /**
     * if (links != null && links.size() >= 2) { this.links = new ArrayList<>(links.subList(0, 2));
     * } else { this.links = links; }
     */
  }

  /**
   * Returns true if the list of links is not empty.
   *
   * @return A boolean value.
   */
  public boolean hasLinks() {
    return !links.isEmpty();
  }

  @Override
  public String toString() {
    return "Passage{"
        + "title='"
        + title
        + '\''
        + ", content='"
        + content
        + '}';
  }

  // TODO: implement equals and hashCode
  @Override
  public boolean equals(Object object) {
    return super.equals(object);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
