package com.pe.platform.profiles.interfaces.rest.transform;

import com.pe.platform.profiles.domain.model.aggregates.Profile;
import com.pe.platform.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
                (long) entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getImage(),
                entity.getDni(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getProfileId(),
                entity.getPaymentMethods()
        );
    }
}
