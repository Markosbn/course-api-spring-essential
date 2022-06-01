package com.schulz.apispringessential.services;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.exceptions.BadRequestException;
import com.schulz.apispringessential.repositories.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.schulz.apispringessential.utils.AnimeCreator.createValidAnime;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(createValidAnime()));
        List<Anime> animeList = List.of(createValidAnime());
        Anime anime = createValidAnime();

        BDDMockito.when(animeRepository.findAll())
                .thenReturn(animeList);

        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(createValidAnime()));

        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeRepository.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(anime);

        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    void listAll_ReturnsListOfAnimesInsidePageObject_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage)
                .isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void listAllNonPageable_ReturnsListOfAnimes_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void findByIdOrThrowBadRequestException_ReturnsAnime_WheSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();
        Anime anime = animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong());

        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId())
                .isEqualTo(expectedId);
    }

    @Test
    void findByIdOrThrowBadRequestException_ThrowBadRequestException_WheAnimeIsNotFound(){
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()));


    }

    @Test
    void findByName_ReturnsListOfAnime_WheSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.findByName(ArgumentMatchers.anyString());

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void findByName_ReturnsEmptyListOfAnime_WheAnimeIsNotFound(){
        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName(ArgumentMatchers.anyString());

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void save_ReturnsAnime_WheSuccessful(){
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBodyToBeSaved());

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }
    @Test
    void replace_UpdateAnime_WheSuccessful(){
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBodyToBeSaved()))
                .doesNotThrowAnyException();
    }

    @Test
    void delete_UpdateAnime_WheSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();
    }
}