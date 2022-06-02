package com.schulz.apispringessential.integration;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.repositories.AnimeRepository;
import com.schulz.apispringessential.requests.AnimePostRequestBody;
import com.schulz.apispringessential.utils.AnimeCreator;
import com.schulz.apispringessential.utils.AnimePostRequestBodyCreator;
import com.schulz.apispringessential.utils.AnimePutRequestBodyCreator;
import com.schulz.apispringessential.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.schulz.apispringessential.utils.AnimeCreator.createAnimeToBeSaved;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;
    //pega porta local do projeto
    @LocalServerPort
    private int port;

    @MockBean
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    public void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidAnime()))
                .thenReturn(AnimeCreator.createValidUpdateAnime());
    }

    @Test
    public void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();


        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();


        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    void listAll_ReturnsListOfAnimes_WheSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    void findById_ReturnsAnime_WheSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);
        Long expectedId = savedAnime.getId();
        Anime anime =  testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId())
                .isEqualTo(expectedId);
    }

    @Test
    void findByName_ReturnsListOfAnime_WheSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    void findByName_ReturnsEmptyListOfAnime_WheAnimeIsNotFound(){
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void save_ReturnsAnime_WheSuccessful(){
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBodyToBeSaved();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity)
                .isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    void replace_UpdateAnime_WheSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        savedAnime.setName("new name");
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes", HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(animeResponseEntity)
                .isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void delete_UpdateAnime_WheSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE, null,
                Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity)
                .isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }
}
