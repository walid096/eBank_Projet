import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './auth/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';
import RoleGuard from './routes/RoleGuard';
import Header from './components/Header';

// Pages
import Login from './pages/Login';
import Forbidden from './pages/Forbidden';
import ChangePassword from './pages/ChangePassword';
import Dashboard from './pages/client/Dashboard';
import Transfer from './pages/client/Transfer';
import AgentDashboard from './pages/agent/AgentDashboard';
import CreateClient from './pages/agent/CreateClient';
import CreateAccount from './pages/agent/CreateAccount';

// Layout wrapper with Header
const Layout = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return (
    <>
      {isAuthenticated() && <Header />}
      {children}
    </>
  );
};

// Home redirect based on role
const HomeRedirect = () => {
  const { user } = useAuth();
  if (!user.role) {
    return <Navigate to="/login" replace />;
  }
  if (user.role === 'CLIENT') {
    return <Navigate to="/client/dashboard" replace />;
  }
  if (user.role === 'AGENT_GUICHET') {
    return <Navigate to="/agent/dashboard" replace />;
  }
  return <Navigate to="/login" replace />;
};

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Layout>
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/forbidden" element={<Forbidden />} />

            {/* Protected routes */}
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <HomeRedirect />
                </ProtectedRoute>
              }
            />

            {/* Change password (both roles) */}
            <Route
              path="/change-password"
              element={
                <ProtectedRoute>
                  <ChangePassword />
                </ProtectedRoute>
              }
            />

            {/* CLIENT routes */}
            <Route
              path="/client/dashboard"
              element={
                <ProtectedRoute>
                  <RoleGuard requiredRole="CLIENT">
                    <Dashboard />
                  </RoleGuard>
                </ProtectedRoute>
              }
            />
            <Route
              path="/client/transfer"
              element={
                <ProtectedRoute>
                  <RoleGuard requiredRole="CLIENT">
                    <Transfer />
                  </RoleGuard>
                </ProtectedRoute>
              }
            />

            {/* AGENT_GUICHET routes */}
            <Route
              path="/agent/dashboard"
              element={
                <ProtectedRoute>
                  <RoleGuard requiredRole="AGENT_GUICHET">
                    <AgentDashboard />
                  </RoleGuard>
                </ProtectedRoute>
              }
            />
            <Route
              path="/agent/create-client"
              element={
                <ProtectedRoute>
                  <RoleGuard requiredRole="AGENT_GUICHET">
                    <CreateClient />
                  </RoleGuard>
                </ProtectedRoute>
              }
            />
            <Route
              path="/agent/create-account"
              element={
                <ProtectedRoute>
                  <RoleGuard requiredRole="AGENT_GUICHET">
                    <CreateAccount />
                  </RoleGuard>
                </ProtectedRoute>
              }
            />

            {/* 404 */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Layout>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;