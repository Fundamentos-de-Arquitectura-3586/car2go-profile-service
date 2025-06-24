package com.pe.platform.profiles.interfaces.rest.resources;

import com.pe.platform.profiles.domain.model.valueobjects.PaymentMethod;
import java.util.List;

public record ProfileResource(
        Long id,
        String firstName,
        String lastName,
        String email,
        String image,
        String dni,
        String address,
        String phone,
        Long profileId,
        List<PaymentMethod> paymentMethods
) { }
