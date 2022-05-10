package com.schulz.apispringessential.controllers;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.requests.AnimePostRequestBody;
import com.schulz.apispringessential.services.AnimeService;
import com.schulz.apispringessential.utils.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static com.schulz.apispringessential.utils.AnimeCreator.createValidAnime;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(createValidAnime()));
        List<Anime> animeList = List.of(createValidAnime());
        Anime anime = createValidAnime();

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(anime);

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(animeList);
    }

    @Test
    void list_ReturnsListOfAnimesInsidePageObject_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage)
                .isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void listAll_ReturnsListOfAnimes_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void findById_ReturnsAnime_WheSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();
        Anime anime = animeController.findById(ArgumentMatchers.anyLong()).getBody();

        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId())
                .isEqualTo(expectedId);
    }

    @Test
    void findByName_ReturnsListOfAnime_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.findByName(ArgumentMatchers.anyString()).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void findByName_ReturnsEmptyListOfAnime_WheAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName(ArgumentMatchers.anyString()).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

}