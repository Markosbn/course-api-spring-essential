package com.schulz.apispringessential.repositories;

import com.schulz.apispringessential.domain.SchulzUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchulzUserRepository extends JpaRepository<SchulzUser, Long> {

    SchulzUser findSchulzUserByUsername(String username);
}
