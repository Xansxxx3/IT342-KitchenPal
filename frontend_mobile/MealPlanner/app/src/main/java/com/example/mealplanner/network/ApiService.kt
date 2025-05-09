package com.example.mealplanner.network

import com.google.gson.annotations.SerializedName
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
    @SerializedName("recipeId")
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>, // Important: now a list
    val prepTime: Int,
    val nutritionInfo: String,
    val cuisineType: String,
    val mealType: String,
    val ratingsAverage: Double,
    val imagePath: String
)
data class MealPlanRequest(
    val userId: String,
    val recipe: Recipe
)
data class MealPlanAdd(
    val userId: String,
    val recipeId: Int
)
data class ShoppingListItem(
    val shoppingListItemId: Long? = null,
    val user: UserEntity,
    val recipes: Set<Recipe>,
    val quantity: Int,
    val status: String
)
data class UserEntity(
    val id: String,
    val name: String // Add other fields as needed
)

interface ApiService {
    @GET("api/v1/user/print")
    suspend fun getGreeting(): Response<GreetingResponse>

    @POST("api/v1/auth/authenticate")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/recipe/allrecipe")
    suspend fun getAllRecipes(
        @Header("Authorization") token: String
    ): List<Recipe>


    @GET("api/recipe/{id}")
    suspend fun getRecipesByIds(
        @Header("Authorization") token: String
    )

    @GET("/api/shopping-list-items/allShoppingList")
    suspend fun getAllShoppingList(
        @Header("Authorization") token: String
    ): List<ShoppingListItem>

    @POST("api/meal-plans/add")
    suspend fun addMealPlan(
        @Header("Authorization") token: String,
        @Body request: MealPlanAdd
    )

    @GET("api/meal-plans/all") // Adjust path if needed
    suspend fun getAllMealPlans(
        @Header("Authorization") token: String
    ): List<MealPlanRequest>



}
