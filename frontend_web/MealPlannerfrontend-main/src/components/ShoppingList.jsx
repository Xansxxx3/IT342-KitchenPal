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
  Snackbar,
  Alert,
  Slide,
  IconButton,
  Divider,
  Paper,
  Fade,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import backgroundImage from "../assets/leafbg.png";
import NavBar from "../components/NavBar";
import EditableText from "./EditableText";
import { useNavigate } from "react-router-dom";
import DeleteSweepIcon from '@mui/icons-material/DeleteSweep';
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import CheckBoxOutlineBlankIcon from "@mui/icons-material/CheckBoxOutlineBlank";

export default function ShoppingList() {
  const navigate = useNavigate();
  const [items, setItems]         = useState([]);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMsg, setSnackbarMsg]   = useState("");
  const [snackbarType, setSnackbarType] = useState("success");

  const userId = localStorage.getItem("userId");
  const token  = localStorage.getItem("token");
  const API    = "https://it342-kitchenpal.onrender.com";


  useEffect(() => {
    if (!userId || !token) {
      navigate("/login");
      return;
    }

    const fetchList = async () => {
      try {
        const resp = await axios.get(${API}/api/shopping-list, {
          headers: { Authorization: Bearer ${token} },
        });
        if (Array.isArray(resp.data)) {
          setItems(resp.data);
        } else {
          throw new Error("Invalid response format");
        }
      } catch (err) {
        console.error(err);
        setError("Failed to load shopping list");
      } finally {
        setLoading(false);
      }
    };

    fetchList();
  }, [navigate, userId, token]);

  const refresh = () =>
    axios
      .get(${API}/api/shopping-list, {
        headers: { Authorization: Bearer ${token} },
      })
      .then(r => setItems(r.data))
      .catch(console.error);

  // group flat items by recipeId
  const recipes = Object.values(
    items.reduce((acc, item) => {
      const rid = item.recipeId;
      if (!acc[rid]) {
        acc[rid] = {
          recipeId: rid,
          title: item.recipeTitle,
          imagePath: item.recipeImagePath,
          description: item.recipeDescription,
          ingredients: [],         // will fill with per-item names
          listItems: [],           // will hold the individual ShoppingListItemDTOs
        };
      }
      acc[rid].listItems.push(item);
      return acc;
    }, {})
  );

  const toggleCheck = async (itm) => {
    try {
      await axios.put(
        ${API}/api/shopping-list/item/${itm.id},
        { checked: !itm.checked },
        { headers: { Authorization: Bearer ${token} } }
      );
      refresh();
    } catch {
      setSnackbarMsg("Failed to toggle check");
      setSnackbarType("error");
      setOpenSnackbar(true);
    }
  };

  const updateField = async (id, field, val) => {
    try {
      await axios.put(
        ${API}/api/shopping-list/item/${id},
        { [field]: val },
        { headers: { Authorization: Bearer ${token} } }
      );
      refresh();
    } catch {
      setSnackbarMsg(Failed to update ${field});
      setSnackbarType("error");
      setOpenSnackbar(true);
    }
  };

  const clearChecked = async () => {
    try {
      await axios.delete(${API}/api/shopping-list/clear-checked, {
        headers: { Authorization: Bearer ${token} },
      });
      refresh();
      setSnackbarMsg("Cleared all checked items");
      setSnackbarType("success");
      setOpenSnackbar(true);
    } catch {
      setSnackbarMsg("Failed to clear checked items");
      setSnackbarType("error");
      setOpenSnackbar(true);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 8 }}>
        <CircularProgress />
      </Box>
    );
  }
  if (error) {
    return (
      <Typography color="error" align="center" mt={4}>
        {error}
      </Typography>
    );
  }

const handleToggleCheck = async (itm) => {
  // optimistic UI update
  setSelectedRecipe(r => ({
    ...r,
    listItems: r.listItems.map(i =>
      i.id === itm.id ? { ...i, checked: !i.checked } : i
    )
  }));
  try {
    await axios.put(
      ${API}/api/shopping-list/item/${itm.id},
      { checked: !itm.checked },
      { headers: { Authorization: Bearer ${token} } }
    );
    setSnackbarMsg("Successfully Checked Ingredient");
    setSnackbarType("success");
    setOpenSnackbar(true);
  } catch {
    setSnackbarMsg("Failed to update check");
    setSnackbarType("error");
    setOpenSnackbar(true);
  }
};

