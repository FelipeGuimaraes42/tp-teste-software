package com.ufmg.testedesoftware.animelistofmine.service

import com.ufmg.testedesoftware.animelistofmine.domain.Anime
import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestException
import com.ufmg.testedesoftware.animelistofmine.mapper.AnimeMapper
import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody
import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody
import org.apache.commons.lang3.ObjectUtils
import org.springframework.data.domain.Sort
import spock.lang.Specification

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createAnime

class AnimeServiceTest extends Specification {
    AnimeService animeService
    AnimeRepository animeRepository = Mock()

    def setup(){
        animeService = new AnimeService(animeRepository)
    }

    def "it should save an anime in data base successfully"(){
        given:
        Anime savedAnime0 = createAnime()
        AnimePostRequestBody animePostRequestBody = new AnimePostRequestBody("Dragon Ball Z", 9.0)

        when:
        Anime anime = animeService.save(animePostRequestBody)

        then:
        1 * animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody)) >> savedAnime0

        and: 'validate anime value and the return method'
        assert ObjectUtils.isNotEmpty(anime)
        assert anime == savedAnime0

    }

    def "it should throw an error when trying to save an anime with empty string"(){
        given:
        String expectedErrorMessage = "Post Request Body cannot be null"

        when:
        Anime anime = animeService.save(null)

        then:
        BadRequestException exception = thrown(BadRequestException)
        assert ObjectUtils.isEmpty(anime)
        assert exception.getMessage() == expectedErrorMessage
    }

    def "it should return an anime in data base based on the name"(){
        given:
        String animeName = "Sailor Moon"
        Anime savedAnime1 = createAnime(2L, animeName, 9.0)

        when:
        List<Anime> listOfAnime = animeService.findByName(animeName)

        then:
        1 * animeRepository.findByName(animeName) >> [savedAnime1]

        and: 'validate the method return'
        assert ObjectUtils.isNotEmpty(listOfAnime)
        assert listOfAnime.size() == 1

        assert listOfAnime.get(0) == savedAnime1
    }

    def "it should return an empty list when trying to get an anime name not in the data base"(){
        given:
        String animeName = "Sailor Moon"

        when:
        List<Anime> listOfAnime = animeService.findByName(animeName)

        then:
        1 * animeRepository.findByName(animeName) >> []

        and: 'validate the method return'
        assert ObjectUtils.isEmpty(listOfAnime)
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

    def "it should return an emptyList when thre is no animes saved in data base"(){
        when:
        List<Anime> listOfAnime = animeService.listAll()

        then:
        1 * animeRepository.findAll() >> []

        and: 'validate the method return'
        assert ObjectUtils.isEmpty(listOfAnime)
    }

    def "it should return an anime based on the given id"() {
        given:
        Anime savedAnime = createAnime()
        Long id = 1L

        when:
        Anime expectedAnime = animeService.findByIdOrThrowBadRequestException(id)

        then:
        1 * animeRepository.findById(id) >> Optional.of(savedAnime)

        and:
        assert ObjectUtils.isNotEmpty(expectedAnime)

        assert expectedAnime == savedAnime
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

    def "it should delete an anime that was saved in data base successfully"(){
        given:
        Long id = 2L
        Anime savedAnime1 = createAnime(id, "Sailor Moon", 9.0)

        when:
        List<Anime> listOfAnime = animeService.delete(id)

        then:
        1 * animeRepository.findById(id) >> Optional.of(savedAnime1)
        assert ObjectUtils.isEmpty(listOfAnime)
    }

    def "it should throw an BadRequestException when trying to delete an unexistent anime"(){
        given:
        Long id = 2L
        String expectedExceptionMessage = "Anime not found"

        when:
        List<Anime> listOfAnime = animeService.delete(id)

        then:
        1 * animeRepository.findById(id) >> Optional.empty()

        and: 'Assert that the exceptiion is being throw'
        BadRequestException exception = thrown(BadRequestException)
        assert exception.getMessage() == expectedExceptionMessage
        assert ObjectUtils.isEmpty(listOfAnime)
    }

    def "it should replace an previously added anime successfully"(){
        given:
        Long id = 1L
        Anime savedAnime0 = createAnime()
        AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(id, "Dragon Ball Z", 9.0)

        when:
        List<Anime> listOfAnime = animeService.replace(animePutRequestBody)

        then:
        1 * animeRepository.findById(id) >> Optional.of(savedAnime0)
        1 * animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePutRequestBody)) >> savedAnime0
        assert ObjectUtils.isEmpty(listOfAnime)
    }

    def "it should throw an error when trying to replace an unexistent anime"(){
        given:
        String expectedExceptionMessage = "Anime not found"
        Long id = 1L
        AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(id, "Dragon Ball Z", 9.0)

        when:
        List<Anime> listOfAnime = animeService.replace(animePutRequestBody)

        then:
        1 * animeRepository.findById(id) >> Optional.empty()
        assert ObjectUtils.isEmpty(listOfAnime)

        and: 'Assert that the exceptiion is being throw'
        BadRequestException exception = thrown(BadRequestException)
        assert exception.getMessage() == expectedExceptionMessage
    }
}
