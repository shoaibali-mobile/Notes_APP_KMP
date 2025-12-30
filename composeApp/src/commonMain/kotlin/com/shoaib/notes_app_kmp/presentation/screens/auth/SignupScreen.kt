package com.shoaib.notes_app_kmp.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.ui.theme.nunitoFontFamily
import com.shoaib.notes_app_kmp.presentation.viewmodel.AuthViewModel
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import org.koin.compose.koinInject
import androidx.compose.runtime.collectAsState

@Composable
fun SignupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = koinInject()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by authViewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Create Account",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle
            Text(
                text = "Sign up to get started",
                style = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = "Full Name",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 14.sp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp
                )
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        text = "Email",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 14.sp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp
                )
            )

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "Password",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 14.sp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp
                )
            )

            // Confirm Password Input
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(
                        text = "Confirm Password",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 14.sp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Text(
                            text = if (confirmPasswordVisible) "👁️" else "👁️‍🗨️",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    fontFamily = nunitoFontFamily(),
                    fontSize = 16.sp
                )
            )

            // Sign Up Button
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }
                        
                        errorMessage = null
                        AnalyticsHelper.logEvent("signup_button_clicked", mapOf(
                            "has_name" to name.isNotBlank(),
                            "has_email" to email.isNotBlank(),
                            "has_password" to password.isNotBlank(),
                            "passwords_match" to (password == confirmPassword)
                        ))
                        
                        authViewModel.signup(
                            username = email.trim(),
                            password = password,
                            onSuccess = { user ->
                                // Set Firebase user ID dynamically
                                com.shoaib.notes_app_kmp.util.UserSetup.updateUser(user.id, user.username)
                                
                                AnalyticsHelper.logEvent("signup_success", mapOf(
                                    "user_id" to user.id,
                                    "username" to user.username
                                ))
                                
                                // Navigate to notes list after successful signup
                                navController.navigate(Screen.NotesList.createRoute(user.id)) {
                                    popUpTo(Screen.Signup.route) { inclusive = true }
                                }
                            },
                            onError = { error ->
                                errorMessage = error
                                AnalyticsHelper.logEvent("signup_failed", mapOf(
                                    "error" to error
                                ))
                            }
                        )
                    } else {
                        errorMessage = "Please fill all required fields"
                    }
                },
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Sign Up",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
            
            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 14.sp
                    )
                )
            }

            // Login link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = TextStyle(
                        fontFamily = nunitoFontFamily(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                TextButton(
                    onClick = {
                        AnalyticsHelper.logEvent("navigate_to_login", mapOf(
                            "source" to "signup_screen"
                        ))
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Login",
                        style = TextStyle(
                            fontFamily = nunitoFontFamily(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

