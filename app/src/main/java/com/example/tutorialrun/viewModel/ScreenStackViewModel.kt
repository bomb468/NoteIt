package com.example.tutorialrun.viewModel

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

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