package com.example.mealplanner.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealplanner.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipesScreen(navHostController: NavHostController) {
    // Handle Android back
    val activity = LocalContext.current as? Activity
    var selectedItem by remember { mutableStateOf(3) } // Recipes index
    BackHandler(enabled = true) { activity?.finish() }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background image (unchanged)
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            // 2. TopNavBar (unchanged)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F877))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "itchen Pal",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { /* Profile tap */ }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // 3. Welcome overlay (unchanged)
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "WELCOME TO",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
                Text(
                    text = "KitchenPal",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    color = Color.Black
                )
            }

            // 4. White card with recipe grid
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Recipe Collection",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )

                    // ← Here’s the grid with the clickable onClick you already added:
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val recipes = List(12) { "Binangkal" }
                        items(recipes) { name ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .clickable {
                                        navHostController.navigate("recipeDetail/$name")
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 8.dp,
                                                topEnd = 8.dp
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFF8CE196),
                                            RoundedCornerShape(
                                                bottomStart = 8.dp,
                                                bottomEnd = 8.dp
                                            )
                                        )
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = name)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. BottomNavBar (unchanged)
        NavigationBar(
            containerColor = Color(0xFFF8F877),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val items = listOf(
                Icons.Default.Settings to "Settings",
                Icons.Default.Search   to "Meal Plan",
                Icons.Default.Home     to "Home",
                Icons.Default.Favorite to "Recipes",
                Icons.Default.ShoppingCart to "Shopping"
            )
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.first, contentDescription = item.second) },
                    label = { Text(item.second) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        when (index) {
                            0 -> navHostController.navigate("settings")
                            1 -> navHostController.navigate("mealPlan")
                            2 -> navHostController.navigate("home")
                            3 -> navHostController.navigate("recipes")
                            4 -> navHostController.navigate("shopping")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeDetailScreen(
    navHostController: NavHostController,
    recipeName: String
) {
    BackHandler(enabled = true) { navHostController.popBackStack() }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // TopNavBar style
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F877))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipeName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Action buttons
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* Shop ingredients */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8CE196))
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Shop Ingredients")
                }
                Button(
                    onClick = { /* Add to meal plan */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8CE196))
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Meal Plan")
                }
            }

            // Recipe image
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "• 1¼ cup all-purpose flour\n• 3/4 cup powdered milk\n",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Directions:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "1. In a large bowl…\n2. In another bowl…\n3. …",
                modifier = Modifier.padding(16.dp)
            )
        }

        // BottomNavBar unchanged
        NavigationBar(
            containerColor = Color(0xFFF8F877),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val items = listOf(
                Icons.Default.Settings to "Settings",
                Icons.Default.Search   to "Meal Plan",
                Icons.Default.Home     to "Home",
                Icons.Default.Favorite to "Recipes",
                Icons.Default.ShoppingCart to "Shopping"
            )
            items.forEach { (icon, _) ->
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = null) },
                    label = { /* no label on detail */ Text("") },
                    selected = false,
                    onClick = { /* can disable or handle as needed */ }
                )
            }
        }
    }
}