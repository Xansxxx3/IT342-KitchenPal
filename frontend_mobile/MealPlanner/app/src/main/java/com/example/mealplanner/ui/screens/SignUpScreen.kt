package com.example.mealplanner.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealplanner.R
import com.example.mealplanner.network.LoginRequest
import com.example.mealplanner.network.RegisterRequest
import com.example.mealplanner.network.RegisterResponse
import com.example.mealplanner.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpScreen(navController: NavController, onSignUp: (String, String, String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    val inputBoxColor = Color(0xFFcff9d3)
    val signUpColor = Color(0xFF7dd08e)

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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBoxColor,
                    focusedContainerColor = inputBoxColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val RegisterRequest = RegisterRequest(email,firstName,lastName, password)
                            val response = RetrofitClient.apiService.register(RegisterRequest)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val RegisterResponse = response.body()
                                    println("Register Success: ${RegisterResponse?.token}")
                                    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("jwt_token", RegisterResponse?.token).apply()
                                    sharedPref.edit().putString("user_role", RegisterResponse?.role).apply()
                                    sharedPref.edit().putString("user_id", RegisterResponse?.userId).apply()
                                    onSignUp(email,firstName,lastName, password)
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
                colors = ButtonDefaults.buttonColors(containerColor = signUpColor)
            ) {
                Text("Sign Up")
            }
        }
    }
}
