package com.schulz.apispringessential.repositories;

import com.schulz.apispringessential.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Optional;

import static com.schulz.apispringessential.utils.AnimeCreator.createAnimeToBeSaved;

@DataJpaTest
@DisplayName("Tests for anime repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("save creates anime when successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(savedAnime)
                .isNotNull();
        Assertions.assertThat(savedAnime.getId())
                .isNotNull();
        Assertions.assertThat(savedAnime.getName())
                .isEqualTo(animeToBeSaved.getName());


    }

    @Test
    @DisplayName("save updates anime when successful")
    void save_UpdatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        savedAnime.setName("DBZ");

        Anime animeUpdated = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeUpdated)
                .isNotNull();
        Assertions.assertThat(animeUpdated.getId())
                .isNotNull();
        Assertions.assertThat(animeUpdated.getName())
                .isEqualTo(savedAnime.getName());


    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOpt = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOpt).isEmpty();
    }

    @Test
    @DisplayName("finbyname return list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        String name = savedAnime.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(savedAnime);
    }

    @Test
    @DisplayName("finbyname return empty list of anime when no anime is found")
    void findByName_EmptyReturnListOfAnime_WhenAnimeIsNotFound(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        this.animeRepository.save(animeToBeSaved);

        List<Anime> animes = this.animeRepository.findByName("name");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();

//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");
    }

}