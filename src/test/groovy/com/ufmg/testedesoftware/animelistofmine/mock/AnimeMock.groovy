package com.ufmg.testedesoftware.animelistofmine.mock

import com.ufmg.testedesoftware.animelistofmine.domain.Anime

class AnimeMock {
    static createAnime(Long id = 1L, String name = "Dragon Ball Z", Double score = 9) {
        return new Anime(id, name, score)
    }
}
