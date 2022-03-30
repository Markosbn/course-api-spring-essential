package com.schulz.apispringessential.mappers;

import com.schulz.apispringessential.domain.Anime;
import com.schulz.apispringessential.requests.AnimePostRequestBody;
import com.schulz.apispringessential.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
