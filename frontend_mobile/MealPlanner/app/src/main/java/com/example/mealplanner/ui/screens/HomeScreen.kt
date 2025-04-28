package com.example.mealplanner.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealplanner.R

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontStyle

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val activity = LocalContext.current as? Activity
    var selectedItem by remember { mutableStateOf(2) } // 0 = Settings, 1 = Meal Plan, 2 = Home

    BackHandler(enabled = true) {
        activity?.finish()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
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
            // Header Section
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
                        modifier = Modifier.height(60.dp)
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
                        .clickable {  }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Main content
            HomeContent();
        }

        // Footer Navigation Bar
        NavigationBar(
            containerColor = Color(0xFFF8F877),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val items = listOf(
                Pair(Icons.Default.Settings, "Settings"),
                Pair(Icons.Default.Search, "Meal Plan"),
                Pair(Icons.Default.Home, "Home"),
                Pair(Icons.Default.Favorite, "Recipes"),
                Pair(Icons.Default.ShoppingCart, "Shopping")
            )

            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.first, contentDescription = item.second) },
                    label = { Text(item.second) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index

                        when(index)
                        {
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
private fun HomeContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),  // or .fillMaxSize()
        contentAlignment = Alignment.Center
    ) {
        // semi-transparent card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
                .aspectRatio(0.7f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // logo + title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "itchenPal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))
                // divider line
                Divider(color = Color.LightGray, thickness = 1.dp)

                Spacer(Modifier.height(12.dp))
                // body: text + image
                Row {
                    // description text
                    Text(
                        text = "KitchenPal makes meal preparation simple and efficient. " +
                                "On our Recipe page, explore a wide variety of dishes and decide " +
                                "how you want to organize your meals. With just a click, you " +
                                "can add recipes directly to your Meal Plan to structure your " +
                                "days or to your Shopping List to ensure you have all the " +
                                "ingredients ready. Whether you’re planning meals for the day " +
                                "or creating a grocery list, KitchenPal gives you the flexibility " +
                                "to stay organized and focus on what matters most – enjoying " +
                                "delicious meals!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(0.55f)
                            .verticalScroll(rememberScrollState())
                    )

                    Spacer(Modifier.width(8.dp))

                    // dish image
                    Image(
                        painter = painterResource(id = R.drawable.plate_dish),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(0.45f)
                            .clip(RoundedCornerShape(8.dp))
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
