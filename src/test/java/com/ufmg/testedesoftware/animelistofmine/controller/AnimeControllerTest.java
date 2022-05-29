package com.ufmg.testedesoftware.animelistofmine.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody;
import com.ufmg.testedesoftware.animelistofmine.service.AnimeService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

@WebMvcTest(AnimeController.class)
class AnimeControllerTest {
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @MockBean private AnimeService animeService;

  private static final String ENDPOINT_ANIMES = "/animes";
  private static final String ENDPOINT_ANIMES_ALL = "/animes/all";
  private static final String ENDPOINT_ANIMES_BY_ID = "/animes/{id}";
  private static final String ENDPOINT_ANIMES_FIND = "/animes/find";
  private static final String APPLICATION_JSON = "application/json";
  private static final String ANIME_NAME = "ODDTAXI";
  private static final Double MAX_SCORE = 10.0;
  private static final Double MIN_SCORE = 0.0;
  private static final Long ANIME_ID = 1L;

  @Test
  void itShouldTestTheGETEndpointAnimesSuccessfully() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES)).andExpect(status().isOk());
  }

  @Test
  void itShouldTestTheGETEndpointAnimesAllSuccessfully() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_ALL)).andExpect(status().isOk());
  }

  @Test
  void itShouldTestTheGETEndpointAnimesIdSuccessfully() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_BY_ID, 1))
        .andExpect(status().isOk());
  }

  @Test
  void itShouldReturnBadRequestWhenWrongPathVariableTypeIsSentToTheGETEndpointAnimesId()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_BY_ID, "wrongType"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldTestTheGETEndpointAnimesFindSuccessfully() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("name", ANIME_NAME);

    mockMvc
        .perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_FIND).params(requestParams))
        .andExpect(status().isOk());
  }

  @Test
  void itShouldReturnBadRequestWhenNoRequiredParamIsSentToTheGETEndpointAnimesFind()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_FIND))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenWrongParamIsSentToTheGETEndpointAnimesFind() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("notName", ANIME_NAME);

    mockMvc
        .perform(MockMvcRequestBuilders.get(ENDPOINT_ANIMES_FIND).params(requestParams))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldTestThePOSTEndpointAnimesSuccessfully() throws Exception {
    AnimePostRequestBody animePostRequestBody = new AnimePostRequestBody(ANIME_NAME, MAX_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePostRequestBody)))
        .andExpect(status().isCreated());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyDoesNotHaveNameInPOSTEndpointAnimes()
      throws Exception {
    AnimePostRequestBody animePostRequestBody =
        new AnimePostRequestBody(StringUtils.EMPTY, MAX_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePostRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyScoreIsLessThanMinimumInPOSTEndpointAnimes()
      throws Exception {
    AnimePostRequestBody animePostRequestBody = new AnimePostRequestBody(ANIME_NAME, MIN_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePostRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyScoreIsGreaterThanMaximumInPOSTEndpointAnimes()
      throws Exception {
    AnimePostRequestBody animePostRequestBody =
        new AnimePostRequestBody(ANIME_NAME, MAX_SCORE + 1.0);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePostRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyIsEmptyInPOSTEndpointAnimes() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(ENDPOINT_ANIMES).contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldTestThePUTEndpointAnimesSuccessfully() throws Exception {
    AnimePutRequestBody animePutRequestBody =
        new AnimePutRequestBody(ANIME_ID, ANIME_NAME, MAX_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePutRequestBody)))
        .andExpect(status().isNoContent());
  }

  @Test
  void itShouldTestThePUTEndpointAnimesWhenRequestBodyIdIsEmptySuccessfully() throws Exception {
    AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(null, ANIME_NAME, MAX_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePutRequestBody)))
        .andExpect(status().isNoContent());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyIsEmptyInPUTEndpointAnimes() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(ENDPOINT_ANIMES).contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyDoesNotHaveNameInPUTEndpointAnimes()
      throws Exception {
    AnimePutRequestBody animePutRequestBody =
        new AnimePutRequestBody(ANIME_ID, StringUtils.EMPTY, MAX_SCORE);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePutRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyScoreIsLessThanMinimumInPUTEndpointAnimes()
      throws Exception {
    AnimePutRequestBody animePutRequestBody =
        new AnimePutRequestBody(ANIME_ID, StringUtils.EMPTY, MIN_SCORE - 0.001);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePutRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenRequestBodyScoreIsGreaterThanMaximumInPUTEndpointAnimes()
      throws Exception {
    AnimePutRequestBody animePutRequestBody =
        new AnimePutRequestBody(ANIME_ID, StringUtils.EMPTY, MAX_SCORE + 0.001);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animePutRequestBody)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldTestTheDELETEEndpointAnimesSuccessfully() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("id", "1");

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .params(requestParams))
        .andExpect(status().isNoContent());
  }

  @Test
  void itShouldReturnBadRequestWhenWrongQueryParamIsSentToTheEndpointAnimesDELETE()
      throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("notId", "1");

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(ENDPOINT_ANIMES)
                .contentType(APPLICATION_JSON)
                .params(requestParams))
        .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldReturnBadRequestWhenNoneQueryParamIsSentToTheEndpointAnimesDELETE()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete(ENDPOINT_ANIMES).contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