const handleSaveField = async (itm, field, val) => {
  // optimistic UI update
  setSelectedRecipe(r => ({
    ...r,
    listItems: r.listItems.map(i =>
      i.id === itm.id ? { ...i, [field]: val } : i
    )
  }));
  try {
    await axios.put(
      ${API}/api/shopping-list/item/${itm.id},
      { [field]: val },
      { headers: { Authorization: Bearer ${token} } }
    );
    setSnackbarMsg(${field} successfully changed);
    setSnackbarType("success");
    setOpenSnackbar(true);
  } catch {
    setSnackbarMsg(Failed to save ${field});
    setSnackbarType("error");
    setOpenSnackbar(true);
  }
};
  return (
    <Box
          sx={{
            backgroundImage: url(${backgroundImage}),
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
              Shopping List
            </Typography>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            mt: 4,
            mb: 3,
          }}
        >
          <Button
            variant="contained"
            startIcon={
              <DeleteSweepIcon
                sx={{
                  fontSize: '1.8rem',
                  transition: 'transform 0.3s ease',
                  '.MuiButton-root:hover &': {
                    transform: 'rotate(-10deg) scale(1.1)',
                  },
                }}
              />
            }
            onClick={clearChecked}
            sx={{
              backgroundColor: '#F76C5E',
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
                backgroundColor: '#D9534F',
                transform: 'translateY(-2px)',
                boxShadow: '0px 6px 15px rgba(0, 0, 0, 0.2)',
              },
            }}
          >
            Clear All Checked
          </Button>
        </Box>


        <Grid container spacing={4}>
          {recipes.map(recipe => (
            <Grid item xs={12} sm={6} md={4} key={recipe.recipeId}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image={recipe.imagePath}
                  alt={recipe.title}
                />
                <CardContent>
                  <Typography variant="h6" noWrap>
                    {recipe.title}
                  </Typography>
                  <Typography variant="body2" color="textSecondary" noWrap>
                    {recipe.description}
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small" onClick={() => setSelectedRecipe(recipe)}>
                    View Details
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Recipe Detail Modal */}
     <Modal open={!!selectedRecipe} onClose={() => setSelectedRecipe(null)} closeAfterTransition>
       <Fade in={!!selectedRecipe}>
         <Box
           sx={{
             position: "absolute",
             top: "50%",
             left: "50%",
             transform: "translate(-50%, -50%)",
             width: 600,
             maxHeight: "90vh",
             overflowY: "auto",
             bgcolor: "#fff",
             borderRadius: 3,
             boxShadow: 24,
             p: 4,
             zIndex: 1300,
           }}
         >
           {selectedRecipe && (
             <>
               <IconButton
                 onClick={() => setSelectedRecipe(null)}
                 sx={{ position: "absolute", top: 8, right: 8, color: "#888" }}
               >
                 <CloseIcon />
               </IconButton>

               <Paper elevation={3} sx={{ borderRadius: 2, overflow: "hidden", mb: 2 }}>
                 <CardMedia
                   component="img"
                   height="200"
                   image={selectedRecipe.imagePath}
                   alt={selectedRecipe.title}
                 />
               </Paper>

               <Typography
                 variant="h4"
                 sx={{
                   fontWeight: 700,
                   color: "#72BF78",
                   mb: 2,
                 }}
                 gutterBottom
               >
                 {selectedRecipe.title}
               </Typography>

               <Typography variant="body1" sx={{ mb: 2, color: "#444" }}>
                 {selectedRecipe.description}
               </Typography>

               <Divider sx={{ my: 2 }} />

               <Typography
                 variant="h6"
                 sx={{
                   fontWeight: 600,
                   color: "#72BF78",
                   mb: 2,
                 }}
               >
                 🛒 Shopping List
               </Typography>

               <Box>
                 {selectedRecipe.listItems.map((itm) => (
                   <Box
                     key={itm.id}
                     sx={{
                       display: "flex",
                       alignItems: "center",
                       justifyContent: "space-between",
                       mb: 1,
                       p: 1,
                       borderRadius: 1,
                       bgcolor: itm.checked ? "#e8f5e9" : "#f5f5f5",
                     }}
                   >
                     <Box sx={{ display: "flex", alignItems: "center" }}>
                       <IconButton onClick={() => handleToggleCheck(itm)} size="small">
                         {itm.checked ? <CheckBoxIcon color="success" /> : <CheckBoxOutlineBlankIcon />}
                       </IconButton>
                       <EditableText
                         value={itm.name}
                         onSave={(v) => handleSaveField(itm, "name", v)}
                         sx={{ ml: 1, fontWeight: 500 }}
                       />
                     </Box>

                     <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                       <EditableText
                         value={itm.quantity || ""}
                         placeholder="qty"
                         onSave={(v) => handleSaveField(itm, "quantity", v)}
                       />
                       <EditableText
                         value={itm.notes || ""}
                         placeholder="notes"
                         onSave={(v) => handleSaveField(itm, "notes", v)}
                       />
                     </Box>
                   </Box>
                 ))}
               </Box>

               <Divider sx={{ my: 3 }} />

               <Box textAlign="right">
                 <Button
                   variant="contained"
                   sx={{
                     backgroundColor: "#72BF78",
                     color: "#fff",
                     fontWeight: "bold",
                     '&:hover': {
                       backgroundColor: "#5aa867",
                     }
                   }}
                   onClick={() => setSelectedRecipe(null)}
                 >
                   Close
                 </Button>
               </Box>
             </>
           )}
         </Box>
       </Fade>
     </Modal>


      <Snackbar
        open={openSnackbar}
        autoHideDuration={3000}
        onClose={() => setOpenSnackbar(false)}
        TransitionComponent={props => <Slide {...props} direction="up" />}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          severity={snackbarType}
        >
          {snackbarMsg}
        </Alert>
      </Snackbar>
    </Box>
  );
}