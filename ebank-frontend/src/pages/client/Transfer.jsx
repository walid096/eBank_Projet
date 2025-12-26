import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyAccounts, createTransfer } from '../../api/clientApi';

// UC-5: Nouveau virement
// Matches TransferController.createTransfer() endpoint
const Transfer = () => {
    const [accounts, setAccounts] = useState([]);
    const [formData, setFormData] = useState({
        sourceRib: '',
        destinationRib: '',
        amount: '',
        motif: '',
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        loadAccounts();
    }, []);

    const loadAccounts = async () => {
        try {
            // GET /api/v1/accounts/me
            const accountsData = await getMyAccounts();
            setAccounts(accountsData);

            // Set default source RIB (first account or only account)
            if (accountsData.length > 0) {
                setFormData((prev) => ({ ...prev, sourceRib: accountsData[0].rib }));
            }
        } catch (err) {
            setError('Erreur lors du chargement des comptes');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setLoading(true);

        try {
            // POST /api/v1/transfers
            // Request: TransferRequest { sourceRib, destinationRib, amount, motif }
            await createTransfer({
                sourceRib: formData.sourceRib,
                destinationRib: formData.destinationRib,
                amount: parseFloat(formData.amount),
                motif: formData.motif,
            });

            setSuccess('Virement effectué avec succès');
            setTimeout(() => {
                navigate('/client/dashboard');
            }, 2000);
        } catch (err) {
            // Error messages match backend exactly (TransferServiceImpl)
            const errorMessage = err.response?.data || 'Erreur lors du virement';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    const isSingleAccount = accounts.length === 1;

    return (
        <div style={{ maxWidth: '600px', margin: '50px auto', padding: '20px' }}>
            <h1>Nouveau virement</h1>
            <form onSubmit={handleSubmit}>
                {/* Source RIB - displayed by default, grayed if single account */}
                <div style={{ marginBottom: '15px' }}>
                    <label>RIB source:</label>
                    {isSingleAccount ? (
                        <input
                            type="text"
                            value={formData.sourceRib}
                            disabled
                            style={{
                                width: '100%',
                                padding: '8px',
                                marginTop: '5px',
                                backgroundColor: '#e9ecef',
                                color: '#6c757d',
                            }}
                        />
                    ) : (
                        <select
                            value={formData.sourceRib}
                            onChange={(e) => setFormData({ ...formData, sourceRib: e.target.value })}
                            required
                            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                        >
                            <option value="">Sélectionner un compte</option>
                            {accounts.map((account) => (
                                <option key={account.rib} value={account.rib}>
                                    {account.rib}
                                </option>
                            ))}
                        </select>
                    )}
                </div>

                {/* Destination RIB */}
                <div style={{ marginBottom: '15px' }}>
                    <label>RIB destinataire:</label>
                    <input
                        type="text"
                        value={formData.destinationRib}
                        onChange={(e) => setFormData({ ...formData, destinationRib: e.target.value })}
                        required
                        placeholder="24 chiffres"
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>

                {/* Amount */}
                <div style={{ marginBottom: '15px' }}>
                    <label>Montant:</label>
                    <input
                        type="number"
                        step="0.01"
                        min="0.01"
                        value={formData.amount}
                        onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                        required
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>

                {/* Motif */}
                <div style={{ marginBottom: '15px' }}>
                    <label>Motif:</label>
                    <input
                        type="text"
                        value={formData.motif}
                        onChange={(e) => setFormData({ ...formData, motif: e.target.value })}
                        required
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>

                {error && (
                    <div style={{ color: 'red', marginBottom: '15px' }}>{error}</div>
                )}
                {success && (
                    <div style={{ color: 'green', marginBottom: '15px' }}>{success}</div>
                )}

                <div style={{ display: 'flex', gap: '10px' }}>
                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            flex: 1,
                            padding: '10px',
                            backgroundColor: '#007bff',
                            color: 'white',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                    >
                        {loading ? 'Traitement...' : 'Valider'}
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate('/client/dashboard')}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#6c757d',
                            color: 'white',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                    >
                        Annuler
                    </button>
                </div>
            </form>
        </div>
    );
};

export default Transfer;