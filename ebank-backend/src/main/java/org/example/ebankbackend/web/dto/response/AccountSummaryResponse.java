package org.example.ebankbackend.web.dto.response;

import java.math.BigDecimal;

public class AccountSummaryResponse {

    private String rib;
    private BigDecimal balance;
    private String status;

    public AccountSummaryResponse() {}

    public AccountSummaryResponse(String rib, BigDecimal balance, String status) {
        this.rib = rib;
        this.balance = balance;
        this.status = status;
    }

    public String getRib() { return rib; }
    public void setRib(String rib) { this.rib = rib; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
