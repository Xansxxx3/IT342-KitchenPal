package com.example.mealplanner.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mealplanner.R
import com.example.mealplanner.network.Recipe
import com.example.mealplanner.network.RetrofitClient

@Composable
fun ShoppingCartScreen(
    itemId: Long,
    navHostController: NavHostController
) {
    // Back button
    BackHandler { navHostController.popBackStack() }

    val context = LocalContext.current
    var item by remember { mutableStateOf<Recipe?>(null) }

    LaunchedEffect(itemId) {
        val token = getJwtToken(context) ?: return@LaunchedEffect
        item = RetrofitClient.RetrofitInstance.api
            .getAllRecipes("Bearer $token")
            .find { it.id == itemId }
    }

    Box(Modifier.fillMaxSize()) {
        // Top bar
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F877))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = item?.title ?: "Loading…",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item?.let { recipe ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Image
                Image(
                    painter = rememberAsyncImagePainter(recipe.imagePath),
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.height(16.dp))

                // Quantity & Price
                var qty by remember { mutableStateOf(1) }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Quantity:", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = qty.toString(),
                        onValueChange = { qty = it.toIntOrNull() ?: qty },
                        singleLine = true,
                        modifier = Modifier.width(80.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Price: \$${"%.2f".format(recipe.price * qty)}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { /* TODO: checkout */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8CE196))
                ) {
                    Text("Buy Now")
                }
            }
        } ?: run {
            // loading spinner
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

// same helper
private fun getJwtToken(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("jwt_token", null)
}
