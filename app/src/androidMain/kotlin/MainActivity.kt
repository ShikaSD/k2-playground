import android.app.Activity
import androidx.compose.runtime.Composable
import test.Test

class MainActivity : Activity() {
    var test: String? = null
    @Composable fun Content() {
        Test(
            test?.let {
                { }
            }
        )
    }
}