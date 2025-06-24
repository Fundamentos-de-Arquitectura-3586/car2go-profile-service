package com.pe.platform.profiles.interfaces.rest.resources;

public record CreateProfileResource(
        String firstName,
        String lastName,
        String email,
        String image,
        String dni,
        String address,
        String phone) { }
