package com.example.tutorialrun.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.plus

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    var title : String,
    var content : String,
    val timestamp : Long = System.currentTimeMillis(),
    val pinned : Boolean
)
@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
}
@Dao
interface NoteDao{
    @Query("SELECT * FROM Note ORDER BY timestamp DESC")
    fun getAllNotes() : Flow<List<Note>>
    @Insert
    suspend fun insertNote(note : Note)
    @Delete
    suspend fun deleteNode(note : Note)
}

class MockNotesDao(initialNotes: List<Note> = emptyList()) : NoteDao {
    private val _notes = MutableStateFlow(initialNotes)

    override fun getAllNotes(): Flow<List<Note>> = _notes

    override suspend fun insertNote(note: Note) {
        _notes.value += note
    }

    override suspend fun deleteNode(note: Note) {
        _notes.value -= note
    }
}