package com.ufmg.testedesoftware.animelistofmine.service

import com.ufmg.testedesoftware.animelistofmine.domain.Anime
import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestException
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
        Anime savedAnime0 = createAnime()
        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)

        when:
        List<Anime> listOfAnime = animeService.listAll()

        then:
        1 * animeRepository.findAll() >> [savedAnime0, savedAnime1]

        and: 'validate the method return'
        assert ObjectUtils.isNotEmpty(listOfAnime)
        assert listOfAnime.size() == 2

        assert listOfAnime.get(0) == savedAnime0
        assert listOfAnime.get(1) == savedAnime1
    }

    def "it should throw a BadRequestException when id is not found in data base"() {
        given:
        String expectedMessage = "Anime not found"
        Long id = 42L

        when:
        animeService.findByIdOrThrowBadRequestException(id)

        then:
        animeRepository.findById(id) >> Optional.empty()

        and:
        BadRequestException exception = thrown(BadRequestException)
        assert exception.getMessage() == expectedMessage
    }
}
