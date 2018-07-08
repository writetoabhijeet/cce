package com.tiaa.cce.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by USER on 08-07-2018.
 */
@Data
public class CreditCard {

    @NotNull
    @NotEmpty
    private final String creditCardNumber;

    @NotEmpty
    private final String expiryDate;

    public CreditCard(String creditCardNumber, String expiryDate) {
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
    }
}
