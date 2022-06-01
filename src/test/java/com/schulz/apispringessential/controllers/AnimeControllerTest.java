package com.schulz.apispringessential.controllers;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.requests.AnimePostRequestBody;
import com.schulz.apispringessential.requests.AnimePutRequestBody;
import com.schulz.apispringessential.services.AnimeService;
import com.schulz.apispringessential.utils.AnimeCreator;
import com.schulz.apispringessential.utils.AnimePostRequestBodyCreator;
import com.schulz.apispringessential.utils.AnimePutRequestBodyCreator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .thenReturn(anime);

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
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

    @Test
    void save_ReturnsAnime_WheSuccessful(){
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBodyToBeSaved()).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }
    @Test
    void replace_UpdateAnime_WheSuccessful(){
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBodyToBeSaved()))
                        .doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBodyToBeSaved());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void delete_UpdateAnime_WheSuccessful(){
        Assertions.assertThatCode(() -> animeController.delete(1L))
                        .doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}