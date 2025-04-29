package com.example.mealplanner.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class GreetingResponse(
    val message: String
)

data class RegisterRequest(
    val email: String,
    val fname: String,
    val lname: String,
    val password: String
)

data class RegisterResponse(
    val token: String,
    val role: String,
    val userId: String
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val role: String,
    val userId: String
)

data class Recipe(
    val id: Long,
    val title: String,
    val description: String,
    val ingredients: String,
    val prepTime: Int,
    val nutritionInfo: String,
    val cuisineType: String,
    val mealType: String,
    val ratingsAverage: Double,
    val price: Int,
    val imagePath: String // this is the uploaded Cloudinary URL
)

interface ApiService {
    @GET("api/v1/user/print")
    suspend fun getGreeting(): Response<GreetingResponse>

    @POST("api/v1/auth/authenticate")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    interface RecipeApi {
        @GET("/api/recipe/allrecipe")
        suspend fun getAllRecipes(
            @Header("Authorization") token: String
        ): List<Recipe>
    }
}