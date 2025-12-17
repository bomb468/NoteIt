package com.example.tutorialrun.composeScreens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

// 1. Define our Tab items
sealed class TabItem(val title: String, val icon: ImageVector) {
    object AllEntries : TabItem("All", Icons.Default.FavoriteBorder)
    object RecentEntry : TabItem("Recent", Icons.Default.AccountBox)
    object CreateEntry : TabItem("Create", Icons.Default.Create)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val tabs = listOf(TabItem.AllEntries, TabItem.RecentEntry, TabItem.CreateEntry)

    // 2. Initialize Pager State
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        // 3. Sync: Tab is "selected" if the pager is on this index
                        selected = pagerState.currentPage == index,
                        onClick = {
                            // 4. Scroll to page when tab is clicked
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 5. The Swipeable Area
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { pageIndex ->
            // Display the appropriate screen based on current page index
            when (pageIndex) {
                0 -> ScreenContent(title = "All Entries Screen")
                1 -> ScreenContent(title = "Recent Entry Screen")
                2 -> ScreenContent(title = "Create New Entry")
            }
        }
    }
}

@Composable
fun ScreenContent(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}