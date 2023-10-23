package io.github.dracula101.todo.ui.login_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onSubmitClicked: (name:String, email: String, password: String) -> Unit = { s: String, s1: String, s2: String -> },
    registerUiEvent: AuthEvent = AuthEvent.StandBy,
    onLoginClicked: () -> Unit = {},
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val name = rememberSaveable {
        mutableStateOf("")
    }
    val isEmailFieldValid = rememberSaveable {
        mutableStateOf(true)
    }
    val isPasswordFieldValid = rememberSaveable {
        mutableStateOf(true)
    }
    val isEmailVerified = rememberSaveable {
        mutableStateOf(false)
    }
    val isPasswordVerified = rememberSaveable {
        mutableStateOf(false)
    }
    val isNameVerified = rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical,
                    enabled = true,
                )
        ) {
            IconButton(
                onClick = {
                },
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            RegisterTitle()
            Spacer(modifier = Modifier.size(36.dp))
            RegisterForm(
                name = name,
                email = email,
                password = password,
                isEmailFieldValid = isEmailFieldValid,
                isPasswordFieldValid = isPasswordFieldValid,
                isEmailVerified = isEmailVerified,
                isPasswordVerified = isPasswordVerified,
                isNameVerified = isNameVerified,
                focusManager = focusManager,
                keyboardManager = keyboardManager,
            )
            Spacer(modifier = Modifier.size(54.dp))
            when (registerUiEvent) {
                is AuthEvent.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 1.5.dp
                        )
                    }
                }
                else -> {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            when {
                                name.value.isEmpty() -> {
                                    isNameVerified.value = false
                                }
                                email.value.isEmpty() -> {
                                    isEmailFieldValid.value = false
                                    isEmailVerified.value = false
                                }
                                password.value.isEmpty() -> {
                                    isPasswordFieldValid.value = false
                                    isPasswordVerified.value = false
                                }
                                else -> {
                                    onSubmitClicked(name.value, email.value, password.value)
                                }
                            }

                        }
                    ) {
                        Text(
                            text = "Register",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(16.dp))
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ){
                Column(modifier = Modifier) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLoginClicked
                    ) {
                        Text(
                            text = "Already have an account? Login",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    name: MutableState<String>,
    email: MutableState<String>,
    password: MutableState<String>,
    isEmailFieldValid: MutableState<Boolean>,
    isPasswordFieldValid: MutableState<Boolean>,
    isEmailVerified: MutableState<Boolean>,
    isPasswordVerified: MutableState<Boolean>,
    isNameVerified : MutableState<Boolean>,
    focusManager: FocusManager,
    keyboardManager: SoftwareKeyboardController?= null,
) {

    val isPasswordVisible = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Name",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            placeholder = {
                Text(
                    text = "Enter your name",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = "Name",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                    )
                )
            },
            trailingIcon = {
                if (isNameVerified.value) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = "Email Name",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                        )
                    )
                } else if (!isEmailFieldValid.value) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "No name given",
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                            )
                            .clip(CircleShape)
                            .clickable {
                                email.value = ""
                                isEmailFieldValid.value = true
                                isEmailVerified.value = false
                            }
                    )
                } else {
                    Spacer(modifier = Modifier.size(16.dp))
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if(name.value.isNotEmpty()) {
                        isNameVerified.value = true
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                    else {
                        isNameVerified.value = false
                    }
                }
            ),
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            supportingText = {
                Text(
                    text = if (isEmailFieldValid.value) "" else "Invalid Email",
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            isError = !isEmailFieldValid.value,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            placeholder = {
                Text(
                    text = "Enter your email",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Email,
                    contentDescription = "Email",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                    )
                )
            },
            trailingIcon = {
                if (isEmailVerified.value) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = "Email Valid",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                        )
                    )
                } else if (!isEmailFieldValid.value) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Invalid Email",
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                            )
                            .clip(CircleShape)
                            .clickable {
                                email.value = ""
                                isEmailFieldValid.value = true
                                isEmailVerified.value = false
                            }
                    )
                } else {
                    Spacer(modifier = Modifier.size(16.dp))
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if (email.value.isNotEmpty() && isEmailValid(email.value)) {
                        isEmailFieldValid.value = true
                        isEmailVerified.value = true
                        focusManager.moveFocus(FocusDirection.Down)
                    } else {
                        isEmailFieldValid.value = false
                        isEmailVerified.value = false
                    }
                }
            ),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            supportingText = {
                Text(
                    text = if (isPasswordFieldValid.value) "" else "Password not correct",
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            placeholder = {
                Text(
                    text = "Enter your password",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = "Password",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                    )
                )
            },
            trailingIcon = {
                if(password.value!=""){
                    TextButton(onClick = {
                        isPasswordVisible.value = !isPasswordVisible.value
                    }) {
                        Text(
                            text = if (isPasswordVisible.value) "Hide" else "Show",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier= Modifier.padding(end = 4.dp)
                        )
                    }
                }
            },
            isError = !isPasswordFieldValid.value,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
            ),
            visualTransformation = if (isPasswordVisible.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    if (email.value.isNotEmpty() && isEmailValid(email.value)) {
                        isEmailFieldValid.value = true
                        isEmailVerified.value = true
                        focusManager.clearFocus()
                    } else {
                        isEmailFieldValid.value = false
                        isEmailFieldValid.value = false
                    }
                    if (password.value.isNotEmpty()) {
                        isPasswordFieldValid.value = true
                        isPasswordVerified.value = true
                        keyboardManager?.hide()
                    } else {
                        isPasswordFieldValid.value = false
                        isPasswordVerified.value = false
                    }
                }
            ),
        )
    }
}


@Composable
fun RegisterTitle() {
    MaterialTheme.typography.headlineLarge?.copy(
        fontSize = 30.sp
    )?.let {
        Text(
            text = "Register",
            style = it,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
        )
    }
}