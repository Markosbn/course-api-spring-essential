package com.schulz.apispringessential.services;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.exceptions.BadRequestException;
import com.schulz.apispringessential.mappers.AnimeMapper;
import com.schulz.apispringessential.repositories.AnimeRepository;
import com.schulz.apispringessential.requests.AnimePostRequestBody;
import com.schulz.apispringessential.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(
                        () -> new BadRequestException("Anime not found"));
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(Long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(animePutRequestBody.getId());
        animeRepository.save(anime);
    }
}
