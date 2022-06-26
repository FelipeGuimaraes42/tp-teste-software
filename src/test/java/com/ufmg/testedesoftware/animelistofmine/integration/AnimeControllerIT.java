package com.ufmg.testedesoftware.animelistofmine.integration;

import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createAnime;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository;
import com.ufmg.testedesoftware.animelistofmine.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private AnimeRepository animeRepository;

  @Test
  @DisplayName("integrated test - list returns list of anime inside page object when successful")
  void itShouldReturnListOfAnimeInsidePageObjectWhenSuccessful() {
    Anime savedAnime = animeRepository.save(createAnime());

    String expectedName = savedAnime.getName();

    PageableResponse<Anime> animePage =
        testRestTemplate
            .exchange(
                "/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {})
            .getBody();

    Assertions.assertThat(animePage).isNotNull();
    Assertions.assertThat(animePage.toList().get(0)).isEqualTo(savedAnime);
    Assertions.assertThat(animePage.toList().get(0).getId()).isEqualTo(savedAnime.getId());
    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("integrated test - listAll should return full list of animes in database")
  void itShouldListAllAnimeSavedInDBSuccessfully() {
    Anime savedAnime0 = animeRepository.save(createAnime());
    Anime savedAnime1 = animeRepository.save(createAnime(2L, "Spy x Family", 9.0));

    String expectedName0 = savedAnime0.getName();
    String expectedName1 = savedAnime1.getName();

    List<Anime> animes =
        testRestTemplate
            .exchange(
                "/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                })
            .getBody();

    Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(2);

    Assertions.assertThat(animes.get(0)).isEqualTo(savedAnime0);
    Assertions.assertThat(animes.get(1)).isEqualTo(savedAnime1);

    Assertions.assertThat(animes.get(0).getId()).isEqualTo(savedAnime0.getId());
    Assertions.assertThat(animes.get(1).getId()).isEqualTo(savedAnime1.getId());

    Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName0);
    Assertions.assertThat(animes.get(1).getName()).isEqualTo(expectedName1);
  }

  @Test
  @DisplayName("integrated test - finding anime by id")
  void itShouldFindAnimeByIdInDBSuccessfully() {
    Anime savedAnime = animeRepository.save(createAnime());

    Long expectedId = savedAnime.getId();

    Anime anime =
      testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

    Assertions.assertThat(anime).isNotNull();
    Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
  }

  @Test
  @DisplayName("integrated test - finding anime by its name")
  void itShouldFindAnimeByNameInDBSuccessfully() {
    Anime savedAnime = animeRepository.save(createAnime(1L, "Oddtaxi", 9.5));

    String expectedName = savedAnime.getName();
    String url = String.format("/animes/find/?name=%s", expectedName);

    List<Anime> animes =
        testRestTemplate
            .exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                })
            .getBody();

    Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

    Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("integrated test - returns an empty list if anime name is not found")
  void itShouldNotFindAnimeInDB() {
    String url = String.format("/animes/find/?name=%s", "Dragon Ball Super");

    List<Anime> animes =
        testRestTemplate
            .exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                })
            .getBody();

    Assertions.assertThat(animes).isNotNull().isEmpty();
  }
}
