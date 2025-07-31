# PulseField Sample Applications

This document provides a comprehensive overview of all sample applications created to demonstrate the PulseField library capabilities.

## 🚀 Overview

The PulseField sample app showcases **6 comprehensive samples** demonstrating different aspects of the reactive form validation library:

1. **Login Form Sample** - Basic validation with email and password
2. **Registration Form Sample** - Multi-field complex form validation
3. **Indian User Registration** - Localized validation for Indian users
4. **Profile Form Sample** - Different data types showcase
5. **Complex DataForm Sample** - Advanced form composition
6. **OTP Verification Sample** - Two-step phone verification

## 📱 Sample Details

### 1. Login Form Sample
**File:** `LoginFormSample.kt`

**Features Demonstrated:**
- EmailField with real-time email validation
- PasswordField with visibility toggle
- Loading states during validation
- Form submission state management
- Visual feedback with error/success states

**Key Learning Points:**
- Basic field creation with `remember`
- Collecting field states with `collectAsState()`
- Reactive validation with immediate feedback
- Form-level validation combining multiple fields

```kotlin
val emailField = remember { EmailField("email", isMandatory = true) }
val passwordField = remember { PasswordField("password", isMandatory = true) }
```

### 2. Registration Form Sample
**File:** `RegistrationFormSample.kt`

**Features Demonstrated:**
- Multiple field types in one form
- ConfirmPasswordField with password matching
- Gender selection with RadioButtons
- Phone number validation
- Age validation with IntField
- Complex form state management

**Key Learning Points:**
- Handling multiple field types
- Password confirmation validation
- Custom UI components for non-text fields
- Form scrolling for long forms

```kotlin
val confirmPasswordField = remember { 
    ConfirmPasswordField("confirmPassword", isMandatory = true, passwordField = passwordField) 
}
```

### 3. Indian User Registration Sample
**File:** `IndianUserRegistrationSample.kt`

**Features Demonstrated:**
- AadhaarField with 12-digit validation
- PhoneNumberField for Indian 10-digit numbers
- PincodeField for 6-digit Indian postal codes
- Localized UI with Indian context
- Optional email field handling

**Key Learning Points:**
- India-specific validation patterns
- Localized form design
- Mixing mandatory and optional fields
- Cultural context in form design

```kotlin
val aadhaarField = remember { AadhaarField("aadhaar", isMandatory = true) }
val pincodeField = remember { PincodeField("pincode", isMandatory = true) }
```

### 4. Profile Form Sample
**File:** `ProfileFormSample.kt`

**Features Demonstrated:**
- All data types: String, Int, Float, Double, Long, Date, List
- QuantityField with min/max constraints
- ListField with dynamic selection
- Computed values (BMI calculation)
- Custom skill management with tags

**Key Learning Points:**
- Working with non-string field types
- UI conversion between strings and typed values
- Dynamic list management
- Computed fields from multiple inputs

```kotlin
val heightField = remember { FloatField("height", isMandatory = true) }
val experienceField = remember { QuantityField("experience", isMandatory = true, min = 0, max = 50) }
```

### 5. Complex DataForm Sample
**File:** `ComplexFormSample.kt`

**Features Demonstrated:**
- DataForm implementation for structured data
- ProductOrder data class with complex relationships
- Form composition with multiple sections
- Computed fields (total price calculation)
- Form building to structured objects

**Key Learning Points:**
- Creating custom DataForm implementations
- Building complex data objects from forms
- Form-level validation and state management
- Sectioned form design

```kotlin
class ProductOrderForm : DataForm<ProductOrder>(
    id,
    listOf(
        FullNameField("customerName", isMandatory = true),
        EmailField("customerEmail", isMandatory = true),
        // ... more fields
    )
)
```

### 6. OTP Verification Sample
**File:** `OTPValidationSample.kt`

**Features Demonstrated:**
- Two-step verification flow
- OTPField validation
- Countdown timer implementation
- State management for multi-step forms
- Loading states and user feedback

**Key Learning Points:**
- Multi-step form workflows
- Time-based state management
- OTP field validation patterns
- User experience for verification flows

