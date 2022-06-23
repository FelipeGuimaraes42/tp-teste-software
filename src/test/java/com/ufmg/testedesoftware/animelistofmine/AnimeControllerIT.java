package academy.devdojo.springboot2essentials.integration;

import static academy.devdojo.springboot2essentials.util.CreateAnimeUtil.createAnime;
import static academy.devdojo.springboot2essentials.util.CreateAnimeUtil.createAnimePostRequestBody;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.domain.DevDojoUser;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import academy.devdojo.springboot2essentials.repository.DevDojoUserRepository;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;
import academy.devdojo.springboot2essentials.wrapper.PageableResponse;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
  @Autowired
  @Qualifier(value = "testRestTemplateRoleUser")
  private TestRestTemplate testRestTemplateRoleUser;

  @Autowired
  @Qualifier(value = "testRestTemplateRoleAdmin")
  private TestRestTemplate testRestTemplateRoleAdmin;

  private static final DevDojoUser USER = DevDojoUser.builder()
      .name("Felipe Santos")
      .authorities("ROLE_USER")
      .username("felipe")
      .password("{bcrypt}$2a$10$aiXSvOaz7LmO72rWtiH4OeGkuLmufRrfcgXbcODel6lS96RZt6tgK")
      .build();

  private static final DevDojoUser ADMIN = DevDojoUser.builder()
      .name("DevDojo Course")
      .authorities("ROLE_USER,ROLE_ADMIN")
      .username("devdojo")
      .password("{bcrypt}$2a$10$aiXSvOaz7LmO72rWtiH4OeGkuLmufRrfcgXbcODel6lS96RZt6tgK")
      .build();

  @Autowired
  private AnimeRepository animeRepository;
  @Autowired
  private DevDojoUserRepository devDojoUserRepository;

  @TestConfiguration
  @Lazy
  static class Config {
    @Bean(name = "testRestTemplateRoleUser")
    public TestRestTemplate testRestTemplateRoleUserCreator(
        @Value("${local.server.port}") int port) {
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
          .rootUri("http://localhost:" + port)
          .basicAuthentication("felipe", "academy");
      return new TestRestTemplate(restTemplateBuilder);
    }

    @Bean(name = "testRestTemplateRoleAdmin")
    public TestRestTemplate testRestTemplateRoleAdminCreator(
        @Value("${local.server.port}") int port) {
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
          .rootUri("http://localhost:" + port)
          .basicAuthentication("devdojo", "academy");
      return new TestRestTemplate(restTemplateBuilder);
    }
  }

  @Test
  @DisplayName("integrated test - testing get all anime paged")
  void itShouldListAllAnimeSavedOnDBPagedSuccessfully() {
    Anime savedAnime = animeRepository.save(createAnime());
    devDojoUserRepository.save(USER);

    PageableResponse<Anime> animePage = testRestTemplateRoleUser
        .exchange(
            "/animes",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<PageableResponse<Anime>>() {
            })
        .getBody();

    Assertions.assertThat(animePage).isNotNull();
    Assertions.assertThat(animePage.toList().get(0)).isEqualTo(savedAnime);
    Assertions.assertThat(animePage.toList().get(0).getId()).isEqualTo(savedAnime.getId());
    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(savedAnime.getName());
  }

  @Test
  @DisplayName("integrated test - testing get all anime")
  void itShouldListAllAnimeSavedOnDBSuccessfully() {
    Anime savedAnime0 = animeRepository.save(createAnime());
    Anime savedAnime1 = animeRepository.save(createAnime("Dragon Ball GT"));
    devDojoUserRepository.save(USER);

    List<Anime> animes = testRestTemplateRoleUser
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

    Assertions.assertThat(animes.get(0).getName()).isEqualTo(savedAnime0.getName());
    Assertions.assertThat(animes.get(1).getName()).isEqualTo(savedAnime1.getName());
  }

  @Test
  @DisplayName("integrated test - testing find anime by id")
  void itShouldFindByIdAnimeSavedOnDBSuccessfully() {
    Anime savedAnime0 = animeRepository.save(createAnime());
    Anime savedAnime1 = animeRepository.save(createAnime("Dragon Ball GT"));
    devDojoUserRepository.save(USER);

    // Anime anime0 =
    // testRestTemplate
    // .exchange(
    // "/animes/{id}",
    // HttpMethod.GET,
    // null,
    // new ParameterizedTypeReference<Anime>() {},
    // savedAnime0.getId())
    // .getBody();

    Anime anime0 = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, savedAnime0.getId());

    Anime anime1 = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, savedAnime1.getId());

    Assertions.assertThat(anime0).isNotNull();
    Assertions.assertThat(anime1).isNotNull();

    Assertions.assertThat(anime0).isEqualTo(savedAnime0);
    Assertions.assertThat(anime1).isEqualTo(savedAnime1);

    Assertions.assertThat(anime0.getId()).isEqualTo(savedAnime0.getId());
    Assertions.assertThat(anime1.getId()).isEqualTo(savedAnime1.getId());

    Assertions.assertThat(anime0.getName()).isEqualTo(savedAnime0.getName());
    Assertions.assertThat(anime1.getName()).isEqualTo(savedAnime1.getName());
  }

  @Test
  @DisplayName("integrated test - testing find anime by name")
  void itShouldFindByNameAnimeSavedOnDBSuccessfully() {
    Anime savedAnime0 = animeRepository.save(createAnime());
    Anime savedAnime1 = animeRepository.save(createAnime("Dragon Ball GT"));
    devDojoUserRepository.save(USER);

    String url0 = String.format("/animes/find/?name=%s", savedAnime0.getName());
    String url1 = String.format("/animes/find/?name=%s", savedAnime1.getName());

    List<Anime> animes0 = testRestTemplateRoleUser
        .exchange(url0, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        })
        .getBody();

    List<Anime> animes1 = testRestTemplateRoleUser
        .exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        })
        .getBody();

    Assertions.assertThat(animes0).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(animes1).isNotNull().isNotEmpty().hasSize(1);

    Assertions.assertThat(animes0.get(0)).isEqualTo(savedAnime0);
    Assertions.assertThat(animes1.get(0)).isEqualTo(savedAnime1);

    Assertions.assertThat(animes0.get(0).getId()).isEqualTo(savedAnime0.getId());
    Assertions.assertThat(animes1.get(0).getId()).isEqualTo(savedAnime1.getId());

    Assertions.assertThat(animes0.get(0).getName()).isEqualTo(savedAnime0.getName());
    Assertions.assertThat(animes1.get(0).getName()).isEqualTo(savedAnime1.getName());
  }

  @Test
  @DisplayName("integrated test - testing not find anime by name")
  void itShouldNotFindByNameAnimeSavedOnDB() {
    animeRepository.save(createAnime());
    devDojoUserRepository.save(USER);

    String url0 = String.format("/animes/find/?name=%s", "Dragon Ball Super");

    List<Anime> animes = testRestTemplateRoleUser
        .exchange(url0, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        })
        .getBody();

    Assertions.assertThat(animes).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("integration test - testing save anime")
  void itShouldSaveAnimeOnDBSuccessfully() {
    devDojoUserRepository.save(ADMIN);

    AnimePostRequestBody animePostRequestBody = createAnimePostRequestBody();
    ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleAdmin.postForEntity("/animes", animePostRequestBody,
        Anime.class);

    Assertions.assertThat(animeResponseEntity).isNotNull();
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
  }

}
