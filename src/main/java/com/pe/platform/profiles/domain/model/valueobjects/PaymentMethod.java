package com.pe.platform.profiles.domain.model.valueobjects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Payment Method", example = "BBVA")
    private String type;

    @Schema(description = "Account Number", example = "12345678912345678912")
    private String details;

    public PaymentMethod(String type, String details) {
        this.type = type;
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
