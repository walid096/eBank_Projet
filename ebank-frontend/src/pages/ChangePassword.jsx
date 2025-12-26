import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { changePassword as changePasswordApi } from '../api/authApi';
import './agent/CreateClient.css'; // reuse the same form UI styles you already use

// Matches AuthController.changePassword() endpoint
const ChangePassword = () => {
  const [formData, setFormData] = useState({ oldPassword: '', newPassword: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      // PUT /api/v1/auth/change-password
      // Request: { oldPassword, newPassword } (matches ChangePasswordRequest)
      await changePasswordApi(formData);

      setSuccess('Mot de passe modifié avec succès');
      setTimeout(() => {
        navigate(-1); // Go back to previous page
      }, 2000);
    } catch (err) {
      // Error message matches backend (AuthServiceImpl.BAD_CREDENTIALS_MESSAGE)
      const errorMessage = err.response?.data || 'Login ou mot de passe erronés';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-page">
      <div className="form-shell">
        <h1 className="form-title">Changer le mot de passe</h1>
        <p className="form-subtitle">Mettez à jour votre mot de passe en toute sécurité.</p>

        <div className="form-card">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="field">
                <label className="label">
                  Ancien mot de passe <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="password"
                  value={formData.oldPassword}
                  onChange={(e) => setFormData({ ...formData, oldPassword: e.target.value })}
                  required
                  autoComplete="current-password"
                />
              </div>

              <div className="field">
                <label className="label">
                  Nouveau mot de passe <span className="req">*</span>
                </label>
                <input
                  className="input"
                  type="password"
                  value={formData.newPassword}
                  onChange={(e) => setFormData({ ...formData, newPassword: e.target.value })}
                  required
                  autoComplete="new-password"
                />
              </div>
            </div>

            {error && <div className="alert alert--error">{error}</div>}
            {success && <div className="alert alert--success">{success}</div>}

            <div className="form-actions">
              <button type="button" className="btn" onClick={() => navigate(-1)}>
                Retour
              </button>

              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Modification...' : 'Modifier'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
