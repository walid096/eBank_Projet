import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createClient } from '../../api/agentApi';
import './CreateClient.css';

// UC-2: Ajouter un nouveau client
// Matches ClientController.createClient() endpoint
const CreateClient = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    identityNumber: '',
    birthDate: '',
    email: '',
    postalAddress: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      await createClient({
        firstName: formData.firstName,
        lastName: formData.lastName,
        identityNumber: formData.identityNumber,
        birthDate: formData.birthDate, // YYYY-MM-DD
        email: formData.email,
        postalAddress: formData.postalAddress,
      });

      setSuccess('Client créé avec succès. Un email a été envoyé avec les identifiants.');
      setFormData({
        firstName: '',
        lastName: '',
        identityNumber: '',
        birthDate: '',
        email: '',
        postalAddress: '',
      });
    } catch (err) {
      const errorMessage = err.response?.data || 'Erreur lors de la création du client';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-page">
      <div className="form-shell">
        <h1 className="form-title">Ajouter un nouveau client</h1>
        <p className="form-subtitle">Renseignez les informations obligatoires.</p>

        <div className="form-card">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              {/* First + last name on same row (desktop) */}
              <div className="field half">
                <label className="label">
                  Prénom <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="text"
                  value={formData.firstName}
                  onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  required
                  autoComplete="given-name"
                />
              </div>

              <div className="field half">
                <label className="label">
                  Nom <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="text"
                  value={formData.lastName}
                  onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  required
                  autoComplete="family-name"
                />
              </div>

              <div className="field">
                <label className="label">
                  Numéro d'identité <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="text"
                  value={formData.identityNumber}
                  onChange={(e) => setFormData({ ...formData, identityNumber: e.target.value })}
                  required
                />
                <div className="helper">RG_4: Doit être unique</div>
              </div>

              <div className="field half">
                <label className="label">
                  Date de naissance <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="date"
                  value={formData.birthDate}
                  onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
                  required
                />
              </div>

              <div className="field half">
                <label className="label">
                  Email <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  required
                  autoComplete="email"
                />
                <div className="helper">
                  RG_6: Doit être unique. RG_7: Un email sera envoyé avec login et mot de passe
                </div>
              </div>

              <div className="field">
                <label className="label">
                  Adresse postale <span className="req">*</span>
                </label>
                <textarea
                  className="textarea"
                  value={formData.postalAddress}
                  onChange={(e) => setFormData({ ...formData, postalAddress: e.target.value })}
                  required
                  rows={3}
                />
              </div>
            </div>

            {error && <div className="alert alert--error">{error}</div>}
            {success && <div className="alert alert--success">{success}</div>}

            <div className="form-actions">
              <button type="button" className="btn" onClick={() => navigate('/agent/dashboard')}>
                Retour
              </button>

              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Création...' : 'Créer client'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateClient;
