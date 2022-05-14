package com.ufmg.testedesoftware.animelistofmine.mapper;

import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody;
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody;
import com.ufmg.testedesoftware.animelistofmine.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
  public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

  public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

  public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
