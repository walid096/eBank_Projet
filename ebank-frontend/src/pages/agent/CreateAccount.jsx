import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createAccount } from '../../api/agentApi';
import './CreateClient.css'; // reuse same form styles

// UC-3: Nouveau compte bancaire
// Matches AccountController.createAccount() endpoint
const CreateAccount = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    rib: '',
    identityNumber: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    // RG_9: Validate RIB format (24 digits)
    if (!/^\d{24}$/.test(formData.rib)) {
      setError('RIB invalide. Le RIB doit contenir exactement 24 chiffres.');
      setLoading(false);
      return;
    }

    try {
      // POST /api/v1/accounts
      // Request: CreateAccountRequest { rib, identityNumber }
      await createAccount({
        rib: formData.rib,
        identityNumber: formData.identityNumber,
      });

      setSuccess('Compte bancaire créé avec succès. Statut: Ouvert (RG_10)');
      setFormData({ rib: '', identityNumber: '' });
    } catch (err) {
      const errorMessage = err.response?.data || 'Erreur lors de la création du compte';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-page">
      <div className="form-shell">
        <h1 className="form-title">Nouveau compte bancaire</h1>
        <p className="form-subtitle">Créer un compte pour un client existant.</p>

        <div className="form-card">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="field">
                <label className="label">
                  RIB <span className="req">*</span>
                </label>

                {/* Visually smaller, banking-like input */}
                <input
                  className="input input-rib"
                  type="text"
                  value={formData.rib}
                  onChange={(e) =>
                    setFormData({ ...formData, rib: e.target.value.replace(/\D/g, '') })
                  }
                  required
                  maxLength={24}
                  placeholder="Ex: 123456789012345678901234"
                  inputMode="numeric"
                  autoComplete="off"
                />

                <div className="helper">RG_9: Doit être un RIB valide (24 chiffres)</div>
              </div>

              <div className="field">
                <label className="label">
                  Numéro d'identité du client <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="text"
                  value={formData.identityNumber}
                  onChange={(e) => setFormData({ ...formData, identityNumber: e.target.value })}
                  required
                />
                <div className="helper">
                  RG_8: Le numéro d'identité doit exister dans la base de données
                </div>
              </div>
            </div>

            {error && <div className="alert alert--error">{error}</div>}
            {success && <div className="alert alert--success">{success}</div>}

            <div className="form-actions">
              <button type="button" className="btn" onClick={() => navigate('/agent/dashboard')}>
                Retour
              </button>

              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Création...' : 'Créer compte'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateAccount;
