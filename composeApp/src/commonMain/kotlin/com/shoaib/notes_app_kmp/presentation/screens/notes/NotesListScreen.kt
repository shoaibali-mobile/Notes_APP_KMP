package com.shoaib.notes_app_kmp.presentation.screens.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.ui.theme.nunitoFontFamily
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.rafiki
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState


@Composable
fun NotesListScreen(
    navController: NavHostController,
    viewModel: NotesViewModel
) {
    val notes by viewModel.notes.collectAsState()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddNoteFAB(
                onClick = { navController.navigate(Screen.NoteEditor.route) }
            )
        }
    ) { paddingValues ->
        NotesListContent(
            notes = notes,
            modifier = Modifier.padding(paddingValues)
        )
    }
}


@Composable
private fun NotesListContent(
    notes: List<Note>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NotesHeader()

        if (notes.isNotEmpty()) {
            ListNotesScreen(
                list = notes,
                onNoteClick = { /* Handle note click */ }
            )
        } else {
            EmptyView()
        }
    }
}

@Composable
private fun NotesHeader() {
    Text(
        text = "Notes",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        style = TextStyle(
            fontFamily = nunitoFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 43.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun AddNoteFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Text(
            text = "+",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.rafiki),
                contentDescription = null,
                modifier = Modifier.size(width = 300.dp, height = 250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Create Your First Notes",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

