import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import cn.edu.zjut.withincircle.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppScaffold() {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                selectedScreen = selectedScreen,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            )
        }
    ) {
        HorizontalPager(
            count = BottomScreen.values().size,
            state = pagerState,
            userScrollEnabled = false,
            contentPadding = it
        ) { page ->
            when(BottomScreen.values()[page]) {
                BottomScreen.Index -> FirstScreen()
                BottomScreen.Message -> SecondScreen()
                BottomScreen.My -> ThirdScreen()
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedScreen = page
        }
    }

}

enum class BottomScreen(
    @StringRes val label: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    Index(R.string.index, Icons.Outlined.Home, Icons.Filled.Home),
    Message(R.string.message, Icons.Outlined.Group, Icons.Filled.Group),
    My(R.string.my, Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle)
}