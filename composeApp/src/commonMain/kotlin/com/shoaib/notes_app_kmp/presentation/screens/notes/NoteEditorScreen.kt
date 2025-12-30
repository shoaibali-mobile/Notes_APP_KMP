package com.shoaib.notes_app_kmp.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoaib.notes_app_kmp.presentation.ui.theme.nunitoFontFamily
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.back_btn
import notes_app_kmp.composeapp.generated.resources.save_ic
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NoteEditorScreen(
    noteId: Long? = null,
    initialTitle: String = "",
    initialContent: String = "",
    onBackClick: () -> Unit = {},
    onVisibilityClick: () -> Unit = {},
    onSaveClick: (String, String) -> Unit = { _, _ -> },
    viewModel: NotesViewModel
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Track the last noteId we initialized for to prevent flickering
    var lastInitializedNoteId by remember(noteId) { mutableStateOf<Long?>(null) }

    // Use noteId as key to reset state only when navigating to a different note
    var title by remember(noteId) { mutableStateOf(initialTitle) }
    var content by remember(noteId) { mutableStateOf(initialContent) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showSaveConfirmationDialog by remember { mutableStateOf(false) }
    
    val hasChanges = remember(title, content, initialTitle, initialContent) {
        title != initialTitle || content != initialContent
    }
    
    // Only update when noteId changes (navigating to different note)
    // This prevents flickering when note data updates after save
    LaunchedEffect(noteId) {
        // Reset state when navigating to a different note
        if (lastInitializedNoteId != noteId) {
        title = initialTitle
        content = initialContent
            lastInitializedNoteId = noteId
        }
    }
    
    // Handle case where note data loads after screen appears (initial values were empty)
    // Only update if current values are still empty (user hasn't typed yet)
    LaunchedEffect(initialTitle, initialContent) {
        if (lastInitializedNoteId == noteId && title.isEmpty() && content.isEmpty()) {
            if (initialTitle.isNotEmpty() || initialContent.isNotEmpty()) {
                title = initialTitle
                content = initialContent
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
        ) {
            TopNavigationBar(
                onBackClick = {
                    if (hasChanges && (title.isNotBlank() || content.isNotBlank())) {
                        showSaveDialog = true
                    } else {
                        onBackClick()
                    }
                },
                onVisibilityClick = onVisibilityClick,
                onSaveClick = {

                    if (title.isNotBlank() || content.isNotBlank()) {
                        showSaveConfirmationDialog = true
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Please fill both title and content"
                            )
                        }
                    }
                }
            )

            NoteEditorContent(
                title = title,
                onTitleChange = { title = it },
                content = content,
                onContentChange = { content = it },
                modifier = Modifier.weight(1f)
            )
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        )

        // Save Confirmation Dialog (for back button)
        if (showSaveDialog) {
            SaveConfirmationDialog(
                onSave = {
                    // Track save confirmation with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val saveParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null)
                    )
                    currentUserId?.let { saveParams["user_id"] = it }
                    AnalyticsHelper.logEvent("note_save_confirmed", saveParams)
                    onSaveClick(title, content)
                    showSaveDialog = false
                    onBackClick()
                },
                onDiscard = {
                    // Track discard action with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val discardParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null)
                    )
                    currentUserId?.let { discardParams["user_id"] = it }
                    AnalyticsHelper.logEvent("note_changes_discarded", discardParams)
                    showSaveDialog = false
                    onBackClick()
                },
                onDismiss = {
                    // Track dialog dismissal with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val dismissParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null)
                    )
                    currentUserId?.let { dismissParams["user_id"] = it }
                    AnalyticsHelper.logEvent("save_dialog_dismissed", dismissParams)
                    showSaveDialog = false
                }
            )
        }

        // Save Confirmation Dialog (for save button) - with Yes/No/Not Now options
        if (showSaveConfirmationDialog) {
            SaveNoteConfirmationDialog(
                onYes = {
                    // Track save confirmation with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val saveParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null),
                        "source" to "save_button"
                    )
                    currentUserId?.let { saveParams["user_id"] = it }
                    AnalyticsHelper.logEvent("note_save_confirmed", saveParams)
                    onSaveClick(title, content)
                    showSaveConfirmationDialog = false
                    onBackClick()
                },
                onNo = {
                    // Track "No" action with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val cancelParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null),
                        "source" to "save_button"
                    )
                    currentUserId?.let { cancelParams["user_id"] = it }
                    AnalyticsHelper.logEvent("note_save_cancelled", cancelParams)
                    showSaveConfirmationDialog = false
                },
                onNotNow = {
                    // Track "Not Now" action with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val postponeParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null),
                        "source" to "save_button"
                    )
                    currentUserId?.let { postponeParams["user_id"] = it }
                    AnalyticsHelper.logEvent("note_save_postponed", postponeParams)
                    showSaveConfirmationDialog = false
                },
                onDismiss = {
                    // Track dialog dismissal with dynamic user ID
                    val currentUserId = com.shoaib.notes_app_kmp.util.UserSetup.getCurrentUserId()
                    val dismissParams = mutableMapOf<String, Any>(
                        "is_new_note" to (noteId == null)
                    )
                    currentUserId?.let { dismissParams["user_id"] = it }
                    AnalyticsHelper.logEvent("save_confirmation_dialog_dismissed", dismissParams)
                    showSaveConfirmationDialog = false
                }
            )
        }
    }
}

