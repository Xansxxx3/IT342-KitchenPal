import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import backgroundImage from '../assets/Landin.jpg';
import logoImage from '../assets/platelogo.png';
import '../CSS/Login.css'; // Import the CSS file
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';

// For Snackbar to work correctly with MuiAlert
const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});



const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [severity, setSeverity] = useState('success'); // 'success', 'error', etc.

  const navigate = useNavigate();

  const togglePasswordVisibility = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('Login attempt started'); // Debugging line

    try {
      const data = { email, password };

      const response = await axios.post(
        `https://it342-kitchenpal.onrender.com/api/v1/auth/authenticate`,
        data,
        { headers: { 'Content-Type': 'application/json' } }
      );

      console.log('Response data:', response.data); // Debugging line


      const { token, role, userId } = response.data; // Assuming the response includes userId


      // Store token, role, and userId in localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      localStorage.setItem('userId', userId); // Store userId in localStorage

      // Set success message and open Snackbar
      setAlertMessage('Login successful! Welcome back!');
      setSeverity('success');
      setOpenSnackbar(true);
      console.log('Snackbar triggered'); // Debugging line

      // Navigate based on role
      if (role === 'ADMIN') {
        navigate('/admin-dashboard');
      } else if (role === 'USER') {
        navigate(`/Home?id=${userId}`);
      } else {
        setErrorMessage('Invalid role. Please contact support.');
      }
    } catch (error) {
      console.error('Login failed:', error.response?.data || error.message);
      setErrorMessage('Invalid email or password. Please try again.');

      // Set error message and open Snackbar
      setAlertMessage('Login failed. Please check your credentials.');
      setSeverity('error');
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <div
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        width: '100vw',
        height: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'left',
      }}
    >
      <div className="login-container">
        {/* Logo Image */}
        <img src={logoImage} alt="Logo" className="login-logo" />

        <h1 className="login-title">Sign In</h1>

        {/* Show error message if login fails */}
        {errorMessage && <p className="login-error">{errorMessage}</p>}

        {/* Login form */}
        <form
          onSubmit={handleLogin}
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            width: '100%',
          }}
        >
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="login-input"
          />
          <div style={{ width: '100%', position: 'relative' }}>
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="login-input"
            />
            <button
              type="button"
              onClick={togglePasswordVisibility}
              className="password-toggle"
            >
              <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
            </button>
          </div>
          <button type="submit" className="login-submit-btn">
            Sign In
          </button>
          <button
        type="button"
        className="google-signin-btn"
        onClick={() => {
          window.location.href = 'https://it342-kitchenpal.onrender.com/login/oauth2/code/google';
    }}
    >
    <img
    src="https://developers.google.com/identity/images/g-logo.png"
    alt="Google logo"
    style={{ width: '20px', marginRight: '10px' }}
  />
  Sign in with Google
</button>

          <div className="login-signup">
            Don't have an account? <Link to="/register">Sign Up</Link>
          </div>
        </form>
      </div>

      {/* Snackbar for notifications */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={severity}
          sx={{ width: '100%' }}
        >
          {alertMessage}
        </Alert>
      </Snackbar>
    </div>
  );
};

export default Login;
