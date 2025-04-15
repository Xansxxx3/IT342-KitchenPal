import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token'); // Get token from localStorage
  const userId = localStorage.getItem('userId'); // Get userId from localStorage

  // If there is no token or userId, redirect to login page
  if (!token || !userId) {
    return <Navigate to="/login" replace />;
  }

  return children; // If token and userId exist, render the children (protected component)
};

export default ProtectedRoute;
