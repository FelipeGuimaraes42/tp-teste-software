package com.ufmg.testedesoftware.animelistofmine.mapper

import com.ufmg.testedesoftware.animelistofmine.domain.Anime
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody
import org.apache.commons.lang3.ObjectUtils
import spock.lang.Specification

class AnimeMapperTest extends Specification {
    def "It should convert a AnimePostRequestBody into a Movie successfully"() {
        given:
        AnimePostRequestBody animePostRequestBody = new AnimePostRequestBody("ODDTAXI", 10.0)

        when:
        Anime anime = AnimeMapper.INSTANCE.fromAnimePostRequestBodyToAnime(animePostRequestBody)

        then: 'verify if the conversion was a success'
        assert anime instanceof Anime
        assert ObjectUtils.isNotEmpty(anime)

        and: 'validate the attribute values'
        assert ObjectUtils.isEmpty(anime.getId())
        assert anime.getName() == animePostRequestBody.getName()
        assert anime.getScore() == animePostRequestBody.getScore()
    }

    def "It should convert a AnimePostRequestBody into a Movie successfully 2"() {
        given:
        AnimePostRequestBody animePostRequestBody = null

        when:
        Anime anime = AnimeMapper.INSTANCE.fromAnimePostRequestBodyToAnime(animePostRequestBody)

        then: 'verify if the conversion was a success'
        assert ObjectUtils.isEmpty(anime)
    }

    def "It should convert a AnimePutRequestBody into a Movie successfully"() {
        given:
        AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(1L, "ODDTAXI", 10.0)

        when:
        Anime anime = AnimeMapper.INSTANCE.fromAnimePutRequestBodyToAnime(animePutRequestBody)

        then: 'verify if the conversion was a success'
        assert anime instanceof Anime
        assert ObjectUtils.isNotEmpty(anime)
        assert ObjectUtils.isNotEmpty(anime.getId())

        and: 'validate the attribute values'
        assert anime.getId() == animePutRequestBody.getId()
        assert anime.getName() == animePutRequestBody.getName()
        assert anime.getScore() == animePutRequestBody.getScore()
    }

    def "It should convert a AnimePutRequestBody into a Movie successfully 2"() {
        given:
        AnimePutRequestBody animePutRequestBody = null

        when:
        Anime anime = AnimeMapper.INSTANCE.fromAnimePutRequestBodyToAnime(animePutRequestBody)

        then: 'verify if the conversion was a success'
        assert ObjectUtils.isEmpty(anime)
    }
}
