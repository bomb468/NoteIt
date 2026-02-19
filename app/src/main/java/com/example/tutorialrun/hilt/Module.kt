package com.example.tutorialrun.hilt

import android.content.Context
import androidx.room.Room
import com.example.tutorialrun.room.MockNotesDao
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.room.NoteDao
import com.example.tutorialrun.room.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@EntryPoint
@InstallIn(SingletonComponent::class)
interface GetSingletonComponents{
    @MockDao
    fun getMockNotesDao() : NoteDao
}
@Qualifier
annotation class MockDao

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule{
    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context) : NotesDatabase {
        return Room.databaseBuilder(context, NotesDatabase::class.java, "notes.db").build()
    }
    @Provides
    @MockDao
    @Singleton
    fun provideMockNotesDao(): NoteDao {
        return MockNotesDao()
    }
    @Provides
    fun provideNotesDao(notesDatabase: NotesDatabase) : NoteDao {
        return notesDatabase.noteDao()
    }
}