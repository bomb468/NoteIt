package com.example.tutorialrun.viewModel

import androidx.lifecycle.ViewModel
import com.example.tutorialrun.hilt.MockDao
import com.example.tutorialrun.room.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor( @MockDao noteDao : NoteDao) : ViewModel(){
    //val noteDao : NoteDao
    val mainNoteHolder = noteDao.getAllNotes()
}