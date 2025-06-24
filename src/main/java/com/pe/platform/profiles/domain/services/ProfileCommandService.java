package com.pe.platform.profiles.domain.services;

import com.pe.platform.profiles.domain.model.aggregates.Profile;
import com.pe.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.pe.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.pe.platform.profiles.domain.model.valueobjects.PaymentMethod;

import java.util.Optional;

public interface ProfileCommandService {
    Optional<Profile> handle(CreateProfileCommand command);

    Optional<Profile> handle(UpdateProfileCommand command);

    Optional<Profile> addPaymentMethod(long profileId, PaymentMethod paymentMethod);

    Optional<Profile> removePaymentMethod(long profileId, PaymentMethod paymentMethod);

    Optional<Profile> save(Profile profile);

    Optional<Profile> getProfileById(long profileId); // Nuevo método
}
