import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mealplanner.network.RetrofitClient

@Composable
fun GreetingScreen() {
    var greeting by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getGreeting()
            if (response.isSuccessful) {
                // Assuming the response body is like: { "message": "Hello from Spring Boot!" }
                greeting = response.body()?.let { it.message } ?: "No message"
            } else {
                greeting = "Error: ${response.code()}"
            }
        } catch (e: Exception) {
            greeting = "Failed: ${e.localizedMessage}"
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
