package com.shoaib.notes_app_kmp


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoaib.notes_app_kmp.notes.ListNotesScreen
import com.shoaib.notes_app_kmp.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.ui.theme.nunitoFontFamily
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.rafiki
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    NotesAppTheme {

        val viewModel = viewModel { HomeViewModel() }
        val notes by viewModel.notes.collectAsState()
        
        Scaffold(
            floatingActionButton = {
               FloatingActionButton(onClick = {}, shape = CircleShape){
                   Text("+",
                       fontSize = 18.sp
                   )
               }
            }
        ) {

            Column(
                modifier = Modifier.padding(it)
            ){
                Text(
                    text = "Notes",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 43.sp
                    )
                )
                if(notes.isNotEmpty())
                    ListNotesScreen(notes)
                else
                     EmptyView()
            }
        }
    }
}

@Composable
fun EmptyView(){
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.align (Alignment.Center)) {
            Image(
                painterResource(Res.drawable.rafiki),
                contentDescription = null,
                modifier = Modifier.size(width = 350.dp, height = 287.dp)
            )
            Text(
                text = "Create Your First Notes",
                modifier = Modifier.align( Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp
                )
            )
        }
    }
}