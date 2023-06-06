import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import cn.edu.zjut.withincircle.utils.LocalNavController

@Composable
fun ThirdScreen() {
    val navController= LocalNavController.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Third Screen",
            color = Color.Red,
            style = TextStyle(textAlign = TextAlign.Center),
        )
        Button(onClick = {
            navController.navigate("map")
        }) {
            Text(text = "我的坐标")
        }
    }
}