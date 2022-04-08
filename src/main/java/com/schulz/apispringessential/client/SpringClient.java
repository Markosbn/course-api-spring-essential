package com.schulz.apispringessential.client;

import com.schulz.apispringessential.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {

    ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8090/animes/{id}", Anime.class, 2);

    Anime object = new RestTemplate().getForObject("http://localhost:8090/animes/{id}", Anime.class, 2);

    Anime[] animes = new RestTemplate().getForObject("http://localhost:8090/animes/all", Anime[].class);

    //@formatter:off
    ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8090/animes/all",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    //@formatter:on

    Anime kingdon = Anime.builder().name("kingdon").build();
    Anime kingdonSaved = new RestTemplate().postForObject("http://localhost:8090/animes/", kingdon, Anime.class );

    Anime samurai = Anime.builder().name("samurai").build();
    ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange("http://localhost:8090/animes/",
            HttpMethod.POST,
            new HttpEntity<>(samurai),
            Anime.class );

    private static HttpHeaders creatJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
