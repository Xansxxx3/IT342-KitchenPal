import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import backgroundImage from '../assets/Landin.jpg';
import logoImage from '../assets/platelogo.png';
import '../CSS/Register.css';

const Register = () => {
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);

    const password = formData.get('password');
    const confirmPassword = formData.get('confirmPassword');

    // Password validation
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
      setMessage(
        'Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character.'
      );
      setMessageType('error');
      setOpenSnackbar(true);
      return;
    }

    if (password !== confirmPassword) {
      setMessage('Passwords do not match');
      setMessageType('error');
      setOpenSnackbar(true);
      return;
    }

    const data = {
      fname: formData.get('firstName'),
      lname: formData.get('lastName'),
      email: formData.get('email'),
      password: password,
    };

    try {
      const response = await axios.post('https://it342-kitchenpal.onrender.com/api/v1/auth/register', data, {
        headers: { 'Content-Type': 'application/json' },
      });

      setMessage('Registration successful! Redirecting to login...');
      setMessageType('success');
      setOpenSnackbar(true);

      localStorage.setItem('token', response.data.token);

      setTimeout(() => navigate('/login'), 2000);
    } catch (error) {
      console.error('Registration failed:', error.response?.data || error.message);
      setMessage('Registration failed. Please try again.');
      setMessageType('error');
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => setOpenSnackbar(false);

  return (
    <div className="register-page">
      <div className="page-background">
        <div className="container">
          <img src={logoImage} alt="Logo" className="logo" />
          <h1 className="title">Sign Up</h1>

          <form onSubmit={handleSubmit} className="form">
            <div className="input-group">
              <input
                type="text"
                name="firstName"
                placeholder="First Name"
                required
                className="input-field"
              />
              <input
                type="text"
                name="lastName"
                placeholder="Last Name"
                required
                className="input-field"
              />
            </div>

            <input
              type="email"
              name="email"
              placeholder="Email"
              required
              className="input-field"
              pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.com$"
              title="Email must be in the format 'name@domain.com'"
            />
            <input
              type="password"
              name="password"
              placeholder="Password"
              required
              className="input-field"
              title="Password must be at least 8 characters, include an uppercase letter, a lowercase letter, a number, and a special character."
            />
            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              required
              className="input-field"
            />
            <button type="submit" className="submit-btn">
              Sign Up
            </button>
            <div className="link">
              Already have an account? <Link to="/login">Sign In</Link>
            </div>
          </form>
        </div>

        <Snackbar
          open={openSnackbar}
          autoHideDuration={6000}
          onClose={handleCloseSnackbar}
        >
          <MuiAlert
            onClose={handleCloseSnackbar}
            severity={messageType}
            sx={{ width: '100%' }}
          >
            {message}
          </MuiAlert>
        </Snackbar>
      </div>
    </div>
  );
};

export default Register;
