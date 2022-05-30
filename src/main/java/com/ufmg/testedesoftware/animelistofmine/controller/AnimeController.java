package com.ufmg.testedesoftware.animelistofmine.controller;

import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody;
import com.ufmg.testedesoftware.animelistofmine.service.AnimeService;
import com.ufmg.testedesoftware.animelistofmine.util.DateUtil;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("animes")
@Log4j2
public class AnimeController {
  private final DateUtil dateUtil = new DateUtil();
  private final AnimeService animeService;

  public AnimeController(AnimeService animeService) {
    this.animeService = animeService;
  }

  @GetMapping
  public ResponseEntity<Page<Anime>> list(Pageable pageable) {
    log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
    return ResponseEntity.ok(animeService.list(pageable));
  }

  @GetMapping(path = "/all")
  public ResponseEntity<List<Anime>> listAll() {
    log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
    return ResponseEntity.ok(animeService.listAll());
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Anime> findById(@PathVariable long id) {
    return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
  }

  @GetMapping(path = "/find")
  public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
    return ResponseEntity.ok(animeService.findByName(name));
  }

  @GetMapping(path = "/top")
  public ResponseEntity<List<Anime>> getTopTen() {
    return ResponseEntity.ok(animeService.getTopTen());
  }

  @GetMapping(path = "/bad")
  public ResponseEntity<List<Anime>> getBadTen() {
    return ResponseEntity.ok(animeService.getBadTen());
  }

  @PostMapping
  public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
    return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam long id) {
    animeService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping
  public ResponseEntity<Void> replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody) {
    animeService.replace(animePutRequestBody);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
