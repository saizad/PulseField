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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulse.field.*
import com.pulse.field.ValidationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndianUserRegistrationSample() {
    // Create India-specific form fields
    val fullNameField = remember { FullNameField("fullName", isMandatory = true) }
    val aadhaarField = remember { AadhaarField("aadhaar", isMandatory = true) }
    val phoneField = remember { PhoneNumberField("phone", isMandatory = true) }
    val pincodeField = remember { PincodeField("pincode", isMandatory = true) }
    val emailField = remember { EmailField("email", isMandatory = false) }
    val genderField = remember { GenderField("gender", isMandatory = true) }
    val addressField = remember { StringField("address", isMandatory = true) }
    
    // Collect field states
    val fullNameValue by fullNameField.fieldFlow.collectAsState()
    val fullNameIsValid by fullNameField.isValid.collectAsState(false)
    val fullNameError by fullNameField.error.collectAsState(initial = "")
    
    val aadhaarValue by aadhaarField.fieldFlow.collectAsState()
    val aadhaarIsValid by aadhaarField.isValid.collectAsState(false)
    val aadhaarError by aadhaarField.error.collectAsState(initial = "")
    
    val phoneValue by phoneField.fieldFlow.collectAsState()
    val phoneIsValid by phoneField.isValid.collectAsState(false)
    val phoneError by phoneField.error.collectAsState(initial = "")
    
    val pincodeValue by pincodeField.fieldFlow.collectAsState()
    val pincodeIsValid by pincodeField.isValid.collectAsState(false)
    val pincodeError by pincodeField.error.collectAsState(initial = "")
    
    val emailValue by emailField.fieldFlow.collectAsState()
    val emailIsValid by emailField.isValid.collectAsState(true) // Optional field
    val emailError by emailField.error.collectAsState(initial = "")
    
    val genderValue by genderField.fieldFlow.collectAsState()
    val genderIsValid by genderField.isValid.collectAsState(false)
    val genderError by genderField.error.collectAsState(initial = "")
    
    val addressValue by addressField.fieldFlow.collectAsState()
    val addressIsValid by addressField.isValid.collectAsState(false)
    val addressError by addressField.error.collectAsState(initial = "")
    
    // Form state
    val isFormValid = fullNameIsValid && aadhaarIsValid && phoneIsValid && 
                     pincodeIsValid && emailIsValid && genderIsValid && addressIsValid
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🇮🇳 Indian User Registration",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Text(
            text = "Register with Indian identification documents",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    label = { Text("Full Name (as per Aadhaar)") },
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
                
                // Aadhaar Number Field
                OutlinedTextField(
                    value = aadhaarValue ?: "",
                    onValueChange = { aadhaarField.updateField(it) },
                    label = { Text("Aadhaar Number") },
                    leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = aadhaarValue != null && !aadhaarIsValid,
                    supportingText = {
                        when {
                            aadhaarValue != null && !aadhaarIsValid && aadhaarError.isNotEmpty() -> {
                                Text(
                                    text = aadhaarError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            aadhaarIsValid -> {
                                Text(
                                    text = "✓ Valid Aadhaar number",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Text(
                                    text = "Enter 12-digit Aadhaar number",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    label = { Text("Mobile Number") },
                    leadingIcon = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "+91",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                            )
                            Icon(Icons.Default.Phone, contentDescription = null)
                        }
                    },
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
                                    text = "✓ Valid Indian mobile number",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Text(
                                    text = "Enter 10-digit mobile number",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Address Field
                OutlinedTextField(
                    value = addressValue ?: "",
                    onValueChange = { addressField.updateField(it) },
                    label = { Text("Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    minLines = 2,
                    maxLines = 3,
                    isError = addressValue != null && !addressIsValid,
                    supportingText = {
                        when {
                            addressValue != null && !addressIsValid && addressError.isNotEmpty() -> {
                                Text(
                                    text = addressError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            addressIsValid -> {
                                Text(
                                    text = "✓ Address provided",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Pincode Field
                OutlinedTextField(
                    value = pincodeValue ?: "",
                    onValueChange = { pincodeField.updateField(it) },
                    label = { Text("Pincode") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = pincodeValue != null && !pincodeIsValid,
                    supportingText = {
                        when {
                            pincodeValue != null && !pincodeIsValid && pincodeError.isNotEmpty() -> {
                                Text(
                                    text = pincodeError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            pincodeIsValid -> {
                                Text(
                                    text = "✓ Valid Indian pincode",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Text(
                                    text = "Enter 6-digit pincode",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Email Field (Optional)
                OutlinedTextField(
                    value = emailValue ?: "",
                    onValueChange = { emailField.updateField(it) },
                    label = { Text("Email Address (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailValue != null && !emailValue.isNullOrEmpty() && !emailIsValid,
                    supportingText = {
                        when {
                            emailValue != null && !emailValue.isNullOrEmpty() && !emailIsValid && emailError.isNotEmpty() -> {
                                Text(
                                    text = emailError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            emailValue != null && !emailValue.isNullOrEmpty() && emailIsValid -> {
                                Text(
                                    text = "✓ Valid email",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Text(
                                    text = "Optional - for notifications",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                
                // Register Button
                Button(
                    onClick = { 
                        println("Indian User Registration: Name=$fullNameValue, Aadhaar=$aadhaarValue, Phone=+91$phoneValue, Pincode=$pincodeValue, Email=$emailValue, Gender=$genderValue")
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Register Indian User")
                }
                
                // Form Status with Indian Theme
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFormValid) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isFormValid) "🇮🇳" else "⚠️",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = if (isFormValid) 
                                "Ready to register with Indian documents!" 
                            else 
                                "Please complete all required Indian verification fields",
                            color = if (isFormValid) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                // Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "🛡️ Your data is secure",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "We use industry-standard encryption to protect your Aadhaar and personal information.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
} 