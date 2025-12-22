package org.example.ebankbackend.domain.entity;

import jakarta.persistence.*;
import org.example.ebankbackend.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "operations",
        indexes = {
                @Index(
                        name = "idx_operations_account_date",
                        columnList = "account_id, operation_datetime"
                )
        }
)
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UC-4: intitulé de l’opération
    @Column(name = "label", nullable = false)
    private String label;

    // UC-4: Débit ou Crédit
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OperationType type;

    // RG-15: date précise de l’opération
    @Column(name = "operation_datetime", nullable = false)
    private LocalDateTime operationDateTime;

    // UC-4: montant
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // UC-5: motif du virement
    @Column(name = "motif")
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_operations_account")
    )
    private Account account;

    public Operation() {}

    public Long getId() { return id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }

    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public void setOperationDateTime(LocalDateTime operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
