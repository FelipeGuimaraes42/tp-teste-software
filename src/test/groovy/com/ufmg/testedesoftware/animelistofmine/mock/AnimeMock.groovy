package com.ufmg.testedesoftware.animelistofmine.mock

import com.ufmg.testedesoftware.animelistofmine.domain.Anime

import java.util.stream.IntStream

class AnimeMock {
    static Anime createAnime(Long id = 1L, String name = "Dragon Ball Z", Double score = 9) {
        return new Anime(id, name, score)
    }

    static List<Anime> createOrderedAnimeList(int numberOfAnimes, String order) {
        def animes = new ArrayList()
        IntStream.range(0, numberOfAnimes).forEach(index -> {
            Double score = index % 9 + 1.5
            def anime = new Anime(index, "Anime " + index.toString(), score)
            animes.add(anime)
        })
        if (order.toLowerCase() == "asc") {
            return animes.sort((Anime a, Anime b) -> a.getScore() <=> b.getScore()) as List<Anime>
        }
        return animes.sort((Anime a, Anime b) -> b.getScore() <=> a.getScore()) as List<Anime>
    }

}
