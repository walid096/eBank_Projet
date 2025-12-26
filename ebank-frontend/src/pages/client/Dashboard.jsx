import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyAccounts, getMyDashboard, getMyOperations } from '../../api/clientApi';

// UC-4: Consulter Tableau de bord
// Matches AccountController endpoints
const Dashboard = () => {
    const [accounts, setAccounts] = useState([]);
    const [selectedRib, setSelectedRib] = useState(null);
    const [dashboard, setDashboard] = useState(null);
    const [operations, setOperations] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Load accounts list
    useEffect(() => {
        loadAccounts();
    }, []);

    // Load dashboard when account is selected
    useEffect(() => {
        if (selectedRib) {
            loadDashboard(selectedRib);
            loadOperations(selectedRib, 0);
        }
    }, [selectedRib]);

    const loadAccounts = async () => {
        try {
            // GET /api/v1/accounts/me
            // Response: AccountSummaryResponse[] { rib, balance, status }
            const accountsData = await getMyAccounts();
            setAccounts(accountsData);

            // If accounts exist, select first one (backend handles "most recently moved" logic)
            if (accountsData.length > 0) {
                setSelectedRib(accountsData[0].rib);
            }
        } catch (err) {
            setError(err.response?.data || 'Erreur lors du chargement des comptes');
        } finally {
            setLoading(false);
        }
    };

    const loadDashboard = async (rib) => {
        try {
            // GET /api/v1/accounts/me/dashboard?rib=...
            // Response: AccountDashboardResponse { rib, balance, operations: OperationLineResponse[] }
            const dashboardData = await getMyDashboard(rib);
            setDashboard(dashboardData);
            setOperations(dashboardData.operations || []); // Last 10 operations
        } catch (err) {
            setError(err.response?.data || 'Erreur lors du chargement du tableau de bord');
        }
    };

    const loadOperations = async (rib, page) => {
        try {
            // GET /api/v1/accounts/me/operations?rib=...&page=...&size=10
            // Response: OperationPageResponse { items, page, size, totalElements, totalPages, last }
            const operationsData = await getMyOperations(rib, page, 10);
            setOperations(operationsData.items);
            setPagination(operationsData);
            setCurrentPage(page);
        } catch (err) {
            setError(err.response?.data || 'Erreur lors du chargement des opérations');
        }
    };

    const handleAccountChange = (e) => {
        const rib = e.target.value;
        setSelectedRib(rib);
    };

    const handlePageChange = (newPage) => {
        if (selectedRib) {
            loadOperations(selectedRib, newPage);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString('fr-FR');
    };

    const formatAmount = (amount) => {
        return new Intl.NumberFormat('fr-FR', {
            style: 'currency',
            currency: 'EUR',
        }).format(amount);
    };

    if (loading) {
        return <div style={{ padding: '50px', textAlign: 'center' }}>Chargement...</div>;
    }

    return (
        <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
            <h1>Tableau de bord</h1>

            {/* Account selector dropdown */}
            {accounts.length > 0 && (
                <div style={{ marginBottom: '20px' }}>
                    <label>Compte bancaire: </label>
                    <select
                        value={selectedRib || ''}
                        onChange={handleAccountChange}
                        style={{ padding: '8px', fontSize: '16px', minWidth: '300px' }}
                    >
                        {accounts.map((account) => (
                            <option key={account.rib} value={account.rib}>
                                {account.rib} - Solde: {formatAmount(account.balance)}
                            </option>
                        ))}
                    </select>
                </div>
            )}

            {error && (
                <div style={{ color: 'red', marginBottom: '20px' }}>{error}</div>
            )}

            {dashboard && (
                <>
                    {/* Account info */}
                    <div style={{ marginBottom: '30px', padding: '20px', backgroundColor: '#f5f5f5', borderRadius: '5px' }}>
                        <h2>Informations du compte</h2>
                        <p><strong>RIB:</strong> {dashboard.rib}</p>
                        <p><strong>Solde:</strong> {formatAmount(dashboard.balance)}</p>
                    </div>

                    {/* Last 10 operations */}
                    <div style={{ marginBottom: '30px' }}>
                        <h2>Dernières opérations</h2>
                        {operations.length === 0 ? (
                            <p>Aucune opération</p>
                        ) : (
                            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                                <thead>
                                    <tr style={{ backgroundColor: '#f0f0f0' }}>
                                        <th style={{ padding: '10px', border: '1px solid #ddd', textAlign: 'left' }}>Intitulé</th>
                                        <th style={{ padding: '10px', border: '1px solid #ddd', textAlign: 'left' }}>Type</th>
                                        <th style={{ padding: '10px', border: '1px solid #ddd', textAlign: 'left' }}>Date</th>
                                        <th style={{ padding: '10px', border: '1px solid #ddd', textAlign: 'right' }}>Montant</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {operations.map((op, index) => (
                                        <tr key={index}>
                                            <td style={{ padding: '10px', border: '1px solid #ddd' }}>{op.label}</td>
                                            <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                                                <span style={{ color: op.type === 'DEBIT' ? 'red' : 'green' }}>
                                                    {op.type === 'DEBIT' ? 'Débit' : 'Crédit'}
                                                </span>
                                            </td>
                                            <td style={{ padding: '10px', border: '1px solid #ddd' }}>{formatDate(op.date)}</td>
                                            <td style={{ padding: '10px', border: '1px solid #ddd', textAlign: 'right' }}>
                                                {formatAmount(op.amount)}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}

                        {/* Pagination */}
                        {pagination && pagination.totalPages > 1 && (
                            <div style={{ marginTop: '20px', textAlign: 'center' }}>
                                <button
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    disabled={currentPage === 0}
                                    style={{ margin: '0 5px', padding: '8px 15px' }}
                                >
                                    Précédent
                                </button>
                                <span style={{ margin: '0 10px' }}>
                                    Page {currentPage + 1} sur {pagination.totalPages}
                                </span>
                                <button
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    disabled={pagination.last}
                                    style={{ margin: '0 5px', padding: '8px 15px' }}
                                >
                                    Suivant
                                </button>
                            </div>
                        )}
                    </div>

                    {/* Nouveau virement button */}
                    <button
                        onClick={() => navigate('/client/transfer')}
                        style={{
                            padding: '12px 24px',
                            backgroundColor: '#28a745',
                            color: 'white',
                            border: 'none',
                            cursor: 'pointer',
                            fontSize: '16px',
                        }}
                    >
                        Nouveau virement
                    </button>
                </>
            )}
        </div>
    );
};

export default Dashboard;