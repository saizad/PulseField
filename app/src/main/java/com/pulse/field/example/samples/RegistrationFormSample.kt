package com.pulse.field.example.samples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.pulse.field.*
import com.pulse.field.ValidationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationFormSample() {
    // Create form fields
    val fullNameField = remember { FullNameField("fullName", isMandatory = true) }
    val emailField = remember { EmailField("email", isMandatory = true) }
    val passwordField = remember { PasswordField("password", isMandatory = true) }
    val confirmPasswordField = remember { ConfirmPasswordField("confirmPassword", isMandatory = true, passwordField = passwordField) }
    val ageField = remember { IntField("age", isMandatory = true) }
    val phoneField = remember { PhoneNumberField("phone", isMandatory = true) }
    val genderField = remember { GenderField("gender", isMandatory = true) }
    
    // Collect field states
    val fullNameValue by fullNameField.fieldFlow.collectAsState()
    val fullNameIsValid by fullNameField.isValid.collectAsState(false)
    val fullNameError by fullNameField.error.collectAsState(initial = "")
    
    val emailValue by emailField.fieldFlow.collectAsState()
    val emailIsValid by emailField.isValid.collectAsState(false)
    val emailError by emailField.error.collectAsState(initial = "")
    
    val passwordValue by passwordField.fieldFlow.collectAsState()
    val passwordIsValid by passwordField.isValid.collectAsState(false)
    val passwordError by passwordField.error.collectAsState(initial = "")
    
    val confirmPasswordValue by confirmPasswordField.fieldFlow.collectAsState()
    val confirmPasswordIsValid by confirmPasswordField.isValid.collectAsState(false)
    val confirmPasswordError by confirmPasswordField.error.collectAsState(initial = "")
    
    val ageValue by ageField.fieldFlow.collectAsState()
    val ageIsValid by ageField.isValid.collectAsState(false)
    val ageError by ageField.error.collectAsState(initial = "")
    
    val phoneValue by phoneField.fieldFlow.collectAsState()
    val phoneIsValid by phoneField.isValid.collectAsState(false)
    val phoneError by phoneField.error.collectAsState(initial = "")
    
    val genderValue by genderField.fieldFlow.collectAsState()
    val genderIsValid by genderField.isValid.collectAsState(false)
    val genderError by genderField.error.collectAsState(initial = "")
    
    // Form state
    val isFormValid = fullNameIsValid && emailIsValid && passwordIsValid && 
                     confirmPasswordIsValid && ageIsValid && phoneIsValid && genderIsValid
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    // Note: confirmPasswordField automatically references passwordField
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registration Form Sample",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Full Name Field
                OutlinedTextField(
                    value = fullNameValue ?: "",
                    onValueChange = { fullNameField.updateField(it) },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = fullNameValue != null && !fullNameIsValid,
                    supportingText = {
                        when {
                            fullNameValue != null && !fullNameIsValid && fullNameError.isNotEmpty() -> {
                                Text(
                                    text = fullNameError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            fullNameIsValid -> {
                                Text(
                                    text = "✓ Valid full name",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
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
                                    text = "✓ Valid email",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Phone Number Field
                OutlinedTextField(
                    value = phoneValue ?: "",
                    onValueChange = { phoneField.updateField(it) },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneValue != null && !phoneIsValid,
                    supportingText = {
                        when {
                            phoneValue != null && !phoneIsValid && phoneError.isNotEmpty() -> {
                                Text(
                                    text = phoneError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            phoneIsValid -> {
                                Text(
                                    text = "✓ Valid phone number",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Age Field
                OutlinedTextField(
                    value = ageValue?.toString() ?: "",
                    onValueChange = { ageField.updateField(it.toIntOrNull()) },
                    label = { Text("Age") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = ageValue != null && !ageIsValid,
                    supportingText = {
                        when {
                            ageValue != null && !ageIsValid && ageError.isNotEmpty() -> {
                                Text(
                                    text = ageError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            ageIsValid -> {
                                Text(
                                    text = "✓ Valid age",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Gender Selection
                Column {
                    Text(
                        text = "Gender *",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf("Male", "Female").forEach { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = genderValue?.name == gender,
                                    onClick = { genderField.updateField(gender.toGender()) }
                                )
                                Text(
                                    text = gender,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                    if (genderValue != null && !genderIsValid && genderError.isNotEmpty()) {
                        Text(
                            text = genderError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
                
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
                                    text = "✓ Strong password",
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
                
                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPasswordValue ?: "",
                    onValueChange = { confirmPasswordField.updateField(it) },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = confirmPasswordValue != null && !confirmPasswordIsValid,
                    supportingText = {
                        when {
                            confirmPasswordValue != null && !confirmPasswordIsValid && confirmPasswordError.isNotEmpty() -> {
                                Text(
                                    text = confirmPasswordError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            confirmPasswordIsValid -> {
                                Text(
                                    text = "✓ Passwords match",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Register Button
                Button(
                    onClick = { 
                        println("Registration data: Name=$fullNameValue, Email=$emailValue, Phone=$phoneValue, Age=$ageValue, Gender=$genderValue")
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Account")
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
                        text = if (isFormValid) "✓ All fields are valid - Ready to register!" else "Please complete all required fields",
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