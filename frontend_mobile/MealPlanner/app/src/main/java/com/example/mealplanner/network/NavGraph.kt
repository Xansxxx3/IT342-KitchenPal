package com.example.mealplanner.network

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mealplanner.ui.screens.SignInScreen
import com.example.mealplanner.ui.screens.SignUpScreen
import com.example.mealplanner.ui.screens.HomeScreen
import com.example.mealplanner.ui.screens.MealPlanScreen
import com.example.mealplanner.ui.screens.RecipesScreen
import com.example.mealplanner.ui.screens.SettingsScreen
import com.example.mealplanner.ui.screens.ShoppingScreen


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            SignInScreen(navController) { username, password ->
                // SignIn logic here
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("signup") {
            SignUpScreen(navController) { email, firstName, lastName, password ->
                // Sign-up logic here
                println("Sign-up info: $email, $firstName, $lastName, $password")
                // Optionally, navigate to the home screen after successful sign-up
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("home") {
            HomeScreen(navController) // This will be your homepage screen
        }
        composable("meal plan") {
            MealPlanScreen(navController) // This will be your meal plan screen
        }
        composable("recipes") {
            RecipesScreen(navController) // This will be your recipes screen
        }
        composable("shopping") {
            ShoppingScreen(navController) // This will be your shopping screen
        }
        composable("settings") {
            SettingsScreen(navController) // This will be your settings screen
        }
        composable("oauth2-redirect") { backStackEntry ->
            // Extract the query parameters from the URL


            val uri = backStackEntry.arguments?.getString("uri") ?: ""
            val token = uri.substringAfter("token=").substringBefore("&")
            val role = uri.substringAfter("role=").substringBefore("&")
            val userId = uri.substringAfter("userId=")

            // Store the token and user data locally (e.g., SharedPreferences)
            val context = LocalContext.current
            val sharedPref = context.getSharedPreferences("aut" +
                    "h_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().putString("jwt_token", token).apply()
            sharedPref.edit().putString("user_role", role).apply()
            sharedPref.edit().putString("user_id", userId).apply()

            // Navigate to Home screen after successful login
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}