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
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.back_btn
import notes_app_kmp.composeapp.generated.resources.save_ic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun NoteEditorScreen(
    noteId: Long? = null,
    initialTitle: String = "",
    initialContent: String = "",
    onBackClick: () -> Unit = {},
    onVisibilityClick: () -> Unit = {},
    onSaveClick: (String, String) -> Unit = { _, _ -> }
) {

    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }
    var showSaveDialog by remember { mutableStateOf(false) }
    
    val hasChanges = remember(title, content, initialTitle, initialContent) {
        title != initialTitle || content != initialContent
    }
    
    LaunchedEffect(noteId, initialTitle, initialContent) {
        title = initialTitle
        content = initialContent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
        ) {
            // Top Navigation Bar
            TopNavigationBar(
                onBackClick = onBackClick,
                onVisibilityClick = onVisibilityClick,
                onSaveClick = {
                    if (hasChanges && (title.isNotBlank() || content.isNotBlank())) {
                        showSaveDialog = true
                    } else if (title.isNotBlank() || content.isNotBlank()) {
                        onSaveClick(title, content)
                        onBackClick()
                    } else {
                        onBackClick()
                    }
                }
            )

            // Content Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Title Input
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    decorationBox = { innerTextFiled->
                        if (title.isEmpty()){
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
                        innerTextFiled()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Content Input
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    decorationBox = { innerTextFiled->
                        if (content.isEmpty()){
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
                        innerTextFiled()

                    }
                )
            }
        }
        
        // Save Confirmation Dialog
        if (showSaveDialog) {
            SaveConfirmationDialog(
                onSave = {
                    onSaveClick(title, content)
                    showSaveDialog = false
                    onBackClick()
                },
                onDiscard = {
                    showSaveDialog = false
                    onBackClick()
                },
                onDismiss = {
                    showSaveDialog = false
                }
            )
        }
    }
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
                    contentDescription = "Back",
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

@Preview
@Composable
fun NoteEditorScreenPreview() {
    NoteEditorScreen()
}

