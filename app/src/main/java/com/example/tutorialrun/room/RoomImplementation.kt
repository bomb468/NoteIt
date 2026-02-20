package com.example.tutorialrun.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    var title : String,
    var content : String,
    val timestamp : Long = System.currentTimeMillis()
)
@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
}
@Dao
interface NoteDao{
    @Query("SELECT * FROM Note ORDER BY timestamp DESC")
    fun getAllNotes() : Flow<List<Note>>
    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getNoteById(id : Int) : Note?
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note : Note)
    @Query("DELETE FROM Note WHERE id IN (:ids)")
    suspend fun deleteNotesById(ids: List<Int>)
}

class MockNotesDao(
    initialNotes: List<Note> = listOf(
        Note(
            id = 1,
            title = "Project Phoenix Ideas",
            content = "Consider using a graph database for the social module. Also, need to verify if the API rate limits allow for 500 requests per minute. Meeting on Friday at 10 AM."
        ),
        Note(
            id = 2,
            title = "Travel Checklist",
            content = "Pack the universal adapter and the noise-canceling headphones. Don't forget to download the offline maps for Kyoto. Check-in opens 24 hours before the flight."
        ),
        Note(
            id = 3,
            title = "Workout Routine",
            content = "Monday: Upper Body (Focus on Pull-ups and Bench Press). Wednesday: Lower Body (Squats 3x10). Friday: Cardio and Core. Keep the rest intervals under 60 seconds."
        ),
        Note(
            id = 4,
            title = "Book Quotes",
            content = "‘All we have to decide is what to do with the time that is given us.’ – Gandalf. Also, find that passage about the lighthouse in the second chapter."
        ),
        Note(
            id = 5,
            title = "Recipe: Spicy Ramen",
            content = "Soft-boiled egg (6 mins), toasted sesame oil, minced garlic, and red pepper flakes. Use the high-quality miso paste from the international market."
        ),
        Note(
            id = 6,
            title = "Midnight Thoughts",
            content = "If a clock falls over, is it still right twice a day or just broken? Also, I need to buy more lightbulbs for the hallway."
        ))
) : NoteDao {
    private val _notes = MutableStateFlow(initialNotes)

    override fun getAllNotes(): Flow<List<Note>> = _notes.map { list ->
        list.sortedByDescending { it.timestamp }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return _notes.value.find { it.id == id }
    }

    override suspend fun upsertNote(note: Note) {
        _notes.update { currentList ->
            if (note.id == 0) {
                val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
                currentList + note.copy(id = newId)
            } else {
                currentList.map { if (it.id == note.id) note else it }
            }
        }
    }

    override suspend fun deleteNotesById(ids : List<Int>) {
        _notes.update { currentList ->
            currentList.filter { !ids.contains(it.id)
            }
        }
    }
}