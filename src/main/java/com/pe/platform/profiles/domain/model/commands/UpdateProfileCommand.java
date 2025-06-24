package com.pe.platform.profiles.domain.model.commands;

public record UpdateProfileCommand(String firstName,
                                   String lastName,
                                   String email,
                                   String image,
                                   String dni,
                                   String address,
                                   String phone) {

    public UpdateProfileCommand {
        validateNonEmpty(firstName, "First name");
        validateNonEmpty(lastName, "Last name");
        validateNonEmpty(email, "Email");
        validateNonEmpty(dni, "DNI");
        validateNonEmpty(address, "Address");
        validateNonEmpty(image, "Image");
        validateNonEmpty(phone, "Phone number");
    }

    private static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}
