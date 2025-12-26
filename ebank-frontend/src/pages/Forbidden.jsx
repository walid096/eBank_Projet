import React from 'react';
import { useNavigate } from 'react-router-dom';

// Matches RestAccessDeniedHandler message exactly
const Forbidden = () => {
    const navigate = useNavigate();

    return (
        <div style={{ textAlign: 'center', padding: '50px' }}>
            <h1>403 - Accès Refusé</h1>
            <p style={{ color: 'red', fontSize: '18px', margin: '20px 0' }}>
                Vous n'avez pas le droit d'accéder à cette fonctionnalité. Veuillez contacter votre administrateur
            </p>
            <button onClick={() => navigate('/login')}>Retour à la connexion</button>
        </div>
    );
};

export default Forbidden;