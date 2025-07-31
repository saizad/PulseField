package com.pulse.field.example.samples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulse.field.EmailField
import com.pulse.field.PasswordField
import com.pulse.field.ValidationState
import com.pulse.field.error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormSample() {
    // Create form fields
    val emailField = remember { EmailField("email", isMandatory = true) }
    val passwordField = remember { PasswordField("password", isMandatory = true) }
    
    // Collect field states
    val emailValue by emailField.fieldFlow.collectAsState()
    val emailValidationState by emailField.validationState.collectAsState(ValidationState.Loading)
    val emailIsValid by emailField.isValid.collectAsState(false)
    val emailError by emailField.error.collectAsState(initial = "")
    
    val passwordValue by passwordField.fieldFlow.collectAsState()
    val passwordValidationState by passwordField.validationState.collectAsState(ValidationState.Loading)
    val passwordIsValid by passwordField.isValid.collectAsState(false)
    val passwordError by passwordField.error.collectAsState(initial = "")
    
    // Form state
    val isFormValid = emailIsValid && passwordIsValid
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login Form Sample",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Email Field
                OutlinedTextField(
                    value = emailValue ?: "",
                    onValueChange = { emailField.updateField(it) },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailValue != null && !emailIsValid,
                    supportingText = {
                        when {
                            emailValue != null && !emailIsValid && emailError.isNotEmpty() -> {
                                Text(
                                    text = emailError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            emailIsValid -> {
                                Text(
                                    text = "Valid email",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        when (emailValidationState) {
                            is ValidationState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                            is ValidationState.Success -> {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = "Valid",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Password Field
                OutlinedTextField(
                    value = passwordValue ?: "",
                    onValueChange = { passwordField.updateField(it) },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordValue != null && !passwordIsValid,
                    supportingText = {
                        when {
                            passwordValue != null && !passwordIsValid && passwordError.isNotEmpty() -> {
                                Text(
                                    text = passwordError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            passwordIsValid -> {
                                Text(
                                    text = "Password looks good",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Login Button
                Button(
                    onClick = { 
                        // Handle login logic here
                        println("Login clicked with: ${emailValue}, password: ${passwordValue}")
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
                
                // Form Status
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFormValid) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = if (isFormValid) "✓ Form is valid and ready to submit" else "Please fix validation errors",
                        modifier = Modifier.padding(12.dp),
                        color = if (isFormValid) 
                            MaterialTheme.colorScheme.onPrimaryContainer 
                        else 
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
} 