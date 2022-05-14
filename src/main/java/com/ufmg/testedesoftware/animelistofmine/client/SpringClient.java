package com.ufmg.testedesoftware.animelistofmine.client;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
  public static void main(String[] args) {
    final String postUrl = "http://localhost:8080/animes/";
    ResponseEntity<Anime> entity =
        new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 4);
    log.info(entity);

    ResponseEntity<List<Anime>> exchange =
        new RestTemplate()
            .exchange(
                "http://localhost:8080/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    log.info(exchange.getBody());

    Anime kimetsu = Anime.builder().name("Kimetsu no Yaiba").build();
    Anime savedAnime = new RestTemplate().postForObject(postUrl, kimetsu, Anime.class);
    log.info(savedAnime);

    Anime missKobayashi = Anime.builder().name("Miss Kobayashi' Dragons Maid").build();
    ResponseEntity<Anime> savedAnime2 =
        new RestTemplate()
            .exchange(
                postUrl,
                HttpMethod.POST,
                new HttpEntity<>(missKobayashi, createJsonHeader()),
                Anime.class);
    log.info(savedAnime2);

    Anime jojo = Anime.builder().name("Jojo's Bizarre Adventures").build();
    ResponseEntity<Anime> savedAnime3 =
        new RestTemplate()
            .exchange(
                postUrl, HttpMethod.POST, new HttpEntity<>(jojo, createJsonHeader()), Anime.class);
    log.info(savedAnime3);
  }

  private static HttpHeaders createJsonHeader() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return httpHeaders;
  }
}
