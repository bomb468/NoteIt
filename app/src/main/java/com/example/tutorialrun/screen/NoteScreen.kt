package com.example.tutorialrun.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorialrun.room.Note
import com.example.tutorialrun.viewModel.NoteScreenViewModel

@Composable
fun NoteScreen(
    noteId: Int,
    onBack: () -> Unit
) {
    val noteScreenViewModel : NoteScreenViewModel = hiltViewModel()
    LaunchedEffect(true) {
        noteScreenViewModel.getNote(noteId)
    }
    BackHandler(enabled = true) {
        noteScreenViewModel.saveNote()
        onBack()
    }
    /*val noteScreenViewModel = Note(
        0,
        "Project Phoenix Ideas a v Project Phoenix Ideas",
        "Consider using a graph database for the social module. Also, need to verify if the API rate limits allow for 500 requests per minute. Meeting on Friday at 10 AM."
    )*/
    val (getEditMode,setEditMode) = remember {
        if (noteId==0) mutableStateOf(true)
        else mutableStateOf(false)
    }
    //val getEditMode = true
    //val setEditMode : (Boolean)->Unit = {}
    Column(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start){
        NoteScreenTopBar(getEditMode,setEditMode) {
            noteScreenViewModel.saveNote()
            onBack()
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            Column(modifier = Modifier.fillMaxWidth()
                .border(2.dp,Color.Black,RoundedCornerShape(10.dp))
                .padding(vertical = 15.dp, horizontal = 20.dp)) {
                BasicTextField(
                    readOnly = !getEditMode,
                    value = noteScreenViewModel.title,
                    onValueChange = {
                        if (it.length<=100) noteScreenViewModel.title = it },
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    ),
                    modifier = Modifier,
                    decorationBox = { innerTextField ->
                        Box {
                            if (noteScreenViewModel.title.isEmpty()) {
                                Text(
                                    text = "Sample Title",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        color = Color.Gray.copy(alpha = 0.5f) // Faint color for hint
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                if (getEditMode){
                    Row(modifier= Modifier.fillMaxWidth().padding(top=5.dp), horizontalArrangement = Arrangement.End){
                        Text("${noteScreenViewModel.title.length}/100")
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
            Column(modifier = Modifier.fillMaxWidth()
                .border(2.dp,Color.Black,RoundedCornerShape(10.dp))
                .padding(vertical = 15.dp, horizontal = 20.dp)) {
                BasicTextField(
                    readOnly = !getEditMode,
                    value = noteScreenViewModel.content,
                    onValueChange = {
                        if (it.length <= 500) {
                        noteScreenViewModel.content = it
                    } },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    ),
                    modifier = Modifier,
                    decorationBox = { innerTextField ->
                        Box {
                            if (noteScreenViewModel.content.isEmpty()) {
                                Text(
                                    text = "Sample Content",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        color = Color.Gray.copy(alpha = 0.5f) // Faint color for hint
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                if (getEditMode){
                    Row(modifier= Modifier.fillMaxWidth().padding(top=5.dp), horizontalArrangement = Arrangement.End){
                        Text("${noteScreenViewModel.content.length}/500")
                    }
                }
            }

        }
    }
}

@Composable
fun NoteScreenTopBar(editMode : Boolean, setMode : (Boolean)->Unit, onBack : ()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterStart)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier.size(25.dp)
            )
        }
        Button(
            onClick = {
                setMode(!editMode)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.CenterEnd).width(75.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp,Color.Black)
        ) {
            Text(if (editMode) "Done" else "Edit")
        }
    }
}
@Preview
@Composable
private fun TestScreen(){
    NoteScreen(0) { }
}