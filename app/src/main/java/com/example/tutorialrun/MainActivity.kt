package com.example.tutorialrun

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tutorialrun.ui.theme.TutorialRunTheme
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tutorialrun.composeScreens.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.collections.listOf

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    var title : String,
    var content : String
)

@HiltAndroidApp
class MainApplication : Application()

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

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

@Parcelize
class ScreenInfo(val name : String,
                 val noteId : Int = 0) : Parcelable

sealed class Screen{
    object NotesListScreen : Screen()
    class NoteScreen(val key : Int) : Screen()
}
@HiltViewModel
class ScreenStackViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    val backStackSaver = Saver<MutableList<Screen>, ArrayList<ScreenInfo>>(
        save = {
            backStack ->
            val list = arrayListOf<ScreenInfo>()
            for (screen in backStack){
                when(screen) {
                    is Screen.NotesListScreen -> {
                        list.add(ScreenInfo(name="NotesListScreen"))
                    }
                    is Screen.NoteScreen -> {
                        list.add(ScreenInfo(name="NotesScreen", noteId = screen.key))
                    }
                }
            }
            list
        },
        restore = { list ->
            val backStack = mutableStateListOf<Screen>()
            for (screenInfo in list){
                when(screenInfo.name){
                    "NotesListScreen" -> {
                        backStack.add(Screen.NotesListScreen)
                    }
                    else -> {
                        backStack.add(Screen.NoteScreen(screenInfo.noteId))
                    }
                }
            }
            backStack
        }
    )
    val backStack by savedStateHandle.saveable(saver = backStackSaver){
        mutableStateListOf<Screen>(Screen.NotesListScreen)
    }
    fun addScreen(screen: Screen) {
        backStack.add(screen)
    }
}

@Composable
fun TestScreen(id : Int){
    Text("This is Main Screen for id $id")
}





@Composable
fun MainNavigation() {
    val screenStackViewModel : ScreenStackViewModel =
        hiltViewModel<ScreenStackViewModel>(
            viewModelStoreOwner = LocalViewModelStoreOwner.current as ViewModelStoreOwner
        )
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()){
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
                        NoteListScreen()
                    }
                    is Screen.NoteScreen -> NavEntry(key) {
                        TestScreen(key.key)
                    }
                }
            }
        )
    }
}

class NoteListViewModel : ViewModel(){
    val mainNoteHolder = mutableStateListOf<Note>(
        Note(1,"Motivational Video Links","https://www.youtube.com/@joerogan\nhttps://www.youtube.com/c/TheGogginsCrew"),
        Note(2,"Grocery List","Carrots\nLube\nVicks")
    )
}
@Composable
fun NoteListScreen() {
    val noteListViewModel : NoteListViewModel = viewModel<NoteListViewModel>()
    LazyColumn(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)){
        items(
            items = noteListViewModel.mainNoteHolder,
            key = { note -> note.id }
        ) { note ->
            NoteItem(note)
        }
    }
}

@Composable
fun NoteItem(note : Note) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 5.dp)
        .border(2.dp,Color.Black, CutCornerShape(5.dp))
        .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    )
    {
        Text(
            text = note.title,
            style = TextStyle(fontWeight = FontWeight.W600, fontSize = 20.sp),
            maxLines = 1, // Usually titles look better as a single line
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = note.content,
            maxLines = 2, // This forces the "two lines then cut off" behavior
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun TestScreen(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top){
        NoteListScreen()
    }
}