package com.example.onlinebankapp.domain.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun SignUpScreen(
    navController: NavController,
    parentNavController: NavController,
    viewModel: UserViewModel
) {
    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    Box( modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlightlyGrey),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AnotherGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 36.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color.Gray
                            )
                        }
                    )

                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = "Username"
                    )

                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        passwordVisibility = passwordVisibility,
                        onPasswordVisibilityChange = { passwordVisibility = !passwordVisibility }
                    )

                    Button(
                        onClick = {
                            viewModel.authenticateUser(email, password, userName,
                                isRegistration = true)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 48.dp, end = 48.dp, bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.DarkGray
                        )
                    ) {
                        Text("Sign Up")
                    }
                }
            }

            TextButton(
                onClick = {
                    navController.navigate("login")
                    showToast = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.DarkGray
                )
            ) {
                Text("Already have an account? Login")
            }
        }
    }

    when (val state = authState) {
        is Resource.Loading -> { }
        is Resource.Success -> {
            LaunchedEffect(state) {
                parentNavController.navigate("main")
            }
        }
        is Resource.Error -> {
            LaunchedEffect(state) {
                showToast = true
                toastMessage = state.message ?: "Unknown Error"
            }
        }
    }

    ErrorToast(
        message = toastMessage,
        isVisible = showToast,
        onDismiss = { showToast = false }
    )
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.DarkGray) },
        leadingIcon = leadingIcon,
        textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.DarkGray,
            unfocusedTextColor = Color.DarkGray
        )
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisibility: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password", color = Color.DarkGray) },
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Password",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityChange) {
                Image(
                    painter = if (passwordVisibility)
                        painterResource(id = R.drawable.visible)
                    else
                        painterResource(id = R.drawable.hidden),
                    modifier = Modifier.size(24.dp),
                    contentDescription = if (passwordVisibility)
                        "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = if (passwordVisibility)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.DarkGray,
            unfocusedTextColor = Color.DarkGray
        )
    )
}