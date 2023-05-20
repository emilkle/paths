package no.ntnu.idatg2001.paths.model;

import jakarta.persistence.*;
import java.io.*;
import java.util.*;

/**
 * The Story class is a container for the story. It contains the title of the story, a map of all
 * the passages in the story, and the opening passage.
 *
 * @author Erik Bjørnsen and Emil Klevgård-Slåttsveen
 * @version 2023.02.02
 */
@Entity
@Table(name = "story")
public class Story {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "story_passage_mapping",
      joinColumns = {@JoinColumn(name = "story_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "passage_id", referencedColumnName = "id")})
  @MapKeyJoinColumn(name = "link_id")
  private Map<Link, Passage> passages;

  @OneToOne(cascade = {CascadeType.ALL})
  @JoinColumn
  private Passage openingPassage;

  private String title;

  public Story(String title, Passage openingPassage) {
    this.passages = new HashMap<>();
    this.title = title;
    setOpeningPassage(openingPassage);
  }

  public Story(String title) {
    this.title = title;
    this.passages = new HashMap<>();
    this.openingPassage = null;
  }

  public Story() {
    this.passages = new HashMap<>();
    this.openingPassage = null;
  }

  public Map<Link, Passage> getPassagesHashMap() {
    return passages;
  }

  public void setPassagesHashMap(Map<Link, Passage> passages) {
    this.passages = passages;
  }

  /**
   * This function returns the title of the game
   *
   * @return The title of the game.
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String text) {
    this.title = text;
  }

  /**
   * This function returns the opening passage of the game.
   *
   * @return The opening passage of the game.
   */
  public Passage getOpeningPassage() {
    return openingPassage;
  }

  public void setOpeningPassage(Passage openingPassage) {
    if (this.openingPassage != null) {
      passages.remove(new Link(this.openingPassage.getTitle(), this.openingPassage.getTitle()));
    }
    this.openingPassage = openingPassage;
    addPassage(openingPassage);
  }

  /**
   * This function adds a passage to the story
   *
   * @param passage The passage that gets added to the game.
   */

  // Metoden addPassage skal legge til en passasje i passages. Da trenger vi også et Link-objekt.
  // Dette løser vi ved å opprette en ny link basert på passasjens tittel. Tittelen kan fungere både
  // som tekst og referanse.

  // • links: linker som kobler denne passasjen mot andre passasjer. En passasje med to eller
  // flere linker gjør historien ikke-lineær.

  // Diagrammet viser at Link-klassen har tre attributter:
  // • text: en beskrivende tekstsom indikerer et valg eller en handling i en historie. Teksten
  // er den delen av linken som vil være synlig for spilleren.
  // • reference: en streng som entydig identifiserer en passasje (en del av en historie). I
  // praksis vil dette være tittelen til passasjen man ønsker å referere til.
  public boolean addPassage(Passage passage) {
    if (passages.containsValue(passage)) {
      return false;
    } else {
      passage.setStory(this);
      Link link = new Link(passage.getTitle(), passage.getTitle());
      link.setStory(this);
      passages.put(link, passage);
      return true;
    }
  }

  /**
   * This function returns a list of all the links connected to a passage.
   *
   * @return A list of all the links connected to a passage.
   */
  public List<Link> getLinksConnectedWithPassage(Passage passage) {
    // get the passage list of links and return the ones that have the same reference as the
    // passage title
    List<Link> links = passage.getLinks();
    return passages.keySet().stream().filter(links::contains).toList();
  }

  /**
   * This function returns a list of all the passages connected to a link.
   *
   * @return A list of all the passages connected to a link.
   */
  public List<Passage> getPassagesConnectedWithLink(Link link) {
    return passages.values().stream().filter(passage -> passage.getLinks().contains(link)).toList();
  }

  public List<Passage> getPassages() {
    // opening passage and passages.values().stream().filter(Objects::nonNull).toList();
    return new ArrayList<>(passages.values());
  }

  public List<Passage> getPassagesExceptForOpeningPassage() {
    return passages.values().stream().filter(passage -> !passage.equals(openingPassage)).toList();
  }

  public Link getRealLinkBetweenPassages(Passage passage1, Passage passage2) {
    return passage1.getLinks().stream()
        .filter(link -> link.getReference().equals(passage2.getTitle()))
        .findFirst()
        .orElse(null);
  }

  public Passage getSourcePassage(Link link) {
    return this.getPassages().stream()
        .filter(passage -> passage.getLinks().contains(link))
        .findFirst()
        .orElse(null);
  }

  public Link reverseLink(Link link) {
    return this.getPassagesHashMap().keySet().stream()
        .filter(l -> l.equals(link))
        .findFirst()
        .orElseThrow();
  }

  public List<Passage> getAllPassagesThatDoesNotHaveALinkPointingToThem() {
    return passages.values().stream()
        .filter(
            passage ->
                passages.values().stream()
                    .noneMatch(
                        p ->
                            p.getLinks()
                                .contains(new Link(passage.getTitle(), passage.getTitle()))))
        .toList();
  }

  public Passage getLinkedPassage(Link link) {
    return passages.get(link);
  }

  public void removePassage(Link link) {
    passages.remove(link);
  }

  /**
   * This function returns a list of all the links that are broken. A broken link is a link that
   * points to a passage that does not exist.
   *
   * @return A list of all the links that are broken.
   */
  public List<Link> getBrokenLinks() {
    return passages.values().stream()
        .flatMap(passage -> passage.getLinks().stream().filter(link -> !passages.containsKey(link)))
        .toList();
  }

  @Override
  public String toString() {
    return title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * This function returns the minimum number of passages needed to traverse from the opening
   * passage to a given passage.
   *
   * @param to The passage to go to.
   * @return The minimum number of passages.
   */
  public int shortestPathFromOpeningPassage(Passage to) {
    Map<Passage, Passage> previousPassages = new HashMap<>();
    Queue<Passage> queue = new LinkedList<>();
    queue.add(openingPassage);
    previousPassages.put(openingPassage, null);

    while (!queue.isEmpty()) {
      Passage currentPassage = queue.remove();

      // If we've reached the opening passage, return the number of passages in the path
      if (currentPassage.equals(to)) {
        List<Passage> shortestPath = new ArrayList<>();
        for (Passage passage = to; passage != null; passage = previousPassages.get(passage)) {
          shortestPath.add(passage);
        }
        return shortestPath.size();
      }

      // Else, add all the linked passages to the queue
      for (Link link : currentPassage.getLinks()) {
        Passage linkedPassage = getLinkedPassage(link);
        if (linkedPassage != null && !previousPassages.containsKey(linkedPassage)) {
          queue.add(linkedPassage);
          previousPassages.put(linkedPassage, currentPassage);
        }
      }
    }

    return 0; // No path found
  }
}
