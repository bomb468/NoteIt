package com.example.tutorialrun.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.viewModel.NoteListViewModel

@Composable
fun NoteListScreen(noteListViewModel: NoteListViewModel = hiltViewModel(),
                   onClickOpenEditNote : (Int)->Unit) {
    val context = LocalContext.current
    Column(modifier= Modifier.fillMaxSize()) {
        if (noteListViewModel.showAlertDialogBox){
            AlertDialog(
                onDismissRequest = {
                    noteListViewModel.toggleAlertBox()
                },
                title = { Text(text = "Confirm Action") },
                text = {
                    val scrollState = rememberScrollState()
                    Text("Are you sure you want to delete all the notes titled,${noteListViewModel.stringOfNoteTitlesToBeDeleted}?",
                        modifier = Modifier.verticalScroll(scrollState))
                },
                confirmButton = {
                    Button(onClick = {
                        noteListViewModel.toggleAlertBox()
                        noteListViewModel.runDeleteNodeQuery(context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.widthIn(min = 75.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp,Color.Black)
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { noteListViewModel.toggleAlertBox() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.widthIn(min = 75.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp,Color.Black)
                    )   {
                        Text("Cancel")
                    }
                }
            )
        }
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
        ) {
            noteListViewModel.toggleAlertBox()
        }
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
            AnimatedVisibility(selectMode) {
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
fun NoteListScreenBottomBar(onClickOpenEditNote: (Int) -> Unit, notesListSize : Int, selectMode: Boolean,toggleAlertBox : ()->Unit) {
    val localContext = LocalContext.current.applicationContext
    Box(
        modifier = Modifier.fillMaxWidth().heightIn(min = 75.dp).padding(vertical = 10.dp),
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
                    toggleAlertBox()
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Notes List Screen",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = { toggleSelectMode() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.widthIn(min = 75.dp),
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
    var showAlertDialogBox by mutableStateOf(false)
    AlertDialog(
        modifier = Modifier.border(2.dp,Color.Black,RoundedCornerShape(0.dp)),
        onDismissRequest = {
            showAlertDialogBox = false
        },
        title = { Text(text = "Confirm Action") },
        text = { Text("This is the dialog content. You must interact with me!") },
        confirmButton = {
            Button(onClick = { showAlertDialogBox = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.widthIn(min = 75.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp,Color.Black)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = { showAlertDialogBox = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.widthIn(min = 75.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp,Color.Black)
            )   {
                    Text("Cancel")
                }
        }
    )
}