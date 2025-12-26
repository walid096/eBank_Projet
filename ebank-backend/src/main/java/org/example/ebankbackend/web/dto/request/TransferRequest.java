package org.example.ebankbackend.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Champs virement obligatoires manquants")
    private String sourceRib;        // shown by default + grayed on frontend

    @NotBlank(message = "Champs virement obligatoires manquants")
    private String destinationRib;

    @NotNull(message = "Champs virement obligatoires manquants")
    @DecimalMin(value = "0.01", message = "Montant invalide")
    private BigDecimal amount;

    @NotBlank(message = "Champs virement obligatoires manquants")
    private String motif;

    public TransferRequest() {}

    public String getSourceRib() { return sourceRib; }
    public void setSourceRib(String sourceRib) { this.sourceRib = sourceRib; }

    public String getDestinationRib() { return destinationRib; }
    public void setDestinationRib(String destinationRib) { this.destinationRib = destinationRib; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
