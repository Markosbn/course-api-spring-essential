package com.schulz.apispringessential.utils;

import com.schulz.apispringessential.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Naruto")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1L)
                .name("Naruto")
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .id(1L)
                .name("DBZ")
                .build();
    }
}
