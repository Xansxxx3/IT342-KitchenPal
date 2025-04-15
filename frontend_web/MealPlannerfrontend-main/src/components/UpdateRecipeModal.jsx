import React, { useState } from 'react';

const UpdateRecipeModal = ({ recipe, onSave, onClose }) => {
  const [updatedRecipe, setUpdatedRecipe] = useState({ ...recipe }); // Initialize with current recipe details

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setUpdatedRecipe({ ...updatedRecipe, [name]: value });
  };

  // Save the updated recipe
  const handleSave = () => {
    onSave(updatedRecipe);
  };

  return (
    <div
      style={{
        position: 'fixed',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        backgroundColor: 'white',
        padding: '20px',
        borderRadius: '8px',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        zIndex: 1000,
      }}
    >
      <h2>Update Recipe</h2>
      <div style={{ marginBottom: '10px' }}>
        <label>Title: </label>
        <input
          type="text"
          name="title"
          value={updatedRecipe.title}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Description: </label>
        <textarea
          name="description"
          value={updatedRecipe.description}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Ingredients: </label>
        <input
          type="text"
          name="ingredients"
          value={updatedRecipe.ingredients}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Prep Time: </label>
        <input
          type="number"
          name="prepTime"
          value={updatedRecipe.prepTime}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Nutrition Info: </label>
        <input
          type="text"
          name="nutritionInfo"
          value={updatedRecipe.nutritionInfo}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Cuisine Type: </label>
        <input
          type="text"
          name="cuisineType"
          value={updatedRecipe.cuisineType}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Meal Type: </label>
        <input
          type="text"
          name="mealType"
          value={updatedRecipe.mealType}
          onChange={handleChange}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Ratings Average: </label>
        <input
          type="number"
          name="ratingsAverage"
          value={updatedRecipe.ratingsAverage}
          onChange={handleChange}
        />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <button onClick={handleSave}>Save</button>
        <button onClick={onClose} style={{ marginLeft: '10px' }}>
          Cancel
        </button>
      </div>
    </div>
  );
};

export default UpdateRecipeModal;
