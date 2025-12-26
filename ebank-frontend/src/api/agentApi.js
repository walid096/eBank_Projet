import httpClient from './httpClient';

// Matches ClientController and AccountController (AGENT_GUICHET endpoints)

// POST /api/v1/clients
// Request: CreateClientRequest { firstName, lastName, identityNumber, birthDate, email, postalAddress }
// Response: 201 Created
export const createClient = async (clientData) => {
    await httpClient.post('/api/v1/clients', clientData);
    // Returns nothing (201 Created)
};

// POST /api/v1/accounts
// Request: CreateAccountRequest { rib: string, identityNumber: string }
// Response: 201 Created
export const createAccount = async (accountData) => {
    await httpClient.post('/api/v1/accounts', accountData);
    // Returns nothing (201 Created)
};