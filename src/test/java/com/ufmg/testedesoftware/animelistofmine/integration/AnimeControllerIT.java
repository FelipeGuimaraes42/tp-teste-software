package com.ufmg.testedesoftware.animelistofmine.integration;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository;
import com.ufmg.testedesoftware.animelistofmine.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createAnime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;
    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void itShouldReturnListOfAnimeInsidePageObjectWhenSuccessful() {
        Anime savedAnime = animeRepository.save(createAnime());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList().get(0)).isEqualTo(savedAnime);
        Assertions.assertThat(animePage.toList().get(0).getId()).isEqualTo(savedAnime.getId());
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
}
