package org.example.ebankbackend.web.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class AccountDashboardResponse {

    private String rib;
    private BigDecimal balance;
    private List<OperationLineResponse> operations; // last 10

    public AccountDashboardResponse(String rib, BigDecimal balance, List<OperationLineResponse> operations) {
        this.rib = rib;
        this.balance = balance;
        this.operations = operations;
    }

    public String getRib() { return rib; }
    public BigDecimal getBalance() { return balance; }
    public List<OperationLineResponse> getOperations() { return operations; }
}
