package org.example.ebankbackend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateAccountRequest {

    @NotBlank(message = "Champs compte obligatoires manquants")
    private String rib;

    @NotBlank(message = "Champs compte obligatoires manquants")
    private String identityNumber;

    public CreateAccountRequest() {}

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
}
