import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Box,
  Button,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import { AccountCircle } from '@mui/icons-material';
import logoImage from '../assets/platelogo.png';

const NavBar = () => {
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);

  const userId = localStorage.getItem('userId'); // Get userId from localStorage

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId'); // Optionally clear userId when logging out
    navigate('/login');
  };

  return (
    <AppBar
      position="static"
      sx={{
        backgroundColor: '#FEFF9F',
        padding: '5px px',
        boxShadow: 'none',
        opacity: .95
      }}
    >
      <Toolbar sx={{ justifyContent: 'space-between' }}>
        <Box component={Link} to={`/home?userId=${userId}`} sx={{ textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
          <Box
            component="img"
            src={logoImage}
            alt="Logo"
            sx={{
              height: '50px',
              transform: 'scale(2.5)',
              transformOrigin: 'center',
              cursor: 'pointer',
            }}
          />
        </Box>

        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            component={Link}
            to={`/mealplan?userId=${userId}`}
            variant="contained"
            sx={{
              backgroundColor: '#A0D683',
              color: '#333',
              fontWeight: 'bold',
              '&:hover': { backgroundColor: '#8DBF6A' },
            }}
          >
            Meal Plan
          </Button>
          <Button
            component={Link}
            to={`/shoppinglist?userId=${userId}`}
            variant="contained"
            sx={{
              backgroundColor: '#A0D683',
              color: '#333',
              fontWeight: 'bold',
              '&:hover': { backgroundColor: '#8DBF6A' },
            }}
          >
            Shopping List
          </Button>
          <Button
            component={Link}
            to={`/recipe?userId=${userId}`} // Append userId as query param
            variant="contained"
            sx={{
              backgroundColor: '#A0D683',
              color: '#333',
              fontWeight: 'bold',
              '&:hover': { backgroundColor: '#8DBF6A' },
            }}
          >
            Recipe
          </Button>

          {/* Add Social Feed Button */}
            <Button
              component={Link}
              to="/social-feed" // Link to SocialFeed page
              variant="contained"
              sx={{
                backgroundColor: '#A0D683',
                color: '#333',
                fontWeight: 'bold',
                '&:hover': { backgroundColor: '#8DBF6A' },
              }}
            >
              Social Feed
            </Button>

          {/* Profile Dropdown */}
          <IconButton
            onClick={handleMenuOpen}
            sx={{
              backgroundColor: '#A0D683',
              '&:hover': { backgroundColor: '#8DBF6A' },
              padding: '10px',
            }}
          >
            <AccountCircle sx={{ color: '#333' }} />
          </IconButton>
          <Menu
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
            sx={{
              mt: '40px',
            }}
          >
            <MenuItem onClick={() => navigate('/profile')}>View Profile</MenuItem>
            <MenuItem onClick={handleLogout}>Log Out</MenuItem>
          </Menu>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default NavBar;
