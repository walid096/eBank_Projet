import httpClient from './httpClient';

// Matches AccountController and TransferController (CLIENT endpoints)

// GET /api/v1/accounts/me
// Response: AccountSummaryResponse[] { rib: string, balance: number, status: string }
export const getMyAccounts = async () => {
    const response = await httpClient.get('/api/v1/accounts/me');
    return response.data; // Array of AccountSummaryResponse
};

// GET /api/v1/accounts/me/dashboard?rib=...
// Response: AccountDashboardResponse { rib: string, balance: number, operations: OperationLineResponse[] }
export const getMyDashboard = async (rib = null) => {
    const params = rib ? { rib } : {};
    const response = await httpClient.get('/api/v1/accounts/me/dashboard', { params });
    return response.data; // AccountDashboardResponse
};

// GET /api/v1/accounts/me/operations?rib=...&page=...&size=...
// Response: OperationPageResponse { items: OperationLineResponse[], page: number, size: number, totalElements: number, totalPages: number, last: boolean }
export const getMyOperations = async (rib, page = 0, size = 10) => {
    const response = await httpClient.get('/api/v1/accounts/me/operations', {
        params: { rib, page, size },
    });
    return response.data; // OperationPageResponse
};

// POST /api/v1/transfers
// Request: TransferRequest { sourceRib: string, destinationRib: string, amount: number, motif: string }
// Response: 201 Created
export const createTransfer = async (transferData) => {
    await httpClient.post('/api/v1/transfers', transferData);
    // Returns nothing (201 Created)
};