package com.example.tutorialrun.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.viewModel.NoteListViewModel

@Composable
fun NoteListScreen(noteListViewModel: NoteListViewModel = hiltViewModel(),
                   onClickOpenEditNote : (Int)->Unit) {

    Column(modifier= Modifier.fillMaxSize()) {
        NoteListScreenTopBar(noteListViewModel.selectMode) { noteListViewModel.toggleSelectMode() }
        SearchBarForNotes(
            noteListViewModel.searchText,
            {noteListViewModel.onSearchTextChange(it)},
            {noteListViewModel.onToggleCaseSensitive()},
            noteListViewModel.isCaseSensitive
        )
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()){
            items(
                items = noteListViewModel.filteredNotes,
                key = { note -> note.id }
            ) { note ->
                NoteItem(note,
                    noteListViewModel.selectMode,
                    onClickOpenEditNote,
                    if (noteListViewModel.selectMode){
                        noteListViewModel.selectedMap[note.id] ?: false
                    }else{
                        false
                    }
                ) { noteListViewModel.toggleSelectForNote(noteId = note.id) }
            }
        }
        NoteListScreenBottomBar(onClickOpenEditNote,
            noteListViewModel.allNotesSize,
            noteListViewModel.selectMode,
            {noteListViewModel.runDeleteNodeQuery()})
    }
}

@Composable
fun NoteItem(note: Note,
             selectMode : Boolean,
             onClickOpenEditNote : (Int) -> Unit,
             isSelected : Boolean,
             toggleSelectForNote : (Int)->Unit) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(start = 20.dp,end = 20.dp, bottom = 20.dp)
        .border(2.dp,Color.Black,RoundedCornerShape(10.dp))
        .clickable{
            if (selectMode) toggleSelectForNote(note.id)
            else onClickOpenEditNote(note.id)
        }
        .padding(start=20.dp,end=20.dp,top=20.dp,bottom=20.dp))
    {
        Row(verticalAlignment = Alignment.CenterVertically){
            if (selectMode){
                RadioButton(
                    selected = isSelected,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black.copy(alpha = 0.6f),
                        disabledSelectedColor = Color.Gray
                    ),
                    modifier = Modifier.padding(start=0.dp,end = 10.dp)
                )
            }
            Column{
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

    }
}

@Composable
fun NoteListScreenBottomBar(onClickOpenEditNote: (Int) -> Unit, notesListSize : Int, selectMode: Boolean,runDeleteNodeQuery : ()->Unit) {
    val localContext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxWidth().height(75.dp),
        contentAlignment = Alignment.Center
    ){
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black) // Match your screen background
                .align(Alignment.TopCenter)
        )
        Button(
            onClick = {
                if (selectMode){
                    runDeleteNodeQuery()
                }else{
                    if (notesListSize==10){
                        Toast.makeText(localContext, "You have reached the maximum number of notes", Toast.LENGTH_SHORT).show()
                    }else{
                        onClickOpenEditNote(0)
                    }
                }
              },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp,Color.Black)
        ) {
            Text("${if (selectMode) "Delete" else "Create"} Note")
        }
    }
}

@Composable
fun SearchBarForNotes(searchQuery : String,
                      setSearchQuery : (String)->Unit,
                      toggleCaseSensitiveButton : ()->Unit,
                      isCaseSensitive : Boolean) {
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    var wasVisible by remember { mutableStateOf(false) }
    LaunchedEffect(imeVisible) {
        if (!imeVisible && wasVisible) {
            focusManager.clearFocus()
        }
        wasVisible=imeVisible
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .border(2.dp,Color.Black,RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
            BasicTextField(
                value = searchQuery,
                onValueChange = { setSearchQuery(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color.Black.copy(alpha = 0.5f),
                                fontWeight = FontWeight.W500
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Text(modifier = Modifier.align(Alignment.CenterEnd).clickable{
            toggleCaseSensitiveButton()
        },text="Aa", fontWeight = if (!isCaseSensitive) FontWeight.W300 else FontWeight.Bold)
    }
}


@Composable
fun NoteListScreenTopBar(selectMode: Boolean, toggleSelectMode: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart) ,
            text = "Notes List Screen",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = { toggleSelectMode() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.CenterEnd).width(75.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp,Color.Black)
        ) {
            Text(if (selectMode) "Cancel" else "Select")
        }
    }
}

@Preview
@Composable
private fun TestScreen(){
    /*
    NoteListScreen(notesListFlow = flow{
        emit(listOf(
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
            )
        )) }
    ){}
    NoteItem(
        Note(
            id = 4,
            title = "Book Quotes",
            content = "‘All we have to decide is what to do with the time that is given us.’ – Gandalf. Also, find that passage about the lighthouse in the second chapter."
        ),
        true,
    ) { }*/
}