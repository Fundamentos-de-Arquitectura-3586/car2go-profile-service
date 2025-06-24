package com.pe.platform.profiles.interfaces.rest.transform;

import com.pe.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.pe.platform.profiles.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResource {
    public static UpdateProfileCommand toCommandFromResource(UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.image(),
                resource.dni(),
                resource.address(),
                resource.phone()
        );
    }
}
