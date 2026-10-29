package com.example.bakery.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bakery.shared.generated.resources.Res
import com.example.bakery.auth.domain.PasswordError
import com.example.bakery.auth.domain.UsernameError
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel
import bakery.shared.generated.resources.login_error_username_empty
import bakery.shared.generated.resources.login_error_username_short
import bakery.shared.generated.resources.login_error_username_chars
import bakery.shared.generated.resources.login_error_password_empty
import bakery.shared.generated.resources.login_error_password_short
import bakery.shared.generated.resources.login_error_password_letter
import bakery.shared.generated.resources.login_error_password_digit
import com.example.bakery.presentation.ui.theme.primaryContainerLight
import org.jetbrains.compose.resources.stringResource
import bakery.shared.generated.resources.login_title
import bakery.shared.generated.resources.login_subtitle
import com.example.bakery.presentation.ui.theme.onSurfaceLight
import bakery.shared.generated.resources.login_username
import bakery.shared.generated.resources.login_password
import bakery.shared.generated.resources.login_hide
import bakery.shared.generated.resources.login_show
import com.example.bakery.presentation.ui.theme.errorDark
import bakery.shared.generated.resources.login_error_auth
import bakery.shared.generated.resources.login_sign_in
import com.example.bakery.presentation.ui.theme.backgroundLight
import com.example.bakery.presentation.ui.theme.errorLight
import com.example.bakery.presentation.ui.theme.onPrimaryLightHighContrast
import com.example.bakery.presentation.ui.theme.onSecondaryLight
import com.example.bakery.presentation.ui.theme.onSecondaryLightHighContrast
import com.example.bakery.presentation.ui.theme.onSurfaceDark
import com.example.bakery.presentation.ui.theme.outlineLight
import com.example.bakery.presentation.ui.theme.primaryLight

@Composable
fun LoginScreen(
    onSignedIn: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
){
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = onSurfaceLight,
        unfocusedTextColor = onSurfaceLight,
        focusedBorderColor = primaryLight,
        unfocusedBorderColor = outlineLight,
        focusedLabelColor = primaryLight,
        unfocusedLabelColor = onSurfaceLight,
        cursorColor = primaryLight,
    )
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> onSignedIn()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryContainerLight)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(Res.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
        )
        Card(modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                Color.Transparent
            )
        ){
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    colors = fieldColors,
                    value = state.username,
                    onValueChange = { viewModel.onIntent(LoginIntent.UsernameChanged(it)) },
                    label = { Text(stringResource(Res.string.login_username)) },
                    isError = state.usernameError != null,
                    supportingText = state.usernameError?.let { e ->
                        { Text(stringResource(e.toRes()), color = onSurfaceLight) }
                    },
                    singleLine = true,
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocus.requestFocus() },
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    colors = fieldColors,
                    value = state.password,
                    onValueChange = { viewModel.onIntent(LoginIntent.PasswordChanged(it)) },
                    label = { Text(stringResource(Res.string.login_password)) },
                    isError = state.passwordError != null,
                    supportingText = state.passwordError?.let { e ->
                        { Text(stringResource(e.toRes())) }
                    },
                    singleLine = true,
                    enabled = !state.isLoading,
                    visualTransformation =
                        if (state.isPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { viewModel.onIntent(LoginIntent.TogglePasswordVisibility) }) {
                            Text(
                                stringResource(
                                    if (state.isPasswordVisible) Res.string.login_hide
                                    else Res.string.login_show
                                ),
                                color = onSurfaceLight
                            )

                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.onIntent(LoginIntent.SignInClicked)
                        },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocus),
                )

                if (state.authError) {
                    Text(
                        text = stringResource(Res.string.login_error_auth),
                        color = errorLight,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onIntent(LoginIntent.SignInClicked)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLight,
                        contentColor = onPrimaryLightHighContrast,),
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.login_sign_in),
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }
        }
        }
    }
}

private fun UsernameError.toRes(): StringResource = when (this) {
    UsernameError.Empty -> Res.string.login_error_username_empty
    UsernameError.TooShort -> Res.string.login_error_username_short
    UsernameError.InvalidChars -> Res.string.login_error_username_chars
}

private fun PasswordError.toRes(): StringResource = when (this) {
    PasswordError.Empty -> Res.string.login_error_password_empty
    PasswordError.TooShort -> Res.string.login_error_password_short
    PasswordError.NoLetter -> Res.string.login_error_password_letter
    PasswordError.NoDigit -> Res.string.login_error_password_digit
}


