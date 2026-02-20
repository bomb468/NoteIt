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
    @MockDao private val noteDao: NoteDao
) : ViewModel() {

    var id : Int = 0
    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var editMode by mutableStateOf(false)
    fun getNote(noteId : Int){
        if (noteId==0){
            editMode=true
            return
        }
        viewModelScope.launch {
            val note = noteDao.getNoteById(noteId)
            if (note!=null){
                id=note.id
                title = note.title
                content = note.content
            }
        }
    }
    fun saveNote(){
        if (title.isEmpty()) return
        viewModelScope.launch {
            noteDao.upsertNote(Note(id,title,content))
        }
    }

    override fun onCleared() {
        super.onCleared()
        //Log.d("TRACKER","Testing on Clear")
    }
}