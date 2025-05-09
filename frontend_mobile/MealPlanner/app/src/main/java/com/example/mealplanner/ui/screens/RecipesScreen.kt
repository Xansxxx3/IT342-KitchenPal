package com.example.mealplanner.ui.screens

import android.app.Activity
import android.content.Context
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
import coil.compose.AsyncImage
import com.example.mealplanner.R
import com.example.mealplanner.network.MealPlanAdd
import com.example.mealplanner.network.MealPlanRequest
import com.example.mealplanner.network.Recipe
import com.example.mealplanner.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipesScreen(navHostController: NavHostController) {
    // Handle Android back
    val activity = LocalContext.current as? Activity
    var selectedItem by remember { mutableStateOf(3) } // Recipes index
    val context = LocalContext.current


    val recipes = remember { mutableStateOf<List<Recipe>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = sharedPref.getString("jwt_token", null)

            if (token != null) {
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.apiService.getAllRecipes(bearerToken)
                recipes.value = response
            } else {
                println("No token found. Maybe user not logged in?")
                // Optional: Navigate to login screen or show error
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
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
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "itchenPal",
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
                        val recipeList = recipes.value
                        items(recipeList) { recipe ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .clickable {
                                        navHostController.navigate("recipeDetail/${recipe.title}") // or recipe.title
                                    }
                            ) {
                                AsyncImage( // use Coil for loading URL images
                                    model = recipe.imagePath,
                                    contentDescription = recipe.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFF8CE196),
                                            RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                                        )
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = recipe.title)
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
                            1 -> navHostController.navigate("meal plan")
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

    var selectedItem by remember { mutableStateOf(3) } // Recipes index
    val context = LocalContext.current
    var recipe: Recipe? by remember { mutableStateOf<Recipe?>(null) }
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val token = sharedPref.getString("jwt_token", null)
    val userid = sharedPref.getString("user_id", null)

    println("Login Success in main: ${recipeName}")

    LaunchedEffect(recipeName) {
        try {
            if (token != null) {
                val bearerToken = "Bearer $token"
                val allRecipes = RetrofitClient.apiService.getAllRecipes(bearerToken)

                recipe = allRecipes.find { it.title == recipeName }
                println("Login Success in main: ${recipe?.title}")
                println("recipe ID: ${recipe?.id}")

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        if (recipe != null) {
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
                        text = recipe?.title ?: "",
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
                        onClick = {
                            val recipeId = recipe?.id
                            if (recipeId != null) {
                                val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                val token = sharedPref.getString("jwt_token", null)
                                val userid = sharedPref.getString("user_id", null)
                                println("Login Success in main: ${token}")
                                println("Login Success in main: ${userid}")
                                val request = MealPlanAdd(userid.toString(), recipeId)
                                println("Login Success in main: ${request.recipeId}")
                                println("Login Success in main: ${request.userId}")
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val bearerToken = "Bearer $token"
                                        RetrofitClient.apiService.addMealPlan(bearerToken, request)
                                        // Maybe show success UI after?
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        // Maybe show error UI
                                    }
                                }
                            } else {
                                println("Recipe ID is null!")
                            }
                        /* Add to meal plan */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8CE196))
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Meal Plan")
                    }
                }

                // Recipe image
                if (!recipe?.imagePath.isNullOrEmpty()) {
                    AsyncImage(
                        model = recipe?.imagePath,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                recipe?.ingredients?.forEach { ingredient ->
                    Text(
                        text = "- $ingredient",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }


                Text(
                    text = "Nutrition Info:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = recipe?.nutritionInfo ?: "",
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Preparation Time: ${recipe?.prepTime ?: ""} minutes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            // Loading or Error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
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
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.first, contentDescription = item.second) },
                    label = { Text(item.second) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        when (index) {
                            0 -> navHostController.navigate("settings")
                            1 -> navHostController.navigate("meal plan")
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