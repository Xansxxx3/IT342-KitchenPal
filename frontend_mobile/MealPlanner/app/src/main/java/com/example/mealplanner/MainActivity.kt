package com.example.mealplanner

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mealplanner.network.AppNavGraph

var isOAuthLogin = false
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().remove("jwt_token").apply()
        super.onCreate(savedInstanceState)

        // Handle immersive mode
        window.decorView.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        )
            }
        }

        // Handle OAuth redirect
        intent?.let { handleOAuthRedirect(it) }

        // Set up the content view using Jetpack Compose
        setContent {
            MealPlannerApp()
        }
    }

    // Handle the OAuth2 redirect
    private fun handleOAuthRedirect(intent: Intent) {
        if (intent.data != null && intent.data!!.scheme == "myapp") {
            val token = intent.data?.getQueryParameter("token")
            val role = intent.data?.getQueryParameter("role")
            val userId = intent.data?.getQueryParameter("userId")


            println("Login Success in main: ${token}")

            val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().putString("jwt_token", token).apply()
            sharedPref.edit().putString("user_role", role).apply()
            sharedPref.edit().putString("user_id", userId).apply()

            isOAuthLogin = true

        }
    }
}

@Composable
fun MealPlannerApp() {
    val navController = rememberNavController()

    // Call the navigation graph in the composable function
    AppNavGraph(navController)
}
