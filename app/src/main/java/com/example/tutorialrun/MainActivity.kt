package com.example.tutorialrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tutorialrun.ui.theme.TutorialRunTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.example.tutorialrun.screen.NoteListScreen
import com.example.tutorialrun.screen.NoteScreen
import com.example.tutorialrun.viewModel.Screen
import com.example.tutorialrun.viewModel.ScreenStackViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.listOf


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutorialRunTheme {
                MainNavigation()
            }
        }
    }
}



@Composable
fun MainNavigation() {
    val screenStackViewModel : ScreenStackViewModel =
        hiltViewModel<ScreenStackViewModel>(
            viewModelStoreOwner = LocalViewModelStoreOwner.current as ViewModelStoreOwner
        )
    Box(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)
    ){
        NavDisplay(
            entryDecorators = listOf(
                // Add the default decorators for managing scenes and saving state
                rememberSaveableStateHolderNavEntryDecorator(),
                // Then add the view model store decorator
                rememberViewModelStoreNavEntryDecorator()
            ),
            backStack = screenStackViewModel.backStack,
            onBack = {
                screenStackViewModel.backStack.removeLastOrNull()
            },
            entryProvider = { key ->
                when (key) {
                    is Screen.NotesListScreen -> NavEntry(key) {
                        NoteListScreen {
                            key ->
                                screenStackViewModel.addScreen(Screen.NoteScreen(key))
                        }
                    }
                    is Screen.NoteScreen -> NavEntry(key) {
                        NoteScreen(key.noteId, onBack = {
                            screenStackViewModel.backStack.removeLastOrNull()
                        })
                    }
                }
            }
        )
    }
}