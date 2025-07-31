# PulseField 🚀

[![](https://jitpack.io/v/saizad/PulseField.svg)](https://jitpack.io/#saizad/PulseField)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A powerful, reactive form field validation library for Android/Kotlin that provides real-time validation using Kotlin Flows. Perfect for building robust forms with type-safe validation and seamless user experience.

## ✨ Features

- 🔄 **Reactive Validation** - Real-time validation using Kotlin Flows
- 🛡️ **Type Safety** - Strongly typed fields with compile-time safety
- 🧩 **Modular Design** - Compose complex forms from simple field components
- 📱 **Android Ready** - Optimized for Android development with Jetpack Compose
- 🌍 **Localization Support** - Built-in support for Indian validation patterns (Aadhaar, Phone, Pincode)
- 🎯 **Easy Integration** - Simple API with minimal boilerplate
- ✅ **Comprehensive Validation** - 20+ pre-built field types with validation rules
- 🔧 **Extensible** - Easy to create custom fields and validation rules

## 📦 Installation

Add JitPack repository to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.saizad:PulseField:latest-version'
}
```

## 🏗️ Quick Start

### Basic Field Usage

```kotlin
// Create an email field
val emailField = EmailField(
    fieldId = "email", 
    isMandatory = true, 
    ogField = "user@example.com"
)

// Update field value
emailField.updateField("newemail@example.com")

// Observe validation state
emailField.validationState.collect { state ->
    when (state) {
        is ValidationState.Success -> println("Valid email")
        is ValidationState.Error -> println("Error: ${state.errors.errorMessage}")
        is ValidationState.Loading -> println("Validating...")
    }
}

// Check if field is valid
emailField.isValid.collect { isValid ->
    println("Email is valid: $isValid")
}
```

## 📋 Available Field Types

PulseField comes with 20+ pre-built field types with comprehensive validation:

### 👤 Identity & Personal Information
```kotlin
// Aadhaar number validation (12 digits, format checking)
AadhaarField("aadhaar", isMandatory = true)

// Full name with first + last name requirement
FullNameField("fullName", isMandatory = true)

// Single name validation
NameField("firstName", isMandatory = true)

// Gender selection
Gender("gender", isMandatory = true)
```

### 📞 Contact Information
```kotlin
// Email format validation
EmailField("email", isMandatory = true)

// Indian phone number validation (10 digits)
PhoneNumberField("phone", isMandatory = true)

// Indian pincode validation
PincodeField("pincode", isMandatory = true)
```

### 🔐 Security & Authentication
```kotlin
// Password validation
PasswordField("password", isMandatory = true)

// Password confirmation with matching validation
ConfirmPasswordField("confirmPassword", isMandatory = true)

// OTP format validation
OTPField("otp", isMandatory = true)
```

### 📊 Data Types
```kotlin
// String field
StringField("title", isMandatory = true)

// Numeric fields
IntField("age", isMandatory = true)
LongField("timestamp", isMandatory = true)
FloatField("price", isMandatory = true)
DoubleField("amount", isMandatory = true)

// Date field
DateField("birthDate", isMandatory = true)

// List field for multiple selections
ListField<String>("tags", isMandatory = false)

// Quantity with min/max validation
QuantityField("quantity", isMandatory = true, min = 1, max = 100)
```

## 🎯 Creating Complex Forms

Use `DataForm` to combine multiple fields into a cohesive form with aggregate validation:

```kotlin
class CreateTestBody(
    val testName: String,
    val totalQuestion: Int,
    val duration: Int,
    val testLevel: Int,
    val subjects: List<Int>,
    val scheduleTime: String
) {
    
    class Form(
        id: String = "createTest",
        testName: String? = null,
        totalQuestion: Int? = null,
        duration: Long? = null,
        testLevel: Int? = null,
        subjects: List<Int>? = null,
        scheduleTime: String? = null
    ) : DataForm<CreateTestBody>(
        id,
        listOf(
            StringField("testName", isMandatory = true, ogField = testName),
            IntField("totalQuestion", isMandatory = true, ogField = totalQuestion),
            LongField("duration", isMandatory = true, ogField = duration),
            FormField("testLevel", isMandatory = true, ogField = testLevel),
            ListField("subjects", isMandatory = false, ogField = subjects),
            DateField("scheduleTime", isMandatory = true, ogField = scheduleTime)
        )
    ) {
        
        // Get field references for easy access
        val testName = requiredFindField<StringField>("testName")
        val totalQuestion = requiredFindField<IntField>("totalQuestion")
        val duration = requiredFindField<LongField>("duration")
        val subjects: ListField<Int> = requiredFindField("subjects")
        val scheduleTime: DateField = requiredFindField("scheduleTime")
        
        override fun build(): Flow<CreateTestBody> {
            return combine(
                testName.fieldFlow,
                totalQuestion.fieldFlow,
                duration.fieldFlow,
                testLevel.fieldFlow,
                subjects.fieldFlow,
                scheduleTime.fieldFlow
            ) { values ->
                CreateTestBody(
                    testName = values[0] as String,
                    totalQuestion = values[1] as Int,
                    duration = (values[2] as Long / 1000).toInt(),
                    testLevel = values[3] as Int,
                    subjects = values[4] as? List<Int> ?: emptyList(),
                    scheduleTime = values[5] as String
                )
            }
        }
    }
}
```

### Using the Form

```kotlin
// Create form instance
val form = CreateTestBody.Form()

// Update individual fields
form.testName.updateField("My Test")
form.totalQuestion.updateField(50)

// Observe form validation state
form.validationState.collect { state ->
    when (state) {
        is ValidationState.Success -> {
            // Form is valid, enable submit button
            submitButton.isEnabled = true
        }
        is ValidationState.Error -> {
            // Show validation errors
            showErrors(state.errors.errorMessage)
        }
    }
}

// Build the final object when form is valid
form.build().collect { testBody ->
    // Submit the valid form data
    submitTest(testBody)
}

// Check if form has been modified
form.isModified.collect { isModified ->
    // Show unsaved changes indicator
}
```

## 🛠️ Creating Custom Fields

Create your own field types by extending `FormField` or existing field types:

### Custom Validation Field

```kotlin
class CustomUrlField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: String? = null
) : StringField(fieldId, isMandatory, ogField) {
    
    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest { parentErrors ->
                flow {
                    val errors = parentErrors.toMutableList()
                    
                    // Custom URL validation logic
                    if (value != null && !isValidUrl(value)) {
                        errors.add(FieldValidationException(ValidationError.INVALID_URL))
                    }
                    
                    emit(errors)
                }
            }
    }
    
    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }
}
```

### Custom Complex Field

```kotlin
class CreditCardField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: String? = null
) : FormField<String>(fieldId, isMandatory, ogField) {
    
    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return flow {
            val errors = mutableListOf<FieldValidationException>()
            
            if (value == null && isMandatory) {
                errors.add(FieldValidationException(ValidationError.MANDATORY_FIELD))
            } else if (value != null) {
                // Luhn algorithm validation
                if (!isValidCreditCard(value)) {
                    errors.add(FieldValidationException(ValidationError.INVALID_CREDIT_CARD))
                }
            }
            
            emit(errors)
        }
    }
    
    private fun isValidCreditCard(number: String): Boolean {
        val cleanNumber = number.replace("\\s".toRegex(), "")
        // Implement Luhn algorithm
        return luhnCheck(cleanNumber)
    }
}
```

## 🎨 Integration with Jetpack Compose

PulseField works seamlessly with Jetpack Compose using the built-in reactive properties:

```kotlin
@Composable
fun LoginForm() {
    val emailField = remember { EmailField("email", isMandatory = true) }
    val passwordField = remember { PasswordField("password", isMandatory = true) }
    
    // Collect field states
    val emailValue by emailField.fieldFlow.collectAsState()
    val emailIsValid by emailField.isValid.collectAsState(false)
    val emailError by emailField.error.collectAsState(initial = null)
    
    val passwordValue by passwordField.fieldFlow.collectAsState()
    val passwordIsValid by passwordField.isValid.collectAsState(false)
    val passwordError by passwordField.error.collectAsState(initial = null)
    
    Column {
        // Email field
        OutlinedTextField(
            value = emailValue ?: "",
            onValueChange = { emailField.updateField(it) },
            label = { Text("Email") },
            isError = !emailIsValid && emailValue != null,
            supportingText = emailError?.let { { Text(it) } }
        )
        
        // Password field
        OutlinedTextField(
            value = passwordValue ?: "",
            onValueChange = { passwordField.updateField(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = !passwordIsValid && passwordValue != null,
            supportingText = passwordError?.let { { Text(it) } }
        )
    }
}
```

### Working with Non-String Fields

For numeric and other field types, handle the conversion in your UI:

```kotlin
@Composable
fun UserProfileForm() {
    val ageField = remember { IntField("age", isMandatory = true) }
    val salaryField = remember { DoubleField("salary", isMandatory = false) }
    
    val ageValue by ageField.fieldFlow.collectAsState()
    val ageIsValid by ageField.isValid.collectAsState(false)
    val ageError by ageField.error.collectAsState(initial = null)
    
    val salaryValue by salaryField.fieldFlow.collectAsState()
    val salaryIsValid by salaryField.isValid.collectAsState(false)
    val salaryError by salaryField.error.collectAsState(initial = null)
    
    Column {
        // Integer field
        OutlinedTextField(
            value = ageValue?.toString() ?: "",
            onValueChange = { ageField.updateField(it.toIntOrNull()) },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !ageIsValid && ageValue != null,
            supportingText = ageError?.let { { Text(it) } }
        )
        
        // Double field
        OutlinedTextField(
            value = salaryValue?.let { "%.2f".format(it) } ?: "",
            onValueChange = { salaryField.updateField(it.toDoubleOrNull()) },
            label = { Text("Salary") },
            leadingIcon = { Text("$") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = !salaryIsValid && salaryValue != null,
            supportingText = salaryError?.let { { Text(it) } }
        )
    }
}
```

## 🔧 Advanced Features

### Field State Management

```kotlin
// Reset field to original value
field.reset()

// Clear field value
field.clear()

// Check if field has been modified
field.isModified.collect { isModified ->
    // Handle modification state
}
```

### Form Utilities

```kotlin
// Get all field values as a map
form.fieldsFlow().collect { fieldMap ->
    // fieldMap contains field_id -> current_value mappings
}

// Update and build form in one operation
form.updateBuild().collect { builtObject ->
    // Get the built object after updating internal state
}
```

## 📖 Error Handling

PulseField provides comprehensive error types through `ValidationError` enum:

```kotlin
enum class ValidationError {
    INVALID_EMAIL,
    MANDATORY_FIELD,
    INVALID_PHONE_NUMBER_FORMAT,
    INVALID_AADHAR_NUMBER,
    PASSWORD_NO_MATCH,
    // ... and many more
}
```

## 🧪 Testing

The library includes comprehensive test coverage. Run tests with:

```bash
./gradlew test
```

Example test:

```kotlin
@Test
fun `email field validation works correctly`() = runTest {
    val emailField = EmailField("email", isMandatory = true)
    
    // Test invalid email
    emailField.updateField("invalid-email")
    val invalidState = emailField.validationState.first()
    assertTrue(invalidState is ValidationState.Error)
    
    // Test valid email
    emailField.updateField("test@example.com")
    val validState = emailField.validationState.first()
    assertTrue(validState is ValidationState.Success)
}
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with ❤️ for the Android community
- Inspired by the need for better form validation in Android apps
- Special focus on Indian market validation requirements

---

**Happy Coding!** 🚀

If you find this library helpful, please ⭐ star the repository and share it with others! 