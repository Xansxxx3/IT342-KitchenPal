import React, { useEffect, useState } from "react";
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Button,
  CircularProgress,
  Modal,
} from "@mui/material";
import axios from "axios";
import backgroundImage from "../assets/leafbg.png"; // Background image
import NavBar from "../components/NavBar"; // NavBar component
import { useNavigate, useLocation } from "react-router-dom"; // Import useLocation and useNavigate

const MealPlan = () => {
  const location = useLocation(); // Hook to access current location
  const navigate = useNavigate(); // To redirect users
  const [mealPlans, setMealPlans] = useState([]); // State for meal plans
  const [loading, setLoading] = useState(true); // Loading indicator
  const [error, setError] = useState(null); // Error state
  const [selectedMealPlan, setSelectedMealPlan] = useState(null); // Selected meal plan for modal
  const [open, setOpen] = useState(false); // Modal state

  // Retrieve userId from query parameters or fallback to localStorage
  const queryParams = new URLSearchParams(location.search);
  const userId = queryParams.get("userId") || localStorage.getItem("userId");

  useEffect(() => {
    const checkLoginStatus = () => {
      const token = localStorage.getItem("token");
      if (!userId || !token) {
        // If no userId or token, redirect to login
        navigate("/login");
        return false;
      }
      return true;
    };
  
    if (checkLoginStatus()) {
      const fetchMealPlans = async () => {
        const token = localStorage.getItem("token");
  
        try {
          const response = await axios.get(`http://localhost:8080/api/meal-plans/user/${userId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
  
          // Check if response.data is an array before using reduce
          if (Array.isArray(response.data)) {
            // Grouping meal plans by their mealType
            const groupedMealPlans = response.data.reduce((acc, mealPlan) => {
              const mealType = mealPlan.recipe?.mealType || "Uncategorized"; // Fallback to 'Uncategorized'
              if (!acc[mealType]) {
                acc[mealType] = [];
              }
              acc[mealType].push(mealPlan);
              return acc;
            }, {});
  
            setMealPlans(groupedMealPlans); // Save the grouped meal plans
          } else {
            console.error("Expected an array of meal plans, but received:", response.data);
            setError("Failed to fetch meal plans. Invalid response format.");
          }
        } catch (err) {
          console.error("Error fetching meal plans:", err.response || err.message);
          setError(err.response?.data || "Failed to fetch meal plans. Please try again later.");
        } finally {
          setLoading(false);
        }
      };
  
      fetchMealPlans();
    }
  }, [userId, navigate]);
  

  // Open modal with selected meal plan
  const handleOpen = (mealPlan) => {
    setSelectedMealPlan(mealPlan);
    setOpen(true);
  };

  // Close modal
  const handleClose = () => {
    setOpen(false);
    setSelectedMealPlan(null);
  };

  // Delete meal plan
  const deleteMealPlan = async () => {
    if (!selectedMealPlan) return;
  
    const token = localStorage.getItem("token");
  
    try {
      // Deleting the meal plan
      await axios.delete(`http://localhost:8080/api/meal-plans/${selectedMealPlan.mealPlanId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
  
      // After deletion, we need to filter out the deleted meal plan
      // Group meal plans by meal type again
      const updatedMealPlans = { ...mealPlans }; // Copy the current state
  
      Object.keys(updatedMealPlans).forEach((mealType) => {
        updatedMealPlans[mealType] = updatedMealPlans[mealType].filter(
          (mealPlan) => mealPlan.mealPlanId !== selectedMealPlan.mealPlanId
        );
  
        // If there are no meal plans for that mealType anymore, delete the key
        if (updatedMealPlans[mealType].length === 0) {
          delete updatedMealPlans[mealType];
        }
      });
  
      // Update the state after deletion
      setMealPlans(updatedMealPlans);
  
      // Close modal
      handleClose();
    } catch (err) {
      console.error("Error deleting meal plan:", err);
      alert("Failed to delete meal plan. Please try again.");
    }
  };

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover', // Ensures the image covers the entire container
        backgroundRepeat: 'no-repeat', // Prevents the image from repeating
        backgroundPosition: 'center', // Centers the image
        minHeight: '100vh', // Minimum height to cover the viewport
        width: '100%',
        overflow: 'auto', // Ensures scrolling works properly if content overflows
        backgroundAttachment: 'fixed', // Keeps the background fixed while scrolling
        paddingBottom: '20px',
        display: 'flex', // Allows centering of content
        flexDirection: 'column', // Stacks child elements vertically
        color: '#fff',
      }}
    >
      <NavBar />
      <Container
        sx={{
          textAlign: "center",
          paddingTop: "20px",
        }}
      >
        <Typography
          variant="h1"
          component="h1"
          sx={{
            fontWeight: 'bold',
            fontSize: '4rem',
            color: '#FFD700', // Gold color
            marginBottom: 5,
            textShadow: '2px 2px 5px rgba(0, 0, 0, 0.5)',
          }}
        >
          Meal Plan
        </Typography>
        <Typography
          variant="body1"
          sx={{
            maxWidth: "600px",
            margin: "0 auto",
            mb: 4,
            fontSize: "1.2rem",
          }}
        >
          
        </Typography>

        {/* Loading Indicator */}
        {loading && (
          <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
            <CircularProgress />
          </Box>
        )}

        {/* Error Handling */}
        {error && (
          <Typography color="error" sx={{ mt: 4 }}>
            {error}
          </Typography>
        )}

        {/* Meal Plans Grouped by Meal Type */}
        {!loading && !error && Object.keys(mealPlans).length > 0 ? (
          Object.keys(mealPlans).map((mealType) => (
            <Box key={mealType} sx={{ marginBottom: "40px" }}>
              <Typography variant="h4" sx={{ marginBottom: "20px" }}>
                {mealType}
              </Typography>
              <Grid container spacing={4}>
                {mealPlans[mealType].map((mealPlan) => (
                  <Grid item xs={12} sm={6} md={4} key={mealPlan.mealPlanId}>
                    <Card
                      sx={{
                        maxWidth: 345,
                        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
                      }}
                      onClick={() => handleOpen(mealPlan)}
                    >
                      <CardMedia
                        component="img"
                        height="140"
                        image={
                          mealPlan.recipe?.imagePath
                            ? `http://localhost:8080/api/recipe/images/${mealPlan.recipe.imagePath}`
                            : "placeholder.jpg"
                        }
                        alt={mealPlan.recipe?.title || "Recipe Image"}
                      />
                      <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                          {mealPlan.recipe?.title || "Recipe Title Not Available"}
                        </Typography>
                        <Typography variant="body2" color="textSecondary">
                          {mealPlan.recipe?.description || "No description available."}
                        </Typography>
                      </CardContent>
                      <CardActions>
                        <Button size="small" onClick={() => handleOpen(mealPlan)}>
                          View Details
                        </Button>
                      </CardActions>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            </Box>
          ))
        ) : (
          !loading &&
          !error && (
            <Typography color="textSecondary" sx={{ mt: 4 }}>
              No meal plans found.
            </Typography>
          )
        )}

        {/* Recipe Details Modal */}
        {selectedMealPlan && (
          <Modal open={open} onClose={handleClose}>
            <Box
              sx={{
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                width: "50%",
                backgroundColor: "white",
                boxShadow: 24,
                p: 4,
                borderRadius: "10px",
                overflow: "auto",
                maxHeight: "90vh",
              }}
            >
              <Box display="flex" flexDirection="row" gap="20px">
                <img
                  src={`http://localhost:8080/api/recipe/images/${selectedMealPlan.recipe.imagePath}`}
                  alt={selectedMealPlan.recipe.title}
                  style={{
                    borderRadius: "10px",
                    width: "40%",
                    objectFit: "cover",
                  }}
                />
                <Box sx={{ width: "60%" }}>
                  <Typography variant="h4" sx={{ fontWeight: "bold", mb: 2 }}>
                    {selectedMealPlan.recipe.title}
                  </Typography>
                  <Typography variant="h5" sx={{ mb: 2 }}>
                    <strong>Ingredients:</strong>
                  </Typography>
                  <ul>
                    {selectedMealPlan.recipe.ingredients.split(",").map((item, index) => (
                      <li key={index}>{item.trim()}</li>
                    ))}
                  </ul>
                </Box>
              </Box>

              <Typography variant="h5" sx={{ mt: 3 }}>
                <strong>Description:</strong> {selectedMealPlan.recipe.description}
              </Typography>

              <Box sx={{ mt: 3, display: "flex", gap: 3 }}>
                <Typography>
                  <strong>Prep Time:</strong> <br />
                  {selectedMealPlan.recipe.prepTime} mins
                </Typography>
                <Typography>
                  <strong>Nutrition Info:</strong> <br />
                  {selectedMealPlan.recipe.nutritionInfo}
                </Typography>
                <Typography>
                  <strong>Cuisine Type:</strong> {selectedMealPlan.recipe.cuisineType}
                </Typography>
                <Typography>
                  <strong>Meal Type:</strong> {selectedMealPlan.recipe.mealType}
                </Typography>
                <Typography>
                  <strong>Ratings:</strong> {selectedMealPlan.recipe.ratingsAverage}
                </Typography>
              </Box>

              {/* Delete Button */}
              <Box sx={{ display: "flex", justifyContent: "flex-end", mt: 3 }}>
                <Button variant="contained" color="error" onClick={deleteMealPlan}>
                  Delete Meal Plan
                </Button>
              </Box>
            </Box>
          </Modal>
        )}
      </Container>
    </Box>
  );
};

export default MealPlan;
