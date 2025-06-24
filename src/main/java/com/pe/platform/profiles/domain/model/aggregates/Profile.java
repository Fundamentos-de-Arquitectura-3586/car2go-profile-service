package com.pe.platform.profiles.domain.model.aggregates;

import com.pe.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.pe.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.pe.platform.profiles.domain.model.valueobjects.PaymentMethod;
import com.pe.platform.profiles.domain.model.valueobjects.PersonName;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private PersonName name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String image;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Long profileId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    private static final int MAX_PAYMENT_METHODS = 3;

    protected Profile() { }

    public Profile(CreateProfileCommand command, Long profileId) {
        this.name = new PersonName(command.firstName(), command.lastName());
        this.email = command.email();
        this.image = command.image();
        this.dni = command.dni();
        this.address = command.address();
        this.phone = command.phone();
        this.profileId = profileId;
    }

    public Profile(UpdateProfileCommand command) {
        this.name = new PersonName(command.firstName(), command.lastName());
        this.email = command.email();
        this.image = command.image();
        this.dni = command.dni();
        this.address = command.address();
        this.phone = command.phone();
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getProfileId() {
        return profileId;
    }

    public String getFirstName() {
        return name.getFirstName();
    }

    public String getLastName() {
        return name.getLastName();
    }

    public void updateName(String firstName, String lastName) {
        this.name = new PersonName(firstName, lastName);
    }


    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethods.size() >= MAX_PAYMENT_METHODS) {
            throw new IllegalArgumentException("Cannot add more than " + MAX_PAYMENT_METHODS + " payment methods");
        }
        paymentMethods.add(paymentMethod);
    }

    public boolean removePaymentMethodById(Long paymentMethodId) {
        return paymentMethods.removeIf(paymentMethod -> paymentMethod.getId().equals(paymentMethodId));
    }

    public boolean updatePaymentMethod(Long paymentMethodId, PaymentMethod updatedPaymentMethod) {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals(paymentMethodId)) {
                paymentMethod.setType(updatedPaymentMethod.getType());
                paymentMethod.setDetails(updatedPaymentMethod.getDetails());
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }
}
