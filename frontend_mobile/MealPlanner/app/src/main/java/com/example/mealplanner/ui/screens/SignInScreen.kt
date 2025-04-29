package com.example.mealplanner.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mealplanner.R
import com.example.mealplanner.isOAuthLogin
import com.example.mealplanner.network.LoginRequest
import com.example.mealplanner.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SignInScreen(navController: NavController, onLogin: (String, String) -> Unit = { _, _ -> }) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val token = sharedPref.getString("jwt_token", null)


    LaunchedEffect(Unit) {
        if (!token.isNullOrEmpty() || isOAuthLogin) {  // 🡸 Check your global flag
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }


    val inputBoxColor = Color(0xFFcff9d3)
    val signInColor = Color(0xFF7dd08e)

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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "itchenPal",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Username Field (rounded)
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sign In Button
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val loginRequest = LoginRequest(username, password)
                            val response = RetrofitClient.apiService.login(loginRequest)
                            val loginResponse = response.body()
                            println("Login Success: ${loginResponse?.userId}")

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()
                                    println("Login Success: ${loginResponse?.token}")
                                    val sharedPref = context.getSharedPreferences(
                                        "auth_prefs",
                                        Context.MODE_PRIVATE
                                    )
                                    sharedPref.edit().putString("jwt_token", loginResponse?.token)
                                        .apply()

                                    sharedPref.edit().putString("user_role", loginResponse?.role)
                                        .apply()
                                    sharedPref.edit().putString("user_id", loginResponse?.userId)
                                        .apply()
                                    onLogin(username, password)
                                } else {
                                    println("Login Failed: ${response.code()}")
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                println("Login Error: ${e.localizedMessage}")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7DD08E))
            ) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Sign up text with clickable "Sign up"
            Text(
                buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Sign up")
                    }
                },
                modifier = Modifier
                    .clickable {
                        navController.navigate("signup")
                    }
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Divider with "or"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    color = Color.DarkGray,
                    thickness = 2.dp
                )
                Text(
                    text = "OR",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    color = Color.DarkGray,
                    thickness = 2.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign-In Button
            Button(
                onClick = {
                    val googleAuthUrl =
                        "https://it342-kitchenpal.onrender.com/oauth2/authorization/google?redirect_uri=myapp://oauth2redirect"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleAuthUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Logo",
                    modifier = Modifier
                        .height(24.dp)
                        .padding(end = 8.dp)
                )
                Text("Sign in with Google", color = Color.Black)
            }
        }
    }
}
