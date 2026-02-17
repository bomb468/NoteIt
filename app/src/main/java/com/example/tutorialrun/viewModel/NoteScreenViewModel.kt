package com.example.tutorialrun.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorialrun.hilt.MockDao
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.room.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    @param:MockDao private val noteDao: NoteDao
) : ViewModel() {

    var title by mutableStateOf("")
    var content by mutableStateOf("")

    fun saveNote() {
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                pinned = false
            )
            noteDao.insertNote(newNote)
        }
    }
}