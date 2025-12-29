package com.shoaib.notes_app_kmp.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.presentation.ui.theme.nunitoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun ListNotesScreen(
    list: List<Note>,
    onNoteClick: (Long) -> Unit = {}
) {


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list.size, key = { list[it].id }) { index ->
            NoteItem(
                note = list[index],
                onClick = { onNoteClick(list[index].id) }
            )
        }
    }
}

fun getRandomColor(): Color {
    // Generate pastel by ensuring high values with some variation
    val channel1 = (230..255).random()
    val channel2 = (200..255).random()
    val channel3 = (200..255).random()

    // Randomly arrange channels for variety
    val channels = listOf(channel1, channel2, channel3).shuffled()
    return Color(channels[0], channels[1], channels[2])
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(getRandomColor())
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = note.title,
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                color = Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = note.description,
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                color = Black,
                maxLines = 3
            )
        }
    }
}

@Composable
@Preview
fun ListNotesScreen(){
//    ListNotesScreen(
//        listOf(Note("This is the first not of Shoaib Ali i want to show ","ahgjvdjavdbabdkubakdbk"),
//    Note("This is the first not of Shoaib Ali i want to show ","ahgjvdjavdbabdkubakdbk")))

}