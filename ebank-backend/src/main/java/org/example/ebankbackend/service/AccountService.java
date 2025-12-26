package org.example.ebankbackend.service;

import org.example.ebankbackend.web.dto.request.CreateAccountRequest;
import org.example.ebankbackend.web.dto.response.AccountSummaryResponse;
import org.example.ebankbackend.web.dto.response.AccountDashboardResponse;
import java.util.List;
import org.example.ebankbackend.web.dto.response.OperationPageResponse;

public interface AccountService {

    void createAccount(CreateAccountRequest request);
    List<AccountSummaryResponse> getMyAccounts(String login);
    AccountDashboardResponse getMyDashboard(String login, String rib);
    OperationPageResponse getMyOperations(String login, String rib, int page, int size);


}
