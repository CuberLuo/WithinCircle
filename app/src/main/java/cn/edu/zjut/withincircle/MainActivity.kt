package cn.edu.zjut.withincircle

import AppScaffold
import Login
import MapWidget
import Register
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.edu.zjut.withincircle.ui.theme.WithinCircleTheme
import cn.edu.zjut.withincircle.utils.LocalNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WithinCircleTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CompositionLocalProvider(
                        LocalNavController provides navController
                    ){
                        ComposeNavigation(navController)
                    }
                }
            }
        }

    }
}



@Composable
fun ComposeNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("login") {
            Login()
        }
        composable("register") {
            Register()
        }
        composable("main") {
            AppScaffold()
        }
        composable("map"){
            MapWidget()
        }
    }
}