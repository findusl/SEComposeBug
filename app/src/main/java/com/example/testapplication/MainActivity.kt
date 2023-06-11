package com.example.testapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var id by remember { mutableStateOf(0) }
    val contentModel = ContentModel(id)

    ContentScreen(contentModel, nextModel = {
        id = Random.nextInt(1000)
    })
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContentScreen(contentModel: ContentModel, nextModel: () -> Unit) {
    Scaffold(topBar = { TopBar(contentModel) }) {
        Column {
            Text("This number should match ${contentModel.id}")
            Button(onClick = nextModel) { Text("Generate new number") }
        }
    }

    // accessing flow is necessary for the bug to appear
    contentModel.contentFlow.collectAsState(0).value.toString()
}

@Composable
private fun TopBar(contentModel: ContentModel) = Text("This number should match ${contentModel.id}")

data class ContentModel(val id: Int)  {
    /** This flow represents data fetched in the background based on the value of id. */
    val contentFlow: Flow<Int>
        get() = flow {
            this.emit(id)
        }
}
