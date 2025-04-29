package com.example.mealplanner.ui.screens

import android.app.Activity
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
import com.example.mealplanner.network.RetrofitClient
import com.example.mealplanner.network.ShoppingListItem

@Composable
fun ShoppingCartScreen(
    itemId: Long,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // 1) Handle Android back from top bar
    BackHandler { navHostController.popBackStack() }

    // 2) State holding the full list
    val shoppinglist = remember { mutableStateOf<List<ShoppingListItem>>(emptyList()) }

    // 3) Your fetch logic (LEFT EXACTLY AS-IS)
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

    // 4) This was missing — let the hardware/back gesture also finish the Activity if needed
    BackHandler(enabled = true) { activity?.finish() }

    // 5) Derive the single ShoppingList entry from the list by itemId
    val item: ShoppingListItem? =
        shoppinglist.value.firstOrNull { it.shoppingListItemId == itemId }

    // 6) From that, grab the first Recipe (if any)
    val recipe = item?.recipes?.firstOrNull()

    Box(Modifier.fillMaxSize()) {
        // — Top bar with back arrow + title —
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
                text = recipe?.title ?: "Loading…",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // — Main scrollable content —
        recipe?.let {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Recipe image
                Image(
                    painter = rememberAsyncImagePainter(it.imagePath),
                    contentDescription = it.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.height(16.dp))

                // Quantity field
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

                // Price calculation (remove or adjust if your Recipe has no price field)
                Text(
                    text = "Price: \$${"%.2f".format(/* it.price */ 0.0 * qty)}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(24.dp))

                // Buy button
                Button(
                    onClick = { /* TODO: checkout */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8CE196))
                ) {
                    Text("Buy Now")
                }
            }
        } ?: run {
            // Loading spinner if no recipe loaded yet
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

// helper to grab stored JWT
private fun getJwtToken(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("jwt_token", null)
}