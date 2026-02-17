package com.example.tutorialrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tutorialrun.ui.theme.TutorialRunTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.viewModel.NoteScreenViewModel
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
fun NoteScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: NoteScreenViewModel = hiltViewModel()
) {
    Text("Hello Son")
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
                        NoteListScreen {
                            key ->
                                screenStackViewModel.addScreen(Screen.NoteScreen(key))
                        }
                    }
                    is Screen.NoteScreen -> NavEntry(key) {
                        NoteScreen(key.key, onBack = {
                            screenStackViewModel.backStack.removeLastOrNull()
                        })
                    }
                }
            }
        )
    }
}

@Composable
fun NoteListScreen(onClickOpenEditNote : (Int)->Unit) {
    //val noteListViewModel : NoteListViewModel = hiltViewModel<NoteListViewModel>()
    //val notesList by noteListViewModel.mainNoteHolder.collectAsStateWithLifecycle(initialValue = emptyList())
    val notesList = mutableListOf(
        Note(
            1, "Motivational",
            "The old oak tree by the ridge has survived three lightning strikes since 1994. Remember to check if the air pressure in the spare tire is still at 32 PSI.",
            pinned = false
        ),
        Note(
            2, "Grocery List",
            "\"The only way out is through,\" whispered the protagonist as the fog rolled in. Ingredients for the marinade: soy sauce, honey, grated ginger, and a dash of lime.",
            pinned = true
        )
    )
    Column(modifier= Modifier.fillMaxSize()) {
        NoteListScreenTopBar()
        LazyColumn(modifier = Modifier){
            items(
                items = notesList,
                key = { note -> note.id }
            ) { note ->
                NoteItem(note)
            }
        }
    }
}


@Composable
fun NoteListScreenTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(20.dp),
        contentAlignment = Alignment.Center // Centers everything inside the Box by default
    ) {
        // This will be perfectly centered to the screen
        Text(
            modifier = Modifier.align(Alignment.CenterStart) ,
            text = "Notes List Screen",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        // This will be pushed to the far end
        Button(
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.CenterEnd).width(75.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp,Color.Black)
        ) {
            Text("Select")
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Column(modifier = Modifier
        .padding(start = 20.dp,end = 20.dp, bottom = 20.dp)
        .border(2.dp,Color.Black,RoundedCornerShape(10.dp))
        .padding(20.dp))
    {
        Text(
            text = note.title,
            style = TextStyle(fontWeight = FontWeight.W600, fontSize = 25.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = note.content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun TestScreen(){
    NoteListScreen{}
}