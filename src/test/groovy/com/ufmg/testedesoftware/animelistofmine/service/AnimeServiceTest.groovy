//package com.ufmg.testedesoftware.animelistofmine.service
//
//import com.ufmg.testedesoftware.animelistofmine.domain.Anime
//import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestException
//import com.ufmg.testedesoftware.animelistofmine.mapper.AnimeMapper
//import com.ufmg.testedesoftware.animelistofmine.repository.AnimeRepository
//import com.ufmg.testedesoftware.animelistofmine.requests.AnimePostRequestBody
//import com.ufmg.testedesoftware.animelistofmine.requests.AnimePutRequestBody
//import org.apache.commons.lang3.ObjectUtils
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.PageImpl
//import org.springframework.data.domain.PageRequest
//import spock.lang.Specification
//
//import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createAnime
//import static com.ufmg.testedesoftware.animelistofmine.mock.AnimeMock.createOrderedAnimeList
//
//class AnimeServiceTest extends Specification {
//    AnimeService animeService
//    AnimeRepository animeRepository = Mock()
//
//    def setup() {
//        animeService = new AnimeService(animeRepository)
//    }
//
//    def "it should save an anime in data base successfully"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        AnimePostRequestBody animePostRequestBody = new AnimePostRequestBody("Dragon Ball Z", 9.0)
//
//        when:
//        Anime anime = animeService.save(animePostRequestBody)
//
//        then:
//        1 * animeRepository.save(AnimeMapper.INSTANCE.fromAnimePostRequestBodyToAnime(animePostRequestBody)) >> savedAnime0
//
//        and: 'validate anime value and the return method'
//        assert ObjectUtils.isNotEmpty(anime)
//        assert anime == savedAnime0
//
//    }
//
//    def "it should throw an error when trying to save an anime with empty string"() {
//        given:
//        String expectedErrorMessage = "Post Request Body cannot be null"
//
//        when:
//        Anime anime = animeService.save(null)
//
//        then:
//        BadRequestException exception = thrown(BadRequestException)
//        assert ObjectUtils.isEmpty(anime)
//        assert exception.getMessage() == expectedErrorMessage
//    }
//
//    def "it should return an anime in data base based on the name"() {
//        given:
//        String animeName = "Sailor Moon"
//        Anime savedAnime1 = createAnime(2L, animeName, 9.0)
//
//        when:
//        List<Anime> listOfAnime = animeService.findByName(animeName)
//
//        then:
//        1 * animeRepository.findByName(animeName) >> [savedAnime1]
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(listOfAnime)
//        assert listOfAnime.size() == 1
//
//        assert listOfAnime.get(0) == savedAnime1
//    }
//
//    def "it should return an empty list when trying to get an anime name not in the data base"() {
//        given:
//        String animeName = "Sailor Moon"
//
//        when:
//        List<Anime> listOfAnime = animeService.findByName(animeName)
//
//        then:
//        1 * animeRepository.findByName(animeName) >> []
//
//        and: 'validate the method return'
//        assert ObjectUtils.isEmpty(listOfAnime)
//    }
//
//    def "it should return all animes saved in data base successfully"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)
//
//        when:
//        List<Anime> listOfAnime = animeService.listAll()
//
//        then:
//        1 * animeRepository.findAll() >> [savedAnime0, savedAnime1]
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(listOfAnime)
//        assert listOfAnime.size() == 2
//
//        assert listOfAnime.get(0) == savedAnime0
//        assert listOfAnime.get(1) == savedAnime1
//    }
//
//    def "it should return an emptyList when there is no animes saved in data base"() {
//        when:
//        List<Anime> listOfAnime = animeService.listAll()
//
//        then:
//        1 * animeRepository.findAll() >> []
//
//        and: 'validate the method return'
//        assert ObjectUtils.isEmpty(listOfAnime)
//    }
//
//    def "it should return an anime based on the given id"() {
//        given:
//        Anime savedAnime = createAnime()
//        Long id = 1L
//
//        when:
//        Anime expectedAnime = animeService.findByIdOrThrowBadRequestException(id)
//
//        then:
//        1 * animeRepository.findById(id) >> Optional.of(savedAnime)
//
//        and:
//        assert ObjectUtils.isNotEmpty(expectedAnime)
//
//        assert expectedAnime == savedAnime
//    }
//
//    def "it should throw a BadRequestException when id is not found in data base"() {
//        given:
//        String expectedMessage = "Anime not found"
//        Long id = 42L
//
//        when:
//        animeService.findByIdOrThrowBadRequestException(id)
//
//        then:
//        animeRepository.findById(id) >> Optional.empty()
//
//        and:
//        BadRequestException exception = thrown(BadRequestException)
//        assert exception.getMessage() == expectedMessage
//    }
//
//    def "it should delete an anime that was saved in data base successfully"() {
//        given:
//        Long id = 2L
//        Anime savedAnime1 = createAnime(id, "Sailor Moon", 9.0)
//
//        when:
//        List<Anime> listOfAnime = animeService.delete(id)
//
//        then:
//        1 * animeRepository.findById(id) >> Optional.of(savedAnime1)
//        assert ObjectUtils.isEmpty(listOfAnime)
//    }
//
//    def "it should throw an BadRequestException when trying to delete an nonexistent anime"() {
//        given:
//        Long id = 2L
//        String expectedExceptionMessage = "Anime not found"
//
//        when:
//        List<Anime> listOfAnime = animeService.delete(id)
//
//        then:
//        1 * animeRepository.findById(id) >> Optional.empty()
//
//        and: 'Assert that the exception is being throw'
//        BadRequestException exception = thrown(BadRequestException)
//        assert exception.getMessage() == expectedExceptionMessage
//        assert ObjectUtils.isEmpty(listOfAnime)
//    }
//
//    def "it should replace an previously added anime successfully"() {
//        given:
//        Long id = 1L
//        Anime savedAnime0 = createAnime()
//        AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(id, "Dragon Ball Z", 9.0)
//
//        when:
//        List<Anime> listOfAnime = animeService.replace(animePutRequestBody)
//
//        then:
//        1 * animeRepository.findById(id) >> Optional.of(savedAnime0)
//        1 * animeRepository.save(AnimeMapper.INSTANCE.fromAnimePutRequestBodyToAnime(animePutRequestBody)) >> savedAnime0
//        assert ObjectUtils.isEmpty(listOfAnime)
//    }
//
//    def "it should throw an error when trying to replace an nonexistent anime"() {
//        given:
//        String expectedExceptionMessage = "Anime not found"
//        Long id = 1L
//        AnimePutRequestBody animePutRequestBody = new AnimePutRequestBody(id, "Dragon Ball Z", 9.0)
//
//        when:
//        List<Anime> listOfAnime = animeService.replace(animePutRequestBody)
//
//        then:
//        1 * animeRepository.findById(id) >> Optional.empty()
//        assert ObjectUtils.isEmpty(listOfAnime)
//
//        and: 'Assert that the exception is being throw'
//        BadRequestException exception = thrown(BadRequestException)
//        assert exception.getMessage() == expectedExceptionMessage
//    }
//
//    def "it should return paged all animes saved in data base successfully"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)
//        def repositoryResponsePageZeroSizeTwo = new PageImpl<Anime>([savedAnime0, savedAnime1])
//        def page = PageRequest.of(0, 2)
//
//        when:
//        Page<Anime> animePage = animeService.list(page)
//
//        then:
//        1 * animeRepository.findAll(page) >> repositoryResponsePageZeroSizeTwo
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(animePage)
//        assert animePage.getTotalElements() == 2L
//        assert animePage.toList().get(0) == savedAnime0
//        assert animePage.toList().get(1) == savedAnime1
//    }
//
//    def "it should return the first page of animes saved in data base successfully"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)
//        def databaseAnimes = [savedAnime0, savedAnime1]
//        def repositoryResponsePageZeroSizeOne = new PageImpl<Anime>([savedAnime0])
//        def page = PageRequest.of(0, 1)
//
//        when:
//        Page<Anime> animePage = animeService.list(page)
//
//        then:
//        1 * animeRepository.findAll(page) >> repositoryResponsePageZeroSizeOne
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(animePage)
//        assert animePage.getTotalElements() != databaseAnimes.size()
//        assert animePage.getTotalElements() == 1L
//        assert animePage.toList().get(0) == savedAnime0
//    }
//
//    def "it should return the second page of animes saved in data base successfully"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)
//        def databaseAnimes = [savedAnime0, savedAnime1]
//        def repositoryResponsePageOneSizeOne = new PageImpl<Anime>([savedAnime1])
//        def page = PageRequest.of(1, 1)
//
//        when:
//        Page<Anime> animePage = animeService.list(page)
//
//        then:
//        1 * animeRepository.findAll(page) >> repositoryResponsePageOneSizeOne
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(animePage)
//        assert animePage.getTotalElements() != databaseAnimes.size()
//        assert animePage.getTotalElements() == 1L
//        assert animePage.toList().get(0) == savedAnime1
//    }
//
//    def "it should return empty page when there is no anime in that page"() {
//        given:
//        Anime savedAnime0 = createAnime()
//        Anime savedAnime1 = createAnime(2L, "Sailor Moon", 9.0)
//        def databaseAnimes = [savedAnime0, savedAnime1]
//        def repositoryResponsePageTwentySizeOne = new PageImpl<Anime>(new ArrayList<>())
//        def page = PageRequest.of(20, 1)
//
//        when:
//        Page<Anime> animePage = animeService.list(page)
//
//        then:
//        1 * animeRepository.findAll(page) >> repositoryResponsePageTwentySizeOne
//
//        and: 'validate the method return'
//        assert ObjectUtils.isNotEmpty(animePage)
//        assert animePage.getTotalElements() != databaseAnimes.size()
//        assert animePage.getTotalElements() == 0L
//    }
//
//    def "it should return the top ten anime ordered by score successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 10
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "desc")
//
//        when:
//        List<Anime> topTenAnimes = animeService.getTopTen()
//
//        then:
//        1 * animeRepository.getTopTen() >> animes
//
//        and:
//        assert ObjectUtils.isNotEmpty(topTenAnimes)
//        assert topTenAnimes.size() == 10
//        assert isScoreDescOrdered(topTenAnimes)
//    }
//
//    def "it should return ten anime ordered by score DESC when DB has more than 10 animes successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 15
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "desc")
//        def tenAnimes = animes.subList(0, 10)
//
//        when:
//        List<Anime> topTenAnimes = animeService.getTopTen()
//
//        then:
//        1 * animeRepository.getTopTen() >> tenAnimes
//
//        and:
//        assert ObjectUtils.isNotEmpty(topTenAnimes)
//        assert topTenAnimes.size() == 10
//        assert isScoreDescOrdered(topTenAnimes)
//    }
//
//    def "it should return all animes ordered by score DESC when DB has less than 10 saved animes successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 7
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "DESC")
//
//        when:
//        List<Anime> topTenAnimes = animeService.getTopTen()
//
//        then:
//        1 * animeRepository.getTopTen() >> animes
//
//        and:
//        assert ObjectUtils.isNotEmpty(topTenAnimes)
//        assert topTenAnimes.size() == numberOfAnimesInDataBase
//        assert isScoreDescOrdered(topTenAnimes)
//    }
//
//    def "it should return empty top list when DB has none saved anime successfully"() {
//        given:
//        def animes = Collections.emptyList()
//
//        when:
//        List<Anime> topTenAnimes = animeService.getTopTen()
//
//        then:
//        1 * animeRepository.getTopTen() >> animes
//
//        and:
//        assert ObjectUtils.isEmpty(topTenAnimes)
//    }
//
//    def "it should return the worst ten anime ordered by score successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 10
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "asc")
//
//        when:
//        List<Anime> worstTenAnimes = animeService.getBadTen()
//
//        then:
//        1 * animeRepository.getBadTen() >> animes
//
//        and:
//        assert ObjectUtils.isNotEmpty(worstTenAnimes)
//        assert worstTenAnimes.size() == 10
//        assert isScoreAscOrdered(worstTenAnimes)
//    }
//
//    def "it should return ten anime ordered by score ASC when DB has more than 10 saved animes successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 15
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "asc")
//        def tenAnimes = animes.subList(0, 10)
//
//        when:
//        List<Anime> worstTenAnimes = animeService.getBadTen()
//
//        then:
//        1 * animeRepository.getBadTen() >> tenAnimes
//
//        and:
//        assert ObjectUtils.isNotEmpty(worstTenAnimes)
//        assert worstTenAnimes.size() == 10
//        assert isScoreAscOrdered(worstTenAnimes)
//    }
//
//    def "it should return all animes ordered by score ASC when DB has less than 10 saved animes successfully"() {
//        given:
//        def numberOfAnimesInDataBase = 6
//        def animes = createOrderedAnimeList(numberOfAnimesInDataBase, "ASC")
//
//        when:
//        List<Anime> worstTenAnimes = animeService.getBadTen()
//
//        then:
//        1 * animeRepository.getBadTen() >> animes
//
//        and:
//        assert ObjectUtils.isNotEmpty(worstTenAnimes)
//        assert worstTenAnimes.size() == numberOfAnimesInDataBase
//        assert isScoreAscOrdered(worstTenAnimes)
//    }
//
//    def "it should return empty bad list when DB has none saved animes successfully"() {
//        given:
//        def animes = Collections.emptyList()
//
//        when:
//        List<Anime> worstTenAnime = animeService.getBadTen()
//
//        then:
//        1 * animeRepository.getBadTen() >> animes
//
//        and:
//        assert ObjectUtils.isEmpty(worstTenAnime)
//    }
//
//    private static boolean isScoreDescOrdered(List<Anime> animes) {
//        boolean isOrdered = true
//        Anime position = animes.get(0)
//
//        for (Anime anime : animes) {
//            isOrdered = anime.getScore() <= position.getScore() && isOrdered
//            position = anime
//        }
//
//        return isOrdered
//    }
//
//    private static boolean isScoreAscOrdered(List<Anime> animes) {
//        boolean isOrdered = true
//        Anime position = animes.get(0)
//
//        for (Anime anime : animes) {
//            isOrdered = anime.getScore() >= position.getScore() && isOrdered
//            position = anime
//        }
//
//        return isOrdered
//    }
//}
