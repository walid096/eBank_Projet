package org.example.ebankbackend.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationLineResponse {
    private String label;
    private String type; // DEBIT/CREDIT (or your enum name)
    private LocalDateTime date;
    private BigDecimal amount;

    public OperationLineResponse() {}

    public OperationLineResponse(String label, String type, LocalDateTime date, BigDecimal amount) {
        this.label = label;
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    public String getLabel() { return label; }
    public String getType() { return type; }
    public LocalDateTime getDate() { return date; }
    public BigDecimal getAmount() { return amount; }
}
