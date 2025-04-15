import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Box,
  Alert,
  Snackbar,
  IconButton,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete'; // Icon for delete button

const AdminMealPlan = () => {
  const [data, setData] = useState([]); // Meal plans data
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openSnackbar, setOpenSnackbar] = useState(false); // Snackbar state for alert
  const navigate = useNavigate();

  // Fetch meal plans
  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/v1/admin/users/mealplans', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setData(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to fetch data.');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleNavigation = (route) => {
    navigate(route);
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  // Handle delete meal plan
  const handleDelete = async (mealPlanId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/api/v1/admin/mealplans/${mealPlanId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setData(data.filter(mealPlan => mealPlan.mealPlanId !== mealPlanId)); // Update UI by removing deleted meal plan
      setOpenSnackbar(true);
    } catch (err) {
      console.error('Error deleting meal plan:', err);
      setError('Failed to delete meal plan.');
      setOpenSnackbar(true);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Admin Meal Plan
          </Typography>
          <Button color="inherit" onClick={() => handleNavigation("/admin-dashboard")}>
            Users
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/AdminRecipeManagement')}>
            Recipes
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/AdminMealPlan')}>
            Meal Plans
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/shopping-list')}>
            Shopping List
          </Button>
          <Button color="inherit" onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      {/* Meal Plan Table */}
      <TableContainer component={Paper} sx={{ margin: '20px auto', maxWidth: '80%' }}>
        <Typography variant="h5" sx={{ marginBottom: '10px' }}>
          Meal Plans
        </Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>User Name</TableCell>
              <TableCell>Recipe Name</TableCell>
              <TableCell>Meal Date</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((mealPlan) => (
              <TableRow key={mealPlan.mealPlanId}>
                <TableCell>{mealPlan.userName}</TableCell>
                <TableCell>{mealPlan.recipeName}</TableCell>
                <TableCell>{new Date(mealPlan.mealDate).toLocaleString()}</TableCell>
                <TableCell>
                  <IconButton
                    color="secondary"
                    onClick={() => handleDelete(mealPlan.mealPlanId)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Snackbar for Alert */}
      <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
        <Alert onClose={handleCloseSnackbar} severity="error" sx={{ width: '100%' }}>
          {error}
        </Alert>
      </Snackbar>
    </div>
  );
};

export default AdminMealPlan;
