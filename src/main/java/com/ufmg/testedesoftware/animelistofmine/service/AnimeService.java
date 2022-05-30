package com.ufmg.testedesoftware.animelistofmine.service;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestException;
import com.ufmg.testedesoftware.animelistofmine.mapper.AnimeMapper;
import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimeService {

  private final AnimeRepository animeRepository;

  public AnimeService(AnimeRepository animeRepository) {
    this.animeRepository = animeRepository;
  }

  public Page<Anime> list(Pageable pageable) {
    return animeRepository.findAll(pageable);
  }

  public List<Anime> listAll() {
    return animeRepository.findAll();
  }

  public List<Anime> findByName(String name) {
    return animeRepository.findByName(name);
  }

  public Anime findByIdOrThrowBadRequestException(long id) {
    return animeRepository
        .findById(id)
        .orElseThrow(() -> new BadRequestException("Anime not found"));
  }

  @Transactional
  public Anime save(AnimePostRequestBody animePostRequestBody) {
    if (animePostRequestBody == null) {
      throw new BadRequestException("Post Request Body cannot be null");
    }
    return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
  }

  public void delete(long id) {
    animeRepository.delete(findByIdOrThrowBadRequestException(id));
  }

  public void replace(AnimePutRequestBody animePutRequestBody) {
    Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
    Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
    anime.setId(savedAnime.getId());
    animeRepository.save(anime);
  }

  public List<Anime> getTopTen() {
    return animeRepository.getTopTen();
  }

  public List<Anime> getBadTen() {
    return animeRepository.getBadTen();
  }
}
