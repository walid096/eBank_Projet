import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AgentDashboard.css';

const AgentDashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="page">
      <h1 className="page-title">Tableau de bord</h1>
      <p className="page-subtitle">Bienvenue dans l’espace Agent Guichet</p>

      <div className="cards">
        <div className="card" onClick={() => navigate('/agent/create-client')}>
          <h3 className="card-title">Ajouter nouveau client</h3>
          <p className="card-desc">Créer un nouveau client dans le système.</p>
          <div className="card-footer">
            <div className="card-cta">Ouvrir <span>→</span></div>
          </div>
        </div>

        <div className="card" onClick={() => navigate('/agent/create-account')}>
          <h3 className="card-title">Nouveau compte bancaire</h3>
          <p className="card-desc">Créer un nouveau compte pour un client.</p>
          <div className="card-footer">
            <div className="card-cta">Ouvrir <span>→</span></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AgentDashboard;