```kotlin
val otpField = remember { OTPField("otp", isMandatory = true) }
```

## 🎨 UI/UX Features

### Material Design 3 Implementation
- **Cards and Elevation** - Organized content in visually appealing cards
- **Color Schemes** - Proper use of Material Design color tokens
- **Typography** - Consistent text hierarchy and styling
- **Icons** - Meaningful icons from Material Icons
- **State Feedback** - Visual feedback for different validation states

### Interactive Elements
- **Real-time Validation** - Immediate feedback as user types
- **Loading States** - Progress indicators during operations
- **Error Handling** - Clear error messages with context
- **Success Feedback** - Positive reinforcement for valid inputs
- **Form Navigation** - Smooth transitions between form steps

### Responsive Design
- **Scrollable Forms** - Handle long forms with proper scrolling
- **Flexible Layouts** - Responsive to different screen sizes
- **Touch Targets** - Properly sized touch areas
- **Keyboard Support** - Appropriate keyboard types for different fields

## 🔧 Technical Implementation

### State Management
```kotlin
// Field state collection
val emailValue by emailField.fieldFlow.collectAsState()
val emailIsValid by emailField.isValid.collectAsState(false)
val emailError by emailField.error.collectAsState(initial = "")
```

### Form Validation
```kotlin
// Form-level validation
val isFormValid = emailIsValid && passwordIsValid && nameIsValid
```

### Error Handling
```kotlin
// Error display in UI
supportingText = {
    when {
        value != null && !isValid && error.isNotEmpty() -> {
            Text(error, color = MaterialTheme.colorScheme.error)
        }
        isValid -> {
            Text("✓ Valid input", color = MaterialTheme.colorScheme.primary)
        }
    }
}
```

## 📊 Field Types Demonstrated

| Field Type | Sample Usage | Validation Features |
|------------|--------------|-------------------|
| StringField | Name, Address, Bio | Length, character validation |
| EmailField | User email | Format validation |
| PasswordField | User password | Strength validation |
| ConfirmPasswordField | Password confirmation | Matching validation |
| IntField | Age | Numeric validation |
| FloatField | Height | Decimal validation |
| DoubleField | Weight | High precision validation |
| LongField | Salary | Large number validation |
| DateField | Birthdate | Date format validation |
| ListField | Skills, Tags | Collection validation |
| QuantityField | Experience | Min/max constraints |
| AadhaarField | Indian ID | 12-digit + format validation |
| PhoneNumberField | Mobile | 10-digit validation |
| PincodeField | Postal code | 6-digit validation |
| OTPField | Verification code | 6-digit OTP validation |
| Gender | Gender selection | Enum validation |

## 🚀 Getting Started

1. **Clone the repository**
2. **Open in Android Studio**
3. **Run the app**
4. **Navigate through samples** using the main menu
5. **Experiment with validation** by entering different values

## 🎯 Key Takeaways

### For Developers
- **Type Safety** - Compile-time safety with typed fields
- **Reactive Validation** - Real-time feedback using Flows
- **Composable Architecture** - Modular and reusable components
- **Easy Integration** - Simple API with minimal boilerplate

### For Users
- **Immediate Feedback** - Know instantly if input is valid
- **Clear Error Messages** - Understand what needs to be fixed
- **Intuitive Design** - Natural flow through form completion
- **Accessible Interface** - Proper labels and descriptions

## 📚 Further Learning

Each sample builds upon previous concepts:

1. **Start with Login** - Learn basic field validation
2. **Move to Registration** - Understand multi-field forms
3. **Try Indian Registration** - See localized validation
4. **Explore Profile** - Master different data types
5. **Build Complex Forms** - Learn DataForm composition
6. **Implement OTP** - Handle multi-step workflows

## 🤝 Contributing

To add new samples:

1. Create a new file in `samples/` directory
2. Follow the existing naming convention
3. Add to the `SampleType` enum in `MainActivity.kt`
4. Update this documentation
5. Submit a pull request

---

**Happy Coding with PulseField!** 🚀 