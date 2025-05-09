import React, { useEffect, useState } from "react";
import {
  Container,
  Typography,
  Grid,
  Card,
  CardMedia,
  CardContent,
  CardHeader,
  Avatar,
  IconButton,
  CircularProgress,
  Box,
  Button,
  Modal,
  TextField,
  Snackbar,
  Alert,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import axios from "axios";
import NavBar from "../components/NavBar";
import Slide from '@mui/material/Slide';
import MoreVertIcon from "@mui/icons-material/MoreVert";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import SaveIcon from '@mui/icons-material/Save';
import SendIcon from '@mui/icons-material/Send';
import backgroundImage from "../assets/leafbg.png"; // Background image

const SocialFeed = () => {
  const [sharedMealPlans, setSharedMealPlans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMealPlan, setSelectedMealPlan] = useState(null);
  const [open, setOpen] = useState(false);
  const [comments, setComments] = useState([]);
  const [commentText, setCommentText] = useState("");
  const [anchorEl, setAnchorEl] = useState(null); // for dropdown sa caption
  const [editingCaption, setEditingCaption] = useState(false); // for dropdown sa caption
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success'); // 'success' or 'error'

  const token = localStorage.getItem("token");
  const loggedInUserId = localStorage.getItem("userId"); // assuming you store userId when logging in

const [captionText, setCaptionText] = useState('');

    useEffect(() => {
      if (selectedMealPlan) {
        setCaptionText(selectedMealPlan.caption || '');
      }
    }, [selectedMealPlan]);

  const fetchSharedMealPlans = async () => {
    try {
      const response = await axios.get("https://it342-kitchenpal.onrender.com/api/meal-plans/shared");
      setSharedMealPlans(response.data);
    } catch (error) {
      console.error("Error fetching shared meal plans", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSharedMealPlans();
  }, []);

  const handleDelete = async (mealPlanId) => {
    try {
      await axios.put(
        `https://it342-kitchenpal.onrender.com/api/meal-plans/unshare/${mealPlanId}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setMessage(" Meal plan removed from social feed.");
      setMessageType("success");
      setOpenSnackbar(true);
      fetchSharedMealPlans(); // Refresh feed
    } catch (error) {
      console.error("Error deleting shared meal plan", error);
      setMessage(" Failed to remove meal plan.");
      setMessageType("error");
      setOpenSnackbar(true);
    }
  };


  const handleOpen = async (mealPlan) => {
    setSelectedMealPlan(mealPlan);
    setCaptionText(mealPlan.caption || ''); // Fix: set caption directly
    setOpen(true);


    try {
      const res = await axios.get(
        `https://it342-kitchenpal.onrender.com/api/meal-plans/${mealPlan.mealPlanId}/comments`
      );
      setComments(res.data);
    } catch (err) {
      console.error("Error fetching comments:", err);
    }
  };

  const handleClose = () => {
    setSelectedMealPlan(null);
    setOpen(false);
    setComments([]);
    setCommentText("");
  };

  const handleCommentSubmit = async () => {
    if (!commentText.trim()) return;
    try {
      const res = await axios.post(
        `https://it342-kitchenpal.onrender.com/api/meal-plans/${selectedMealPlan.mealPlanId}/comments`,
        { text: commentText },
        { headers: { Authorization: `Bearer ${token}` },
      });
      setComments((prev) => [...prev, res.data]);
      setCommentText("");

      setMessage("Comment posted successfully!");
      setMessageType("success");
      setOpenSnackbar(true);
    } catch (err) {
      console.error("Error submitting comment:", err);
      setMessage(" Failed to post comment.");
      setMessageType("error");
      setOpenSnackbar(true);
    }
  };

   const handleDeleteComment = async (commentId) => {
     try {
       await axios.delete(
         `https://it342-kitchenpal.onrender.com/api/meal-plans/${selectedMealPlan.mealPlanId}/comments/${commentId}`,
         {
           headers: { Authorization: `Bearer ${token}` },
         }
       );
       setComments((prevComments) => prevComments.filter((comment) => comment.commentId !== commentId));

       // Show success snackbar
       setMessage("Comment deleted successfully!");
       setMessageType("success");
       setOpenSnackbar(true);
     } catch (error) {
       console.error("Failed to delete comment", error);

       // Show error snackbar
       setMessage(" Failed to delete comment.");
       setMessageType("error");
       setOpenSnackbar(true);
     }
   };

   const handleCaptionSubmit = async () => {
     try {
       await axios.put(
         `https://it342-kitchenpal.onrender.com/api/meal-plans/${selectedMealPlan.mealPlanId}/caption`,
         { caption: captionText },
         { headers: { Authorization: `Bearer ${token}` } }
       );

       // Instead of re-fetching, just update the selectedMealPlan caption immediately
       setSelectedMealPlan((prev) => ({
         ...prev,
         caption: captionText,
       }));

       setMessage("Caption updated successfully!");
       setMessageType("success");
       setOpenSnackbar(true);
     } catch (error) {
       console.error('Error updating caption:', error);
       setMessage(" Failed to update caption.");
       setMessageType("error");
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
          color: '#fff',
        }}
      >
      <NavBar />
      <Container sx={{ paddingTop: 4 }}>
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
                  Shared Meal Plans
                </Typography>

        {loading ? (
          <CircularProgress />
        ) : sharedMealPlans.length === 0 ? (
          <Typography>No shared meal plans yet.</Typography>
        ) : (
          <Grid container spacing={4}>
            {sharedMealPlans.map((plan) => (
              <Grid item xs={12} sm={6} md={4} key={plan.mealPlanId}>
                <Card>
                  <CardHeader
                    avatar={
                      <Avatar
                        src={
                          plan.user?.profileImagePath
                            ? plan.user.profileImagePath
                            : "https://via.placeholder.com/150?text=Profile"
                        }

                        alt="User Avatar"
                      >
                        {!plan.user?.profileImagePath &&
                          `${plan.user?.fname?.[0] ?? ""}${plan.user?.lname?.[0] ?? ""}`}
                      </Avatar>
                    }
                    action={
                      plan.user?.userId?.toString() === loggedInUserId && (
                        <IconButton onClick={() => handleDelete(plan.mealPlanId)}>
                          <DeleteIcon color="error" />
                        </IconButton>
                      )
                    }
                    title={`${plan.user?.fname} ${plan.user?.lname}`}
                    subheader="Shared a meal plan"
                  />
                 <CardMedia
                   component="img"
                   height="140"
                   image={plan.recipe.imagePath} // Assuming this is a full Cloudinary URL
                   alt={plan.recipe.title}
                 />
                  <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                      {plan.recipe?.title || "Recipe Title Not Available"}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {plan.recipe?.description || "No description available."}
                    </Typography>
                  </CardContent>
                  <Box sx={{ display: "flex", justifyContent: "center", mb: 2 }}>
                    <Button size="small" onClick={() => handleOpen(plan)}>
                      View Details
                    </Button>
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Container>

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
                src={selectedMealPlan.recipe.imagePath}
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
                  {Array.isArray(selectedMealPlan.recipe?.ingredients) ? (
                    selectedMealPlan.recipe.ingredients.map((ingredient, index) => (
                      <li key={index}>{ingredient.trim?.()}</li>
                    ))
                  ) : (
                    <li>No ingredients available.</li>
                  )}
                </ul>
              </Box>
            </Box>

            <Typography variant="h5" sx={{ mt: 3 }}>
              <strong>Description:</strong> {selectedMealPlan.recipe.description}
            </Typography>

            <Box sx={{ mt: 3, display: "flex", flexWrap: "wrap", gap: 3 }}>
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

            {/* Caption Section */}
            <Box sx={{ mt: 4, borderBottom: "1px solid #ccc", pb: 2 }}>
              <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <Typography variant="h6" gutterBottom>
                  <strong>Caption:</strong>
                </Typography>

                {/* Only show the three-dots if user is owner */}
                {selectedMealPlan?.user?.userId?.toString() === loggedInUserId && (
                  <>
                    <IconButton onClick={(e) => setAnchorEl(e.currentTarget)}>
                      <MoreVertIcon />
                    </IconButton>
                    <Menu
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={() => setAnchorEl(null)}
                    >
                      <MenuItem
                        onClick={() => {
                          setEditingCaption(true);
                          setAnchorEl(null);
                        }}
                      >
                        Edit Caption
                      </MenuItem>
                    </Menu>
                  </>
                )}
              </Box>

              {/* Show caption text */}
              <Typography variant="body1" sx={{ mb: 2 }}>
                {selectedMealPlan.caption || "No caption added yet."}
              </Typography>

              {/* If editing mode ON, show TextField + Save button */}
              {editingCaption && (
                <>
                  <TextField
                    fullWidth
                    multiline
                    rows={2}
                    value={captionText}
                    onChange={(e) => setCaptionText(e.target.value)}
                    placeholder="Edit your caption..."
                    sx={{
                      mb: 2,
                      borderRadius: '12px', // Rounded corners

                      '& .MuiOutlinedInput-root': {
                        '& fieldset': {
                          borderColor: '#ccc', // Default border color
                        },
                        '&:hover fieldset': {
                          borderColor: '#8dbf6a', // Border color on hover
                        },
                        '&.Mui-focused fieldset': {
                          borderColor: '#8dbf6a', // Border color when focused
                        },
                      },
                      padding: '10px', // Optional: add some padding for better internal spacing
                    }}
                    variant="outlined"
                  />
                  <Button
                    onClick={async () => {
                      await handleCaptionSubmit();
                      setEditingCaption(false);
                    }}
                    disabled={captionText === (selectedMealPlan.caption || '')}
                    startIcon={<SaveIcon />}
                    sx={{
                      mb: 2,
                      color: '#8dbf6a',
                      backgroundColor: 'white',
                      border: '1px solid #8dbf6a',
                      '&:hover': {
                        backgroundColor: '#f0f5eb',
                      },
                      textTransform: 'none',
                      fontWeight: 'bold',
                    }}
                  >
                    Save Caption
                  </Button>
                </>
              )}
            </Box>


            {/* Comments Section */}
            <Box sx={{ mt: 5 }}>
              <Typography variant="h6" gutterBottom>
                💬 Comments
              </Typography>

             {/* Scrollable Comments Container */}
             <Box sx={{ maxHeight: "300px", overflowY: "auto", mt: 2, pr: 1 }}>
               {comments.map((comment, index) => (
                 <Box
                   key={index}
                   sx={{
                     display: "flex",
                     alignItems: "flex-start",
                     gap: 2,
                     mb: 2,
                     position: "relative", // Add position relative for delete button
                   }}
                 >
                   <Avatar
                     alt={`${comment.firstName} ${comment.lastName}`}
                     src={
                       comment.profileImagePath
                         ? comment.profileImagePath
                         : "https://via.placeholder.com/150?text=Profile"
                     }

                     sx={{ width: 40, height: 40 }}
                   >
                    {!comment.profileImagePath &&
                      `${comment.firstName?.[0] ?? ""}${comment.lastName?.[0] ?? ""}`}

                   </Avatar>
                   <Box>
                     <Typography variant="subtitle2" fontWeight="bold">
                       {comment.firstName} {comment.lastName}
                     </Typography>
                     <Typography variant="body2" color="textSecondary">
                       {new Date(comment.createdAt).toLocaleString()}
                     </Typography>
                     <Typography>{comment.text}</Typography>
                   </Box>

                   {/* Show delete button if it's user's own comment */}
                   {comment.userId?.toString() === loggedInUserId && (
                     <IconButton
                               onClick={() => handleDeleteComment(comment.commentId)}
                               size="small"
                               sx={{
                                 position: "absolute",
                                 top: 4,
                                 right: 4,
                               }}
                             >
                               <DeleteIcon color="error" fontSize="medium" />
                             </IconButton>
                   )}




                 </Box>
               ))}

             </Box>


              {/* Comment Input */}
              <Box
                sx={{
                  display: "flex",
                  gap: 2,
                  mt: 2,
                  alignItems: "center", // center vertically
                }}
              >
                <TextField
                  fullWidth
                  multiline
                  rows={2}
                  placeholder="Write a comment…"
                  value={commentText}
                  onChange={(e) => setCommentText(e.target.value)}
                  variant="outlined"
                  sx={{
                    borderRadius: '12px', // Rounded corners
                    borderColor: '#ccc', // Default border color
                    '& .MuiOutlinedInput-root': {
                      '& fieldset': {
                        borderColor: '#ccc', // Border color for the text field
                      },
                      '&:hover fieldset': {
                        borderColor: '#8dbf6a', // Border color on hover
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: '#8dbf6a', // Border color when focused
                      },
                    },

                    padding: '10px', // Add padding for better spacing inside
                  }}
                />
                <Button
                  variant="outlined"
                  startIcon={<SendIcon />}
                  onClick={handleCommentSubmit}
                  disabled={!commentText.trim()}
                  sx={{
                    color: "#8dbf6a",
                    border: "1px solid #8dbf6a",
                    backgroundColor: "white",
                    textTransform: "none",
                    fontSize: "1rem",
                    px: 3,
                    py: 1.25,
                    minWidth: "120px",
                    borderRadius: "12px",
                    "&:hover": {
                      backgroundColor: "#f0f5eb",
                    },
                    "&:disabled": {
                      color: "#ccc",
                      borderColor: "#ccc",
                      backgroundColor: "#f9f9f9",
                      cursor: "default",
                    },
                  }}
                >
                  Post
                </Button>
              </Box>
            </Box>
          </Box>
        </Modal>
      )}
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

export default SocialFeed;
