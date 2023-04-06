package io.github.akiomik.seiun.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.akiomik.seiun.R
import io.github.akiomik.seiun.SeiunApplication
import io.github.akiomik.seiun.datastores.Credential
import io.github.akiomik.seiun.datastores.Session
import io.github.akiomik.seiun.ui.components.SingleLineTextField
import io.github.akiomik.seiun.ui.theme.Red700
import io.github.akiomik.seiun.viewmodels.LoginViewModel

@Composable
private fun AppName() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 66.sp,
        modifier = Modifier.padding(top = 20.dp, end = 20.dp, bottom = 8.dp, start = 20.dp)
    )
}

@Composable
private fun LoginTitle() {
    Text(
        text = stringResource(id = R.string.login_title),
        fontSize = 23.sp,
        modifier = Modifier.padding(horizontal = 23.dp)
    )
}

@Composable
private fun LoginForm(onLoginSuccess: () -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val savedCredential = viewModel.getCredential()
    var serviceProvider by remember { mutableStateOf(savedCredential.serviceProvider) }
    var handleOrEmail by remember { mutableStateOf(savedCredential.handleOrEmail) }
    var password by remember { mutableStateOf(savedCredential.password) }
    var valid by remember {
        mutableStateOf(
            viewModel.isLoginParamValid(
                serviceProvider,
                handleOrEmail,
                password
            )
        )
    }
    var errorMessage by remember { mutableStateOf("") }
    val loginErrorMessage = stringResource(id = R.string.login_error)
    var isSubmitting by remember { mutableStateOf(false) }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Red700, modifier = Modifier.padding(20.dp))
        }

        SingleLineTextField(
            value = serviceProvider,
            onValueChange = {
                serviceProvider = it
                valid = viewModel.isLoginParamValid(serviceProvider, handleOrEmail, password)
            },
            label = { Text(stringResource(id = R.string.service_provider)) },
            placeholder = { Text("bsky.social") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )

        SingleLineTextField(
            value = handleOrEmail,
            onValueChange = {
                handleOrEmail = it
                valid = viewModel.isLoginParamValid(serviceProvider, handleOrEmail, password)
            },
            label = { Text(stringResource(id = R.string.login_handle_or_email)) },
            placeholder = { Text(text = stringResource(id = R.string.login_handle_or_email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )

        SingleLineTextField(
            value = password,
            onValueChange = {
                password = it
                valid = viewModel.isLoginParamValid(serviceProvider, handleOrEmail, password)
            },
            label = { Text(stringResource(id = R.string.login_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )

        ElevatedButton(
            onClick = {
                Log.d(SeiunApplication.TAG, "Login as $handleOrEmail on $serviceProvider")
                isSubmitting = true

                // NOTE: Init atpClient here using serviceProvider
                SeiunApplication.instance!!.setAtpClient(serviceProvider)

                val authRepository = SeiunApplication.instance!!.authRepository
                authRepository.saveCredential(Credential(serviceProvider, handleOrEmail, password))

                viewModel.login(
                    handle = handleOrEmail,
                    password = password,
                    onSuccess = { session ->
                        Log.d(SeiunApplication.TAG, "Login successful")
                        isSubmitting = false
                        authRepository.saveSession(Session.fromISession(session))
                        onLoginSuccess()
                    },
                    onError = { error ->
                        Log.d(SeiunApplication.TAG, "Login failure: $error")
                        isSubmitting = false
                        errorMessage = error.message ?: loginErrorMessage
                    }
                )
            },
            enabled = valid && !isSubmitting,
            modifier = Modifier
                .padding(20.dp)
                .align(alignment = Alignment.End)

        ) {
            Text(stringResource(id = R.string.login_button))
        }
    }
}

@Composable
private fun CreateAccountButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(20.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(stringResource(id = R.string.login_or), modifier = Modifier.padding(bottom = 2.dp))
        TextButton(onClick = onClick) {
            Text(stringResource(id = R.string.login_create_account))
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onCreateAccountClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            AppName()
            LoginTitle()
            LoginForm(onLoginSuccess)
            CreateAccountButton(onCreateAccountClick)
        }
    }
}
