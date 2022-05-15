package com.ufmg.testedesoftware.animelistofmine.service

import com.ufmg.testedesoftware.animelistofmine.domain.Anime
import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository
import org.apache.commons.lang3.ObjectUtils
import spock.lang.Specification

import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createAnime

class AnimeServiceTest extends Specification {
    AnimeService animeService
    AnimeRepository animeRepository = Mock()

    def setup(){
        animeService = new AnimeService(animeRepository)
    }

    def "it should return all animes saved in data base successfully"(){
        given:
        List<Anime> savedAnimes = [createAnime(), createAnime(2L, "Sailor Moon", 9.0)]

        when:
        List<Anime> listOfAnime = animeService.listAll()

        then:
        1 * animeRepository.findAll() >> savedAnimes

        and:
        assert ObjectUtils.isNotEmpty(listOfAnime)
        assert listOfAnime.size() == 2

        assert listOfAnime.get(0).getId() == 1L
        assert listOfAnime.get(0).getName() == "Dragon Ball Z"
        assert listOfAnime.get(0).getScore() == 9.0

        assert listOfAnime.get(1).getId() == 2L
        assert listOfAnime.get(1).getName() == "Sailor Moon"
        assert listOfAnime.get(1).getScore() == 9.0
    }
}
