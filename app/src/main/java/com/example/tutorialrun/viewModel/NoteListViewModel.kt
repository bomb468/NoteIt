package com.example.tutorialrun.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorialrun.hilt.MockDao
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.room.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class NoteListViewModel @Inject constructor(@MockDao private val noteDao: NoteDao) : ViewModel() {

    private var allNotes: List<Note> = emptyList()
    val allNotesSize : Int
        get() = allNotes.size

    var selectMode by mutableStateOf(false)
        private set
    fun toggleSelectMode() {
        selectMode = !selectMode
        if (!selectMode) {
            selectedMap.clear()
        } else {
            allNotes.forEach { selectedMap[it.id] = false }
        }
    }

    var filteredNotes: List<Note> by mutableStateOf(emptyList())
        private set

    var searchText by mutableStateOf("")
        private set

    var isCaseSensitive by mutableStateOf(false)
        private set


    val selectedMap = mutableStateMapOf<Int, Boolean>()
    init {
        viewModelScope.launch {
            noteDao.getAllNotes().collect { notes ->
                allNotes = notes
                if (selectMode) {
                    notes.forEach { note ->
                        if (selectedMap[note.id] == null) selectedMap[note.id] = false
                    }
                }
                applyFilter()
            }
        }
    }

    fun onSearchTextChange(newText: String) {
        searchText = newText
        applyFilter()
    }

    fun onToggleCaseSensitive() {
        isCaseSensitive = !isCaseSensitive
        applyFilter()
    }

    private fun applyFilter() {
        filteredNotes = if (searchText.isBlank()) {
            allNotes
        } else {
            val query = searchText.trim()
            allNotes.filter { note ->
                note.title.contains(query, ignoreCase = !isCaseSensitive) ||
                        note.content.contains(query, ignoreCase = !isCaseSensitive)
            }
        }
    }

    fun toggleSelectForNote(noteId: Int) {
        val current = selectedMap[noteId] ?: false
        selectedMap[noteId] = !current
    }

    fun runDeleteNodeQuery(){
        viewModelScope.launch {
            val idsToDelete = selectedMap.filter { it.value }.keys.toList()
            withContext(Dispatchers.IO) {
                idsToDelete.forEach { id ->
                    noteDao.deleteNodeById(id)
                }
            }
            toggleSelectMode()
        }
    }
}

