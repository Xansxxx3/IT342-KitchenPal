import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Button,
  Grid,
} from "@mui/material";
import axios from "axios";

const DishDialog = ({ open, handleClose, dish, userId }) => {
  const addToMealPlan = async () => {
    try {
    
      const response = await axios.post("https://it342-kitchenpal.onrender.com/api/meal-plans", {
        user: {
          userId: userId, 
        },
        dish: {
          id: dish.id,
        },
        mealDate: new Date(), 
      });

      alert(`Dish "${dish.title}" added to Meal Plan!`);
      handleClose();
    } catch (error) {
      console.error("Error adding dish to meal plan:", error);
      alert("Failed to add dish to meal plan.");
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle>{dish?.title}</DialogTitle>
      <DialogContent>
        <Grid container spacing={2}>
          {/* Dish Image */}
          <Grid item xs={12} sm={4}>
            <img
              src={dish?.image}
              alt={dish?.title}
              style={{
                width: "100%",
                borderRadius: "10px",
                boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
              }}
            />
          </Grid>

          {/* Dish Details */}
          <Grid item xs={12} sm={8}>
            <Typography variant="h6">Ingredients:</Typography>
            <Typography>
              {dish?.ingredients || "Ingredients not available"}
            </Typography>
            <Typography variant="h6" sx={{ mt: 2 }}>
              Description:
            </Typography>
            <Typography>
              {dish?.description || "Description not available"}
            </Typography>
          </Grid>
        </Grid>
      </DialogContent>
      <DialogActions>
        <Button
          variant="contained"
          color="success"
          onClick={addToMealPlan}
        >
          Add to Meal Plan
        </Button>
        <Button variant="outlined" color="error" onClick={handleClose}>
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DishDialog;
