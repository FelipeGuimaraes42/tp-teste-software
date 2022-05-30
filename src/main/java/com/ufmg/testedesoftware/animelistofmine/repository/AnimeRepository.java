package com.ufmg.testedesoftware.animelistofmine.repository;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
  List<Anime> findByName(String name);

  @Query(value = "SELECT * from anime an ORDER BY an.score DESC LIMIT 10", nativeQuery = true)
  List<Anime> getTopTen();

  @Query(value = "SELECT * from anime an ORDER BY an.score ASC LIMIT 10", nativeQuery = true)
  List<Anime> getBadTen();
}
