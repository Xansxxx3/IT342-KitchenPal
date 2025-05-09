import React, { useEffect, useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardMedia,
  CardContent,
  CardActionArea,
  Modal,
  Button,
  Snackbar,
  Alert,
} from '@mui/material';
import NavBar from '../components/NavBar'; // NavBar component
import backgroundImage from '../assets/leafbg.png'; // Background image
import { useLocation, Navigate } from 'react-router-dom';
import axios from 'axios'; // Make sure axios is installed
import Slide from '@mui/material/Slide';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

const Recipe = () => {
  const location = useLocation();
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [open, setOpen] = useState(false);

  // Snackbar state
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success'); // 'success' or 'error'
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const queryParams = new URLSearchParams(location.search);
  const userId = queryParams.get('userId');

  // Fetch recipes from the backend
  useEffect(() => {
    const fetchRecipes = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('https://it342-kitchenpal.onrender.com/api/recipe/allrecipe', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch recipes');
        }
        const data = await response.json();
        setRecipes(data);
      } catch (error) {
        console.error('Error fetching recipes:', error);
      }
    };

    fetchRecipes();
  }, []);

  const token = localStorage.getItem('token');
  if (!token || !userId) {
    return <Navigate to="/login" replace />;
  }

  const handleOpen = (recipe) => {
    setSelectedRecipe(recipe);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedRecipe(null);
  };

  // Add to meal plan
  const handleAddToMealPlan = async () => {
    try {
      const token = localStorage.getItem("token");
      const userId = localStorage.getItem("userId");

      if (!token || !userId) {
        setMessage('You need to log in to add a recipe to the Meal Plan.');
        setMessageType('error');
        setOpenSnackbar(true);
        return;
      }

      const mealPlanResponse = await fetch(
        `https://it342-kitchenpal.onrender.com/api/meal-plans/user/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      let mealPlans = [];

      if (mealPlanResponse.status === 200) {
        mealPlans = await mealPlanResponse.json();
      } else if (mealPlanResponse.status === 204) {
        mealPlans = [];
      } else {
        throw new Error("Failed to fetch existing meal plans.");
      }

      const isDuplicate = mealPlans.some(
        (mealPlan) =>
          mealPlan.recipe && mealPlan.recipe.recipeId === selectedRecipe.recipeId
      );

      if (isDuplicate) {
        setMessage('This recipe is already added to the Meal Plan.');
        setMessageType('error');
        setOpenSnackbar(true);
        return;
      }

      const body = {
        userId: Number(userId),
        recipeId: selectedRecipe.recipeId,
      };

      const response = await fetch("https://it342-kitchenpal.onrender.com/api/meal-plans/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
      }

      setMessage(`${selectedRecipe.title} added to Meal Plan!`);
      setMessageType('success');
      setOpenSnackbar(true);
      handleClose(); // Close the modal after successful addition
    } catch (error) {
      console.error("Error adding to meal plan:", error);
      setMessage(error.message || 'Failed to add recipe to Meal Plan. Please try again.');
      setMessageType('error');
      setOpenSnackbar(true);
    }
  };

  // Add to shopping list
  const handleAddToShoppingList = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token || !userId) {
        setMessage('You need to log in to add a recipe to the shopping list.');
        setMessageType('error');
        setOpenSnackbar(true);
        return;
      }

      console.log('Adding to shopping list with:', { userId, recipeId: selectedRecipe.recipeId });

      const response = await fetch('https://it342-kitchenpal.onrender.com/api/shopping-list-items/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          userId: Number(userId),
          recipeId: selectedRecipe.recipeId,
        }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      setMessage(`${selectedRecipe.title} added to Shopping List!`);
      setMessageType('success');
      setOpenSnackbar(true);
    } catch (error) {
      console.error('Error adding to shopping list:', error);
      setMessage('Failed to add recipe to Shopping List.');
      setMessageType('error');
      setOpenSnackbar(true);
    }
  };

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        minHeight: '100vh',
        width: '100%',
        overflow: 'auto',
        backgroundAttachment: 'fixed',
        paddingBottom: '20px',
        display: 'flex',
        flexDirection: 'column',
        color: '#333',
      }}
    >
      <NavBar />

      <Container sx={{ textAlign: 'center', paddingTop: '20px' }}>
        <Typography
          variant="h1"
          component="h1"
          sx={{
            fontWeight: 'bold',
            fontSize: '4rem',
            color: '#FFD700',
            marginBottom: 5,
            textShadow: '2px 2px 5px rgba(0, 0, 0, 0.5)',
          }}
        >
          Recipe Collection
        </Typography>

        {/* Recipe Grid */}
        <Grid container spacing={4} justifyContent="center">
          {recipes.map((recipe) => (
            <Grid item xs={12} sm={6} md={4} key={recipe.recipeId}>
              <Card
                sx={{
                  borderRadius: '15px',
                  boxShadow: '0 6px 15px rgba(0, 0, 0, 0.3)',
                  transition: 'transform 0.3s ease-in-out',
                  '&:hover': { transform: 'scale(1.05)' },
                }}
              >
                <CardActionArea onClick={() => handleOpen(recipe)}>
                  <CardMedia
                    component="img"
                    height="200"
                    image={recipe.imagePath}
                    alt={recipe.title}
                    sx={{
                      borderTopLeftRadius: '15px',
                      borderTopRightRadius: '15px',
                    }}
                  />
                  <CardContent>
                    <Typography
                      variant="h6"
                      component="h3"
                      sx={{
                        fontWeight: 'bold',
                        textAlign: 'center',
                      }}
                    >
                      {recipe.title}
                    </Typography>
                  </CardContent>
                </CardActionArea>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Recipe Details Modal */}
      {selectedRecipe && (
        <Modal open={open} onClose={handleClose}>
          <Box
            sx={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '50%',
              backgroundColor: 'white',
              boxShadow: 24,
              p: 4,
              borderRadius: '10px',
              overflow: 'auto',
              maxHeight: '90vh',
            }}
          >
            {/* Modal content */}
            <Box display="flex" flexDirection="row" gap="20px">
              <img
                src={selectedRecipe.imagePath}
                alt={selectedRecipe.title}
                style={{
                  borderRadius: '10px',
                  width: '40%',
                  objectFit: 'cover',
                }}
              />
<Box sx={{ width: '60%', textAlign: 'left' }}>
  <Typography variant="h4" sx={{ fontWeight: 'bold', mb: 2 }}>
    {selectedRecipe.title}
  </Typography>
  <Typography variant="h6" sx={{ mb: 2 }}>
    <strong>Ingredients:</strong>
  </Typography>
  <ul>
    {selectedRecipe.ingredients.map((ingredient, index) => (
      <li key={index}>{ingredient}</li>
    ))}
  </ul>
</Box>
            </Box>

            <Typography variant="h6" sx={{ mt: 3, textAlign: 'left' }}>
              <strong>Description:</strong> <br />
              {selectedRecipe.description}
            </Typography>

            <Box sx={{ mt: 3, display: 'flex', gap: 3 }}>
              <Typography>
                <strong>Prep Time:</strong> <br />
                {selectedRecipe.prepTime}
              </Typography>
              <Typography>
                <strong>Nutrition Info:</strong>
                <br />
                {selectedRecipe.nutritionInfo}
              </Typography>
              <Typography>
                <strong>Cuisine Type:</strong>
                <br />
                {selectedRecipe.cuisineType}
              </Typography>
              <Typography>
                <strong>Meal Type:</strong>
                <br />
                {selectedRecipe.mealType}
              </Typography>
              <Typography>
                <strong>Ratings:</strong>
                <br />
                {selectedRecipe.ratingsAverage}
              </Typography>
            </Box>

            {/* Action Buttons */}
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'center',
                gap: 3,
                mt: 4,
              }}
            >
              <Button
                variant="contained"
                startIcon={
                  <RestaurantMenuIcon
                    sx={{
                      fontSize: '1.8rem',
                      transition: 'transform 0.3s ease',
                      '.MuiButton-root:hover &': {
                        transform: 'rotate(-10deg) scale(1.1)',
                      },
                    }}
                  />
                }
                sx={{
                  backgroundColor: '#A0D683',
                  color: '#fff',
                  px: 4,
                  py: 1.5,
                  borderRadius: 3,
                  boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.15)',
                  fontWeight: 'bold',
                  fontSize: '1rem',
                  textTransform: 'none',
                  transition: 'all 0.3s ease',
                  '&:hover': {
                    backgroundColor: '#8CC765',
                    transform: 'translateY(-2px)',
                    boxShadow: '0px 6px 15px rgba(0, 0, 0, 0.2)',
                  },
                }}
                onClick={handleAddToMealPlan}
              >
                Add to Meal Plan
              </Button>

              <Button
                variant="contained"
                startIcon={
                  <ShoppingCartIcon
                    sx={{
                      fontSize: '1.8rem',
                      transition: 'transform 0.3s ease',
                      '.MuiButton-root:hover &': {
                        transform: 'rotate(10deg) scale(1.1)',
                      },
                    }}
                  />
                }
                sx={{
                  backgroundColor: '#72BF78',
                  color: '#fff',
                  px: 4,
                  py: 1.5,
                  borderRadius: 3,
                  boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.15)',
                  fontWeight: 'bold',
                  fontSize: '1rem',
                  textTransform: 'none',
                  transition: 'all 0.3s ease',
                  '&:hover': {
                    backgroundColor: '#5E9F64',
                    transform: 'translateY(-2px)',
                    boxShadow: '0px 6px 15px rgba(0, 0, 0, 0.2)',
                  },
                }}
                onClick={handleAddToShoppingList}
              >
                Add to Shopping List
              </Button>
            </Box>


          </Box>
        </Modal>
      )}

      {/* Snackbar for Messages */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={4000}
        onClose={() => setOpenSnackbar(false)}
        TransitionComponent={(props) => <Slide {...props} direction="up" />}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          severity={messageType} // Use messageType
          sx={{ width: '100%' }}
        >
          {message} {/* Use message */}
        </Alert>
      </Snackbar>

    </Box>
  );
};

export default Recipe;
