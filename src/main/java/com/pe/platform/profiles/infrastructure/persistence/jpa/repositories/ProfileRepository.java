package com.pe.platform.profiles.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pe.platform.profiles.domain.model.aggregates.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {    Optional<Profile> findByEmail(String email);

    Optional<Profile> findByProfileId(Long profileId);


    default boolean canAddPaymentMethod(Profile profile) {
        return profile.getPaymentMethods().size() < 3;
    }
}
