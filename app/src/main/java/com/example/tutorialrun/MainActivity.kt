package com.example.tutorialrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tutorialrun.composeScreens.MainScreen
import com.example.tutorialrun.ui.theme.TutorialRunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutorialRunTheme {
                MainScreen()
                //MainNavigation()
                /*
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TutorialRunTheme {
        Greeting("Android")
    }
}

data object HomeScreen
data class MainScreen(val blue : Boolean)
@Composable
fun MainNavigation() {

    val backStack = remember { mutableStateListOf<Any>(HomeScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is HomeScreen -> NavEntry(key) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = {
                            backStack.add(MainScreen(true))
                        }) {
                            Text("Click to navigate to Blue Screen")
                        }
                        Button(onClick = {
                            backStack.add(MainScreen(false))
                        }) {
                            Text("Click to navigate to Red Screen")
                        }
                    }
                }

                is MainScreen -> NavEntry(key) {
                    if (key.blue){
                        Column(modifier = Modifier.fillMaxSize().background(Color.Blue),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Text("Just press the Back Screen Lil Bro")
                        }
                    }else{
                        Column(modifier = Modifier.fillMaxSize().background(Color.Red),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Text("Just press the Back Screen Lil Bro")
                        }
                    }
                }
                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}