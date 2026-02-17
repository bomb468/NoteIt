package com.example.tutorialrun

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TestClass {
    companion object{
        fun testMethod() {

        }
        /*
        customDataScopeStart {
            Log.d("TRACKER","Current level: $currentLevel")
            customDataScope(CustomData(5)) {
                Log.d("TRACKER","Current level: $currentLevel")
                customDataScope(CustomData(10)) {
                    Log.d("TRACKER","Current level: $currentLevel")
                }
                customDataScope {
                    Log.d("TRACKER","Current level: $currentLevel")
                }
            }
        }*/
    }
}

class CustomData(val currentLevel : Int = 0, val finalLevel : Int = 10)

fun customDataScopeStart(customData: CustomData = CustomData(), block : CustomData.()->Unit){
    customData.block()
}

fun CustomData.customDataScope(customData: CustomData = this, block : CustomData.()->Unit){
    customData.block()
}