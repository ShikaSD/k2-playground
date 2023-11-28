import android.app.Activity
import androidx.compose.runtime.Composable
import test.Test

class MainActivity : Activity() {
    @Composable fun Content() {
        Test {
            println("Test")
        }
    }
}