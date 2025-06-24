package com.pe.platform.profiles.domain.model.commands;

public record CreateMechanicCommand(String firstName,
                                    String lastName,
                                    String phoneNumber,
                                    String street,
                                    String address,
                                    String password) {
    public CreateMechanicCommand {
        validateNonEmpty(firstName, "First name");
        validateNonEmpty(lastName, "Last name");
        validateNonEmpty(phoneNumber, "Phone number");
        validateNonEmpty(street, "Street");
        validateNonEmpty(address, "Address");
        validateNonEmpty(password, "Password");
    }

    private static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}
