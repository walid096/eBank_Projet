import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import { login as loginApi } from '../api/authApi';
import './Login.css';
import bg from "../assets/login-bg.jpg";

const Login = () => {
  const [formData, setFormData] = useState({ login: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await loginApi(formData);
      login(response.token, response.role);

      if (response.role === 'CLIENT') navigate('/client/dashboard');
      else if (response.role === 'AGENT_GUICHET') navigate('/agent/dashboard');
    } catch (err) {
      const errorMessage = err.response?.data || 'Login ou mot de passe erronés';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
<div
  className="login-page"
  style={{
    backgroundImage: `
      linear-gradient(rgba(255,255,255,0.65), rgba(255,255,255,0.65)),
      url(${bg})
    `
  }}
>
      <div className="login-card">
        <div className="login-card__header">
          <h1 className="login-title">Login</h1>
        </div>

        <div className="login-card__body">
          <form onSubmit={handleSubmit}>
            <div className="login-field">
              <label className="login-label">Nom d’utilisateur</label>
              <input
                className="login-input"
                type="text"
                value={formData.login}
                onChange={(e) => setFormData({ ...formData, login: e.target.value })}
                required
                autoComplete="username"
              />
            </div>

            <div className="login-field">
              <label className="login-label">Mot de passe</label>
              <input
                className="login-input"
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required
                autoComplete="current-password"
              />
            </div>

            {error && <div className="login-error">{error}</div>}

            <button className="login-btn" type="submit" disabled={loading}>
              {loading ? 'Connexion...' : 'LOGIN'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;
