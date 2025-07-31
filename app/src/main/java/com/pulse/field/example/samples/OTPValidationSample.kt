package com.pulse.field.example.samples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulse.field.OTPField
import com.pulse.field.PhoneNumberField
import com.pulse.field.ValidationState
import com.pulse.field.error
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPValidationSample() {
    // Create form fields
    val phoneField = remember { PhoneNumberField("phone", isMandatory = true) }
    val otpField = remember { OTPField("otp", isMandatory = true) }
    
    // Collect field states
    val phoneValue by phoneField.fieldFlow.collectAsState()
    val phoneIsValid by phoneField.isValid.collectAsState(false)
    val phoneError by phoneField.error.collectAsState(initial = "")
    
    val otpValue by otpField.fieldFlow.collectAsState()
    val otpIsValid by otpField.isValid.collectAsState(false)
    val otpError by otpField.error.collectAsState(initial = "")
    
    // OTP sending state
    var otpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(0) }
    
    // Countdown timer
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📱 OTP Verification",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Secure phone number verification",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                if (!otpSent) {
                    // Phone Number Entry Step
                    Text(
                        text = "Step 1: Enter Phone Number",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
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
                                        text = "✓ Valid phone number",
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
                    
                    Button(
                        onClick = { 
                            isLoading = true
                            // Simulate API call
                            otpSent = true
                            countdown = 30
                            isLoading = false
                        },
                        enabled = phoneIsValid && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Send OTP")
                    }
                    
                } else {
                    // OTP Entry Step
                    Text(
                        text = "Step 2: Enter OTP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "We've sent a 6-digit code to +91${phoneValue}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    OutlinedTextField(
                        value = otpValue ?: "",
                        onValueChange = { otpField.updateField(it) },
                        label = { Text("Enter OTP") },
                        leadingIcon = { Icon(Icons.Default.SecurityUpdate, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = otpValue != null && !otpIsValid,
                        supportingText = {
                            when {
                                otpValue != null && !otpIsValid && otpError.isNotEmpty() -> {
                                    Text(
                                        text = otpError,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                otpIsValid -> {
                                    Text(
                                        text = "✓ Valid OTP format",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Enter 6-digit OTP",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { 
                                if (countdown == 0) {
                                    countdown = 30
                                    // Simulate resend API call
                                }
                            },
                            enabled = countdown == 0,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (countdown > 0) "Resend in ${countdown}s" else "Resend OTP"
                            )
                        }
                        
                        Button(
                            onClick = { 
                                // Handle OTP verification
                                println("OTP Verified: $otpValue for phone: +91$phoneValue")
                            },
                            enabled = otpIsValid,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Verify")
                        }
                    }
                    
                    TextButton(
                        onClick = { 
                            otpSent = false
                            otpField.clear()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Change Number")
                    }
                }
                
                // Status Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            !otpSent && phoneIsValid -> MaterialTheme.colorScheme.primaryContainer
                            otpSent && otpIsValid -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when {
                            !otpSent && phoneIsValid -> "📱"
                            otpSent && otpIsValid -> "✅"
                            otpSent -> "⏳"
                            else -> "📝"
                        }
                        
                        Text(
                            text = icon,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        Text(
                            text = when {
                                !otpSent && phoneIsValid -> "Ready to send OTP"
                                otpSent && otpIsValid -> "OTP verified successfully!"
                                otpSent -> "Enter the OTP sent to your phone"
                                else -> "Enter your phone number to begin"
                            }
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
                            text = "🔐 Security Features",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val features = listOf(
                            "OTP expires in 5 minutes",
                            "Maximum 3 verification attempts",
                            "SMS delivery within 30 seconds",
                            "Your number is encrypted and secure"
                        )
                        features.forEach { feature ->
                            Text(
                                text = "• $feature",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
} 