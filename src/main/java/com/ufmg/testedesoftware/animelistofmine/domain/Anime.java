package com.ufmg.testedesoftware.animelistofmine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;

@Entity
@Builder
public class Anime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  public Anime(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Anime() {
    // Default Constructor
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Anime{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Anime anime = (Anime) o;
    return Objects.equals(id, anime.id) && Objects.equals(name, anime.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
