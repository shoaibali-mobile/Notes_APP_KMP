package com.shoaib.notes_app_kmp.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoaib.notes_app_kmp.presentation.ui.theme.nunitoFontFamily
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.back_btn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NoteEditorScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onVisibilityClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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
                onSaveClick = onSaveClick
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
                Text(
                    text = "💾",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
fun NoteEditorScreenPreview() {
    NoteEditorScreen()
}

