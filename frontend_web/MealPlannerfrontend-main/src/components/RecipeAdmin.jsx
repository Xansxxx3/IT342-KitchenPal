import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import UpdateRecipeModal from './UpdateRecipeModal';
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
    IconButton,
    Dialog,
    DialogActions,
    TextField,
    Box,
  } from '@mui/material';
  import DeleteIcon from '@mui/icons-material/Delete';
  import EditIcon from '@mui/icons-material/Edit';

const RecipeAdmin = () => {
  const [recipes, setRecipes] = useState([]); // Stores all recipes
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showRecipeModal, setShowRecipeModal] = useState(false);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [newRecipe, setNewRecipe] = useState({
    title: '',
    description: '',
    ingredients: '',
    prepTime: '',
    nutritionInfo: '',
    cuisineType: '',
    mealType: '',
    ratingsAverage: '',
  });
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        const [recipeResponse] = await Promise.all([
          axios.get('http://localhost:8080/api/recipe/allrecipe', {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);
        setRecipes(recipeResponse.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to fetch data.');
        setLoading(false);
      }
    };

    fetchData();
  }, []);
  
// Update Recipe
const handleUpdateRecipe = async (updatedRecipe) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.put(
        `http://localhost:8080/api/recipe/update/${updatedRecipe.recipeId}`,
        updatedRecipe,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setRecipes((prevRecipes) =>
        prevRecipes.map((recipe) =>
          recipe.recipeId === updatedRecipe.recipeId ? response.data : recipe
        )
      );

      handleCloseRecipeModal();
    } catch (err) {
      console.error('Error updating recipe:', err);
      setError('Failed to update recipe.');
    }
  };

  // Add New Recipe
  const handleAddRecipe = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post(
        'http://localhost:8080/api/recipe/addrecipe',
        newRecipe,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setRecipes([...recipes, response.data]);
      setNewRecipe({
        title: '',
        description: '',
        ingredients: '',
        prepTime: '',
        nutritionInfo: '',
        cuisineType: '',
        mealType: '',
        ratingsAverage: '',
      });
    } catch (err) {
      console.error('Error adding recipe:', err);
      setError('Failed to add recipe.');
    }
  };

  // Delete Recipe
  const handleDeleteRecipe = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/api/recipe/delete/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRecipes((prevRecipes) =>
        prevRecipes.filter((recipe) => recipe.recipeId !== id)
      );
    } catch (err) {
      console.error('Error deleting recipe:', err);
      setError('Failed to delete recipe.');
    }
  };

  const handleOpenRecipeModal = (recipe) => {
    setSelectedRecipe(recipe);
    setShowRecipeModal(true);
  };

  const handleCloseRecipeModal = () => {
    setShowRecipeModal(false);
    setSelectedRecipe(null);
  };
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleNavigation = (route) => {
    navigate(route);
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Admin Dashboard
          </Typography>
          <Button color="inherit" onClick={() => handleNavigation('/admin-dashboard')}>
            Users
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/admin-recipes')}>
            Recipes
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/meal-plans')}>
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

      <TableContainer component={Paper} sx={{ margin: '20px auto', maxWidth: '80%' }}>
  <Typography variant="h5" sx={{ marginBottom: '10px' }}>
    Recipe Management
  </Typography>
  <Table>
    <TableHead>
      <TableRow>
        <TableCell><strong>ID</strong></TableCell>
        <TableCell><strong>Title</strong></TableCell>
        <TableCell><strong>Description</strong></TableCell>
        <TableCell><strong>Ingredients</strong></TableCell>
        <TableCell><strong>Prep Time (mins)</strong></TableCell>
        <TableCell><strong>Nutrition Info</strong></TableCell>
        <TableCell><strong>Cuisine Type</strong></TableCell>
        <TableCell><strong>Meal Type</strong></TableCell>
        <TableCell><strong>Ratings Average</strong></TableCell>
        <TableCell align="right"><strong>Actions</strong></TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {recipes.length > 0 ? (
        recipes.map((recipe) => (
          <TableRow key={recipe.recipeId}>
            <TableCell>{recipe.recipeId}</TableCell>
            <TableCell>{recipe.title}</TableCell>
            <TableCell>{recipe.description}</TableCell>
            <TableCell>{recipe.ingredients}</TableCell>
            <TableCell>{recipe.prepTime}</TableCell>
            <TableCell>{recipe.nutritionInfo}</TableCell>
            <TableCell>{recipe.cuisineType}</TableCell>
            <TableCell>{recipe.mealType}</TableCell>
            <TableCell>{recipe.ratingsAverage}</TableCell>
            <TableCell align="right">
              <IconButton color="primary" onClick={() => handleOpenRecipeModal(recipe)}>
                <EditIcon />
              </IconButton>
              <IconButton color="error" onClick={() => handleDeleteRecipe(recipe.recipeId)}>
                <DeleteIcon />
              </IconButton>
            </TableCell>
          </TableRow>
        ))
      ) : (
        <TableRow>
          <TableCell colSpan={10} align="center">
            No recipes found
          </TableCell>
        </TableRow>
      )}
    </TableBody>
  </Table>
</TableContainer>


      {/* Add Recipe */}
      <Box sx={{ maxWidth: '80%', margin: '20px auto' }}>
  <Typography variant="h6" sx={{ marginBottom: '10px' }}>
    Add New Recipe
  </Typography>
  <TextField
    label="Title"
    fullWidth
    margin="normal"
    value={newRecipe.title}
    onChange={(e) => setNewRecipe({ ...newRecipe, title: e.target.value })}
    placeholder="Enter recipe title"
  />
  <TextField
    label="Description"
    fullWidth
    multiline
    rows={4}
    margin="normal"
    value={newRecipe.description}
    onChange={(e) => setNewRecipe({ ...newRecipe, description: e.target.value })}
    placeholder="Enter a brief description"
  />
  <TextField
    label="Ingredients"
    fullWidth
    multiline
    rows={2}
    margin="normal"
    value={newRecipe.ingredients}
    onChange={(e) => setNewRecipe({ ...newRecipe, ingredients: e.target.value })}
    placeholder="List ingredients (comma-separated)"
  />
  <TextField
    label="Preparation Time (minutes)"
    type="number"
    fullWidth
    margin="normal"
    value={newRecipe.prepTime}
    onChange={(e) => setNewRecipe({ ...newRecipe, prepTime: e.target.value })}
    placeholder="Enter preparation time"
  />
  <TextField
    label="Nutrition Information"
    fullWidth
    margin="normal"
    value={newRecipe.nutritionInfo}
    onChange={(e) => setNewRecipe({ ...newRecipe, nutritionInfo: e.target.value })}
    placeholder="Provide nutritional details"
  />
  <TextField
    label="Cuisine Type"
    fullWidth
    margin="normal"
    value={newRecipe.cuisineType}
    onChange={(e) => setNewRecipe({ ...newRecipe, cuisineType: e.target.value })}
    placeholder="E.g., Italian, Asian, Mexican"
  />
  <TextField
    label="Meal Type"
    fullWidth
    margin="normal"
    value={newRecipe.mealType}
    onChange={(e) => setNewRecipe({ ...newRecipe, mealType: e.target.value })}
    placeholder="E.g., Breakfast, Lunch, Dinner"
  />
  <TextField
    label="Ratings Average (1-5)"
    type="number"
    fullWidth
    margin="normal"
    value={newRecipe.ratingsAverage}
    onChange={(e) => setNewRecipe({ ...newRecipe, ratingsAverage: e.target.value })}
    placeholder="Enter average rating"
  />
  <Button
    variant="contained"
    color="primary"
    onClick={handleAddRecipe}
    sx={{ marginTop: '10px' }}
  >
    Add Recipe
  </Button>
</Box>
{showRecipeModal && (
        <UpdateRecipeModal
          recipe={selectedRecipe}
          onSave={handleUpdateRecipe}
          onClose={handleCloseRecipeModal}
        />
      )}
    </div>
  );

};
export default RecipeAdmin;
