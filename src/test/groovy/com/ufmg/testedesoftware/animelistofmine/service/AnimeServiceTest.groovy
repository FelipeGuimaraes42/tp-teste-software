package com.ufmg.testedesoftware.animelistofmine.service

import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository
import spock.lang.Specification

class AnimeServiceTest extends Specification {
    AnimeService animeService
    AnimeRepository animeRepository = Mock()

    def setup(){
        animeService = new AnimeService(animeRepository)
    }

    def "itShouldTest"(){
        given:
        when:
        animeService.listAll()
        then:
        1 * animeRepository.findAll()
    }
}