@Composable
private fun NoteEditorContent(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
            Column(
        modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
        TitleInputField(
            title = title,
            onTitleChange = onTitleChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            ContentInputField(
                content = content,
                onContentChange = onContentChange
            )
        }
    }
}



@Composable
private fun TitleInputField(
    title: String,
    onTitleChange: (String) -> Unit
) {
                BasicTextField(
                    value = title,
        onValueChange = onTitleChange,
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                            Text(
                                text = "Title",
                                style = TextStyle(
                                    fontFamily = nunitoFontFamily(),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
            innerTextField()
        }
    )
}

@Composable
private fun ContentInputField(
    content: String,
    onContentChange: (String) -> Unit
) {
                BasicTextField(
                    value = content,
        onValueChange = onContentChange,
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
            .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
        decorationBox = { innerTextField ->
            if (content.isEmpty()) {
                            Text(
                                text = "Type something....",
                                style = TextStyle(
                                    fontFamily = nunitoFontFamily(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
            innerTextField()
        }
    )
}

@Composable
fun TopNavigationBar(
    onBackClick: () -> Unit,
    onVisibilityClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Icon(
                painter = painterResource(Res.drawable.back_btn),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurfaceVariant, // Makes the icon white
                modifier = Modifier.size(24.dp)

            )
        }

        // Right side buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Visibility Button
            IconButton(
                onClick = onVisibilityClick,
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Text(
                    text = "👁",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Save Button
            IconButton(
                onClick = onSaveClick,
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.save_ic),
                    contentDescription = "save",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, // Makes the icon white
                    modifier = Modifier.size(24.dp)

                )
            }
        }

    }
}


@Composable
fun SaveConfirmationDialog(
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Save Changes?",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        text = {
            Text(
                text = "You have unsaved changes. Do you want to save them?",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onSave,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDiscard,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Discard",
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun SaveNoteConfirmationDialog(
    onYes: () -> Unit,
    onNo: () -> Unit,
    onNotNow: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Save Note?",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        text = {
            Text(
                text = "Do you want to save this note?",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onYes,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Yes",
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onNotNow,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Not Now",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                TextButton(
                    onClick = onNo,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

// Preview commented out - requires viewModel parameter
//@Preview
//@Composable
//fun NoteEditorScreenPreview() {
//    NoteEditorScreen()
//}

//@Preview
//@Composable
//fun NoteEditorScreenPreview() {
//    // Create mock use cases with a mock repository
//    val mockRepository = object : com.shoaib.notes_app_kmp.domain.repository.NotesRepository {
//        override fun getAllNotes() = flowOf(emptyList())
//        override suspend fun addNote(note: com.shoaib.notes_app_kmp.domain.model.Note) = 0L
//    }
//
//    val mockGetNotesUseCase = GetNotesUseCase(mockRepository)
//    val mockAddNoteUseCase = AddNoteUseCase(mockRepository)
//
//    val mockViewModel = NotesViewModel(
//        getNotesUseCase = mockGetNotesUseCase,
//        addNoteUseCase = mockAddNoteUseCase
//    )
//
//    NoteEditorScreen(
//        viewModel = mockViewModel
//    )
//}
