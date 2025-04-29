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
import androidx.compose.foundation.lazy.grid.items // ✅ this is the key import
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mealplanner.R
import com.example.mealplanner.network.RetrofitClient
import com.example.mealplanner.network.ShoppingListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? Activity
    BackHandler { activity?.finish() }

    var selectedItem by remember { mutableStateOf(4) }
    val shoppinglist = remember { mutableStateOf<List<ShoppingListItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = sharedPref.getString("jwt_token", null)

            if (token != null) {
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.apiService.getAllShoppingList(bearerToken)
                shoppinglist.value = response
            } else {
                println("No token found. Maybe user not logged in?")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F877))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "itchen Pal",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { }
                ) {
                    Image(
                        painter = painterResource(R.drawable.dropdown),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Your Shopping List",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
            )

            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val sList = shoppinglist.value
                    items(sList, key = { it.shoppingListItemId ?: 0L }) { shoppingListItem ->
                        val firstRecipe = shoppingListItem.recipes.firstOrNull()

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable {
                                    navHostController.navigate("cart/${shoppingListItem.shoppingListItemId}")
                                }
                        ) {
                            if (firstRecipe != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(firstRecipe.imagePath),
                                    contentDescription = firstRecipe.title,
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

                                Spacer(Modifier.height(4.dp))

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
                                    Text(
                                        text = firstRecipe.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = "Qty: ${shoppingListItem.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = shoppingListItem.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (shoppingListItem.status.equals("PENDING", true))
                                        Color.Yellow else Color.Green,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                            } else {
                                Text(
                                    text = "No recipe",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        NavigationBar(
            containerColor = Color(0xFFF8F877),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val itemsNav = listOf(
                Icons.Default.Settings to "Settings",
                Icons.Default.Search to "Meal Plan",
                Icons.Default.Home to "Home",
                Icons.Default.Favorite to "Recipes",
                Icons.Default.ShoppingCart to "Shopping"
            )
            itemsNav.forEachIndexed { idx, (icon, label) ->
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) },
                    selected = selectedItem == idx,
                    onClick = {
                        selectedItem = idx
                        when (idx) {
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
