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
fun ProfileFormSample() {
    // Create different data type fields
    val nameField = remember { NameField("name", isMandatory = true) }
    val ageField = remember { IntField("age", isMandatory = true) }
    val heightField = remember { FloatField("height", isMandatory = true) }
    val weightField = remember { DoubleField("weight", isMandatory = true) }
    val salaryField = remember { LongField("salary", isMandatory = false) }
    val birthdateField = remember { DateField("birthdate", isMandatory = true) }
    val skillsField = remember { ListField<String>("skills", isMandatory = false) }
    val bioField = remember { StringField("bio", isMandatory = false) }
    val experienceField = remember { QuantityField("experience", isMandatory = true, minQuantity = 0, maxQuantity = 50) }
    
    // Collect field values
    val nameValue by nameField.fieldFlow.collectAsState()
    val nameIsValid by nameField.isValid.collectAsState(false)
    val nameError by nameField.error.collectAsState(initial = "")
    
    val ageValue by ageField.fieldFlow.collectAsState()
    val ageIsValid by ageField.isValid.collectAsState(false)
    val ageError by ageField.error.collectAsState(initial = "")
    
    val heightValue by heightField.fieldFlow.collectAsState()
    val heightIsValid by heightField.isValid.collectAsState(false)
    val heightError by heightField.error.collectAsState(initial = "")
    
    val weightValue by weightField.fieldFlow.collectAsState()
    val weightIsValid by weightField.isValid.collectAsState(false)
    val weightError by weightField.error.collectAsState(initial = "")
    
    val salaryValue by salaryField.fieldFlow.collectAsState()
    val salaryIsValid by salaryField.isValid.collectAsState(true) // Optional
    val salaryError by salaryField.error.collectAsState(initial = "")
    
    val birthdateValue by birthdateField.fieldFlow.collectAsState()
    val birthdateIsValid by birthdateField.isValid.collectAsState(false)
    val birthdateError by birthdateField.error.collectAsState(initial = "")
    
    val skillsValue by skillsField.fieldFlow.collectAsState()
    val skillsIsValid by skillsField.isValid.collectAsState(true) // Optional
    
    val bioValue by bioField.fieldFlow.collectAsState()
    val bioIsValid by bioField.isValid.collectAsState(true) // Optional
    
    val experienceValue by experienceField.fieldFlow.collectAsState()
    val experienceIsValid by experienceField.isValid.collectAsState(false)
    val experienceError by experienceField.error.collectAsState(initial = "")
    
    // Form state
    val isFormValid = nameIsValid && ageIsValid && heightIsValid && weightIsValid && 
                     salaryIsValid && birthdateIsValid && skillsIsValid && bioIsValid && experienceIsValid
    
    // Skills management
    val availableSkills = listOf(
        "Kotlin", "Java", "Python", "JavaScript", "Swift", 
        "React", "Flutter", "Android", "iOS", "Web Development",
        "Machine Learning", "Data Science", "DevOps", "Design", "Testing"
    )
    var selectedSkills by remember { mutableStateOf(setOf<String>()) }
    var customSkill by remember { mutableStateOf("") }
    
    // Update skills in the form
    LaunchedEffect(selectedSkills) {
        skillsField.updateField(selectedSkills.toList())
    }
    
    // Calculate BMI from height and weight
    val bmi by remember {
        derivedStateOf {
            val h = heightValue
            val w = weightValue
            if (h != null && w != null && h > 0) {
                val heightInMeters = h / 100 // Convert cm to meters
                w / (heightInMeters * heightInMeters)
            } else null
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "👤 Profile Form Sample",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Text(
            text = "Showcase of different data types",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        // Personal Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🆔 Personal Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // String Field (Name)
                OutlinedTextField(
                    value = nameValue ?: "",
                    onValueChange = { nameField.updateField(it) },
                    label = { Text("Name (String Field)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = nameValue != null && !nameIsValid,
                    supportingText = {
                        when {
                            nameValue != null && !nameIsValid && nameError.isNotEmpty() -> {
                                Text(nameError, color = MaterialTheme.colorScheme.error)
                            }
                            nameIsValid -> {
                                Text("✓ Valid name", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Int Field (Age)
                OutlinedTextField(
                    value = ageValue?.toString() ?: "",
                    onValueChange = { ageField.updateField(it.toIntOrNull()) },
                    label = { Text("Age (Int Field)") },
                    leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = ageValue != null && !ageIsValid,
                    supportingText = {
                        when {
                            ageValue != null && !ageIsValid && ageError.isNotEmpty() -> {
                                Text(ageError, color = MaterialTheme.colorScheme.error)
                            }
                            ageIsValid -> {
                                Text("✓ Age: $ageValue years", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Date Field (Birthdate)
                OutlinedTextField(
                    value = birthdateValue ?: "",
                    onValueChange = { birthdateField.updateField(it) },
                    label = { Text("Birthdate (Date Field - YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    placeholder = { Text("1990-01-01") },
                    isError = birthdateValue != null && !birthdateIsValid,
                    supportingText = {
                        when {
                            birthdateValue != null && !birthdateIsValid && birthdateError.isNotEmpty() -> {
                                Text(birthdateError, color = MaterialTheme.colorScheme.error)
                            }
                            birthdateIsValid -> {
                                Text("✓ Valid date format", color = MaterialTheme.colorScheme.primary)
                            }
                            else -> {
                                Text("Enter date in YYYY-MM-DD format", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Physical Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "⚖️ Physical Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Float Field (Height)
                    OutlinedTextField(
                        value = heightValue?.toString() ?: "",
                        onValueChange = { heightField.updateField(it.toFloatOrNull()) },
                        label = { Text("Height (cm)") },
                        leadingIcon = { Icon(Icons.Default.Height, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = heightValue != null && !heightIsValid,
                        supportingText = {
                            when {
                                heightValue != null && !heightIsValid && heightError.isNotEmpty() -> {
                                    Text(heightError, color = MaterialTheme.colorScheme.error)
                                }
                                heightIsValid -> {
                                    Text("✓ ${heightValue}cm", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Double Field (Weight)
                    OutlinedTextField(
                        value = weightValue?.toString() ?: "",
                        onValueChange = { weightField.updateField(it.toDoubleOrNull()) },
                        label = { Text("Weight (kg)") },
                        leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = weightValue != null && !weightIsValid,
                        supportingText = {
                            when {
                                weightValue != null && !weightIsValid && weightError.isNotEmpty() -> {
                                    Text(weightError, color = MaterialTheme.colorScheme.error)
                                }
                                weightIsValid -> {
                                    Text("✓ ${weightValue}kg", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // BMI Calculation Display
                if (bmi != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                bmi!! < 18.5 -> MaterialTheme.colorScheme.errorContainer
                                bmi!! < 25 -> MaterialTheme.colorScheme.primaryContainer
                                bmi!! < 30 -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.errorContainer
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("BMI (Calculated):")
                            Text(
                                text = String.format("%.1f", bmi),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
        
        // Professional Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "💼 Professional Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Quantity Field (Experience)
                OutlinedTextField(
                    value = experienceValue?.toString() ?: "",
                    onValueChange = { experienceField.updateField(it.toIntOrNull()) },
                    label = { Text("Years of Experience (Quantity Field)") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = experienceValue != null && !experienceIsValid,
                    supportingText = {
                        when {
                            experienceValue != null && !experienceIsValid && experienceError.isNotEmpty() -> {
                                Text(experienceError, color = MaterialTheme.colorScheme.error)
                            }
                            experienceIsValid -> {
                                Text("✓ ${experienceValue} years experience", color = MaterialTheme.colorScheme.primary)
                            }
                            else -> {
                                Text("Range: 0-50 years", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Long Field (Salary)
                OutlinedTextField(
                    value = salaryValue?.toString() ?: "",
                    onValueChange = { salaryField.updateField(it.toLongOrNull()) },
                    label = { Text("Annual Salary (Long Field - Optional)") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = salaryValue != null && !salaryIsValid,
                    supportingText = {
                        when {
                            salaryValue != null && !salaryIsValid && salaryError.isNotEmpty() -> {
                                Text(salaryError, color = MaterialTheme.colorScheme.error)
                            }
                            salaryValue != null && salaryIsValid -> {
                                Text("✓ Salary: ₹${salaryValue}", color = MaterialTheme.colorScheme.primary)
                            }
                            else -> {
                                Text("Optional - Leave blank if prefer not to say", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // List Field (Skills)
                Column {
                    Text(
                        text = "Skills (List Field)",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Skill chips
                    if (availableSkills.isNotEmpty()) {
                        Text(
                            text = "Select skills:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Skills grid using FlowRow-like layout
                        Column {
                            availableSkills.chunked(3).forEach { skillRow ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    skillRow.forEach { skill ->
                                        FilterChip(
                                            selected = selectedSkills.contains(skill),
                                            onClick = {
                                                selectedSkills = if (selectedSkills.contains(skill)) {
                                                    selectedSkills - skill
                                                } else {
                                                    selectedSkills + skill
                                                }
                                            },
                                            label = { Text(skill, fontSize = 12.sp) },
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                    }
                                    // Fill remaining space if row has fewer than 3 items
                                    repeat(3 - skillRow.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                    
                    // Custom skill input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customSkill,
                            onValueChange = { customSkill = it },
                            label = { Text("Add custom skill") },
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (customSkill.isNotBlank()) {
                                    selectedSkills = selectedSkills + customSkill
                                    customSkill = ""
                                }
                            },
                            enabled = customSkill.isNotBlank()
                        ) {
                            Text("Add")
                        }
                    }
                    
                    // Selected skills display
                    if (selectedSkills.isNotEmpty()) {
                        Text(
                            text = "Selected: ${selectedSkills.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                
                // Bio Field (Multiline String)
                OutlinedTextField(
                    value = bioValue ?: "",
                    onValueChange = { bioField.updateField(it) },
                    label = { Text("Bio (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    minLines = 3,
                    maxLines = 5,
                    supportingText = {
                        Text(
                            text = "${bioValue?.length ?: 0}/500 characters",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Submit Button
        Button(
            onClick = { 
                println("Profile Data:")
                println("Name: $nameValue (String)")
                println("Age: $ageValue (Int)")
                println("Height: $heightValue cm (Float)")
                println("Weight: $weightValue kg (Double)")
                println("Salary: $salaryValue (Long)")
                println("Birthdate: $birthdateValue (Date)")
                println("Experience: $experienceValue years (Quantity)")
                println("Skills: $selectedSkills (List)")
                println("Bio: $bioValue (String)")
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Profile")
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
                text = if (isFormValid) 
                    "✅ Profile is complete with all data types validated!" 
                else 
                    "📝 Please complete required fields to save profile",
                modifier = Modifier.padding(12.dp),
                color = if (isFormValid) 
                    MaterialTheme.colorScheme.onPrimaryContainer 
                else 
                    MaterialTheme.colorScheme.onErrorContainer
            )
        }
        
        // Data Types Info Card
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
                    text = "📊 Data Types Demonstrated",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val dataTypes = listOf(
                    "String → Name, Bio",
                    "Int → Age",
                    "Float → Height",
                    "Double → Weight",
                    "Long → Salary",
                    "Date → Birthdate",
                    "Quantity → Experience (with min/max)",
                    "List<String> → Skills"
                )
                dataTypes.forEach { type ->
                    Text(
                        text = "• $type",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
        }
    }
} 