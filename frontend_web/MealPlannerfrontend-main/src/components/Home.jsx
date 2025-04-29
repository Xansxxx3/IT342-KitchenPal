import React from 'react';
import { Box, Container, Typography, Grid } from '@mui/material';
import NavBar from '../components/NavBar'; // Assuming NavBar is still needed
import backgroundImage from '../assets/leafbg.png'; // Background image
import Slider from 'react-slick'; // For the image slider

// Import Slider Images
import sliderImage1 from '../assets/Recipes/Cabbage Roll Casserole.png';
import sliderImage2 from '../assets/Recipes/Carrot Cake Based Oatmeal.png';
import sliderImage3 from '../assets/Recipes/Crispy Baked Catfish.png';
import sliderImage4 from '../assets/Recipes/Green Goddess Farro Bowl.png';

const Home = () => {
  // Slider Settings
  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    arrows: false,
  };

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        position: 'relative',
      }}
    >
      {/* Background Overlay */}
      <Box
        sx={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          backgroundColor: 'rgba(0, 0, 0, 0.6)',
          zIndex: -1,
        }}
      />

      {/* NavBar Component */}
      <NavBar />

      <Container sx={{ flex: 1, padding: '40px 20px', marginTop: '50px' }}> {/* Added marginTop */}
  <Grid
    container
    spacing={4}
    sx={{
      alignItems: 'center',
      justifyContent: 'space-between',
    }}
  >
    {/* Left Column: Logo and Description */}
    <Grid item xs={12} md={6} sx={{ display: 'flex', justifyContent: 'left', marginTop: '120px'}}> {/* Added marginTop */}
      <Box
        sx={{
          position: 'relative',
          backgroundColor: 'rgba(255, 255, 255, 0.7)', // Semi-transparent background
          borderRadius: '30px',
          padding: '40px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
          width: '100%',
          maxWidth: '750px',
          textAlign: 'center',
          zIndex: 1,
          height: 'auto',
        }}
      >
        {/* Logo and Description */}
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
          KitchenPal
        </Typography>
        <Typography
          variant="body1"
          sx={{
            fontSize: '1.28rem',
            lineHeight: 1.8,
            color: '#333',
          }}
        >
         <span style={{ color: '#FFD700', fontWeight: 'bold' }}>KitchenPal</span> makes meal preparation simple and efficient. On our Recipe page, explore a wide variety of dishes and decide how you want to organize your meals. With just a click, you can add recipes directly to your Meal Plan to structure your days or to your Shopping List to ensure you have all the ingredients ready. Whether you’re planning meals for the day or creating a grocery list, <span style={{ color: '#FFD700' }}>KitchenPal</span> gives you the flexibility to stay organized and focus on what matters most – enjoying delicious meals!

        </Typography>
      </Box>
    </Grid>

<Grid item xs={12} md={6} sx={{ display: 'flex', justifyContent: 'flex-end', marginTop: '120px' }}> {/* Using flex-end */}
  <Box
    sx={{
      borderRadius: '40px',
      overflow: 'hidden',
      boxShadow: '0 8px 20px rgba(0, 0, 0, 0.4)',
      width: '120%',
      maxWidth: '850px', // Limiting width of the slider
      marginLeft: 'auto', // Ensures it aligns to the right
    }}
  >
    <Slider {...sliderSettings}>
      <img src={sliderImage1} alt="Dish 1" style={{ width: '100%', height: 'auto' }} />
      <img src={sliderImage2} alt="Dish 2" style={{ width: '100%', height: 'auto' }} />
      <img src={sliderImage3} alt="Dish 3" style={{ width: '100%', height: 'auto' }} />
      <img src={sliderImage4} alt="Dish 4" style={{ width: '100%', height: 'auto' }} />
    </Slider>
  </Box>
</Grid>

  </Grid>
</Container>


      {/* Footer */}
      <Box
        sx={{
          backgroundColor: 'rgba(0, 0, 0, 0.6)', // Gray with opacity
          padding: '20px 0',
          textAlign: 'center',
          color: '#fff',
        }}
      >
        <Typography variant="body2" sx={{ fontSize: '1rem' }}>
          © 2024 KitchenPal. All rights reserved.
        </Typography>
        <Typography variant="body2" sx={{ fontSize: '1rem' }}>
          Email us: <a href="mailto:KitchenPal@gmail.com" style={{ color: '#FFD700' }}>KitchenPal@gmail.com</a>
        </Typography>
      </Box>
    </Box>
  );
};

export default Home;
