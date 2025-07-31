package com.pulse.field.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulse.field.example.samples.*
import com.pulse.field.example.ui.theme.PulseFieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulseFieldTheme {
                PulseFieldSampleApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseFieldSampleApp() {
    var currentSample by remember { mutableStateOf<SampleType?>(null) }
    
    if (currentSample == null) {
        SampleListScreen(
            onSampleSelected = { currentSample = it }
        )
    } else {
        Column {
            // Top App Bar
            TopAppBar(
                title = { Text(currentSample!!.title) },
                navigationIcon = {
                    IconButton(onClick = { currentSample = null }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            
            // Sample Content
            when (currentSample) {
                SampleType.LOGIN -> LoginFormSample()
                SampleType.REGISTRATION -> RegistrationFormSample()
                                SampleType.INDIAN_USER -> IndianUserRegistrationSample()
                SampleType.PROFILE -> ProfileFormSample()
                SampleType.COMPLEX_FORM -> ComplexFormSample()
                SampleType.OTP -> OTPValidationSample()
                null -> {} // This won't happen due to the if condition above
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleListScreen(
    onSampleSelected: (SampleType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🚀 PulseField",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Reactive Form Validation Library",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Library Info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoChip("20+ Field Types")
                    InfoChip("Type Safe")
                    InfoChip("Reactive")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "📱 Sample Applications",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Sample List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SampleType.values()) { sample ->
                SampleCard(
                    sample = sample,
                    onClick = { onSampleSelected(sample) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Footer Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "✨ Features Demonstrated",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                val features = listOf(
                    "Real-time validation with Kotlin Flows",
                    "Multiple field types (String, Int, Double, etc.)",
                    "Indian-specific validations (Aadhaar, Phone, Pincode)",
                    "Complex form composition with DataForm",
                    "Jetpack Compose integration",
                    "Type-safe field management",
                    "Custom validation rules"
                )
                
                features.forEach { feature ->
                    Text(
                        text = "• $feature",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleCard(
    sample: SampleType,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = sample.icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sample.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = sample.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Tags
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sample.tags.forEach { tag ->
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

enum class SampleType(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tags: List<String>
) {
    LOGIN(
        title = "Login Form",
        description = "Basic email and password validation with real-time feedback",
        icon = Icons.Default.Login,
        tags = listOf("Basic", "Email", "Password")
    ),
    REGISTRATION(
        title = "Registration Form",
        description = "Comprehensive user registration with multiple field types",
        icon = Icons.Default.PersonAdd,
        tags = listOf("Complex", "Validation", "Multi-field")
    ),
    INDIAN_USER(
        title = "Indian User Registration",
        description = "Aadhaar, phone number, and pincode validation for Indian users",
        icon = Icons.Default.LocationOn,
        tags = listOf("Aadhaar", "Indian", "Localized")
    ),
    PROFILE(
        title = "Profile Form",
        description = "Showcase of different data types: Int, Float, Double, Long, Date, List",
        icon = Icons.Default.Person,
        tags = listOf("Data Types", "Advanced", "Showcase")
    ),
    COMPLEX_FORM(
        title = "Complex DataForm",
        description = "Product order form using DataForm with structured data building",
        icon = Icons.Default.ShoppingCart,
        tags = listOf("DataForm", "Complex", "E-commerce")
    ),
    OTP(
        title = "OTP Verification",
        description = "Two-step phone verification with OTP validation and countdown timer",
        icon = Icons.Default.SecurityUpdate,
        tags = listOf("OTP", "Security", "Two-Step")
    )
}