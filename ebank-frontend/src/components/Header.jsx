import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import './Header.css';

const BANK_NAME = "Walid & Anass Bank";

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getMenuItems = () => {
    if (!user.role) return [];

    if (user.role === 'CLIENT') {
      return [
        { label: 'Tableau de bord', path: '/client/dashboard' },
        { label: 'Nouveau virement', path: '/client/transfer' },
        { label: 'Changer mot de passe', path: '/change-password' },
      ];
    }

    if (user.role === 'AGENT_GUICHET') {
      return [
        { label: 'Tableau de bord', path: '/agent/dashboard' },
        { label: 'Ajouter client', path: '/agent/create-client' },
        { label: 'Nouveau compte', path: '/agent/create-account' },
        { label: 'Changer mot de passe', path: '/change-password' },
      ];
    }

    return [];
  };

  const roleLabel =
    user.role === 'AGENT_GUICHET' ? 'Agent Guichet' :
    user.role === 'CLIENT' ? 'Client' : '';

  const isActive = (path) => location.pathname === path;

  return (
    <header className="app-header">
      <div className="app-header__inner">
        <div className="brand">
          <div className="brand__name">{BANK_NAME}</div>
          {roleLabel && <div className="brand__role">{roleLabel}</div>}
        </div>

        {user.role && (
          <div className="header-right">
            <nav className="tabs" aria-label="Navigation">
              {getMenuItems().map((item) => (
                <button
                  key={item.path}
                  className={`tab ${isActive(item.path) ? 'tab--active' : ''}`}
                  onClick={() => navigate(item.path)}
                  type="button"
                >
                  {item.label}
                </button>
              ))}
            </nav>

            <button className="logout-btn" onClick={handleLogout} type="button">
              Déconnexion
            </button>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
