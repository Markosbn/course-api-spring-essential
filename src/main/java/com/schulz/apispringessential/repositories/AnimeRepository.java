package com.schulz.apispringessential.repositories;

import com.schulz.apispringessential.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
