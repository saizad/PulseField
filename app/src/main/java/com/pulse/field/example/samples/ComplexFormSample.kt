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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

// Data class representing a product order
data class ProductOrder(
    val customerName: String,
    val customerEmail: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double,
    val deliveryAddress: String,
    val pincode: String,
    val phone: String,
    val specialInstructions: String?,
    val tags: List<String>
)

// DataForm implementation for ProductOrder
class ProductOrderForm(
    id: String = "productOrder",
    customerName: String? = null,
    customerEmail: String? = null,
    productName: String? = null,
    quantity: Int? = null,
    unitPrice: Double? = null,
    deliveryAddress: String? = null,
    pincode: String? = null,
    phone: String? = null,
    specialInstructions: String? = null,
    tags: List<String>? = null
) : DataForm<ProductOrder>(
    id,
    listOf(
        FullNameField("customerName", isMandatory = true, ogField = customerName),
        EmailField("customerEmail", isMandatory = true, ogField = customerEmail),
        StringField("productName", isMandatory = true, ogField = productName),
        QuantityField("quantity", isMandatory = true, minQuantity = 1, maxQuantity = 100, ogField = quantity),
        DoubleField("unitPrice", isMandatory = true, ogField = unitPrice),
        StringField("deliveryAddress", isMandatory = true, ogField = deliveryAddress),
        PincodeField("pincode", isMandatory = true, ogField = pincode),
        PhoneNumberField("phone", isMandatory = true, ogField = phone),
        StringField("specialInstructions", isMandatory = false, ogField = specialInstructions),
        ListField("tags", isMandatory = false, ogField = tags)
    )
) {
    
    // Get field references for easy access
    val customerName: FullNameField = requiredFindField("customerName")
    val customerEmail: EmailField = requiredFindField("customerEmail")
    val productName: StringField = requiredFindField("productName")
    val quantityField: QuantityField = requiredFindField("quantity")
    val unitPriceField: DoubleField = requiredFindField("unitPrice")
    val deliveryAddress: StringField = requiredFindField("deliveryAddress")
    val pincode: PincodeField = requiredFindField("pincode")
    val phone: PhoneNumberField = requiredFindField("phone")
    val specialInstructions: StringField = requiredFindField("specialInstructions")
    val tags: ListField<String> = requiredFindField("tags")
    
    // Computed field for total price
    val totalPrice: Flow<Double> = combine(
        quantityField.fieldFlow,
        unitPriceField.fieldFlow
    ) { qty, price ->
        (qty ?: 0) * (price ?: 0.0)
    }
    
    override fun build(): Flow<ProductOrder> {
        return combine(
            customerName.fieldFlow,
            customerEmail.fieldFlow,
            productName.fieldFlow,
            quantityField.fieldFlow,
            unitPriceField.fieldFlow,
            deliveryAddress.fieldFlow,
            pincode.fieldFlow,
            phone.fieldFlow,
            specialInstructions.fieldFlow,
            tags.fieldFlow,
            totalPrice
        ) { values ->
            ProductOrder(
                customerName = values[0] as String,
                customerEmail = values[1] as String,
                productName = values[2] as String,
                quantity = values[3] as Int,
                unitPrice = values[4] as Double,
                totalPrice = values[10] as Double,
                deliveryAddress = values[5] as String,
                pincode = values[6] as String,
                phone = values[7] as String,
                specialInstructions = values[8] as? String,
                tags = (values[9] as? List<String>) ?: emptyList()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexFormSample() {
    // Create the form instance
    val form = remember { ProductOrderForm() }
    
    // Collect form-level states
    val formValidationState by form.validationState.collectAsState(ValidationState.Loading)
    val isFormValid by form.isValid.collectAsState(false)
    val isFormModified by form.isModified.collectAsState(false)
    
    // Collect individual field values for display
    val customerNameValue by form.customerName.fieldFlow.collectAsState()
    val customerEmailValue by form.customerEmail.fieldFlow.collectAsState()
    val productNameValue by form.productName.fieldFlow.collectAsState()
    val quantityValue by form.quantityField.fieldFlow.collectAsState()
    val unitPriceValue by form.unitPriceField.fieldFlow.collectAsState()
    val totalPriceValue by form.totalPrice.collectAsState(0.0)
    val deliveryAddressValue by form.deliveryAddress.fieldFlow.collectAsState()
    val pincodeValue by form.pincode.fieldFlow.collectAsState()
    val phoneValue by form.phone.fieldFlow.collectAsState()
    val specialInstructionsValue by form.specialInstructions.fieldFlow.collectAsState()
    
    // Collect field validation states
    val customerNameError by form.customerName.error.collectAsState("")
    val customerEmailError by form.customerEmail.error.collectAsState("")
    val productNameError by form.productName.error.collectAsState("")
    val quantityError by form.quantityField.error.collectAsState("")
    val unitPriceError by form.unitPriceField.error.collectAsState("")
    val deliveryAddressError by form.deliveryAddress.error.collectAsState("")
    val pincodeError by form.pincode.error.collectAsState("")
    val phoneError by form.phone.error.collectAsState("")
    
    val customerNameValid by form.customerName.isValid.collectAsState(false)
    val customerEmailValid by form.customerEmail.isValid.collectAsState(false)
    val productNameValid by form.productName.isValid.collectAsState(false)
    val quantityValid by form.quantityField.isValid.collectAsState(false)
    val unitPriceValid by form.unitPriceField.isValid.collectAsState(false)
    val deliveryAddressValid by form.deliveryAddress.isValid.collectAsState(false)
    val pincodeValid by form.pincode.isValid.collectAsState(false)
    val phoneValid by form.phone.isValid.collectAsState(false)
    
    // Available product tags
    val availableTags = listOf("Electronics", "Books", "Clothing", "Home", "Sports", "Beauty")
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    
    // Update tags in the form
    LaunchedEffect(selectedTags) {
        form.tags.updateField(selectedTags.toList())
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "📦 Complex Form with DataForm",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Text(
            text = "Product Order Management",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        // Customer Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "👤 Customer Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = customerNameValue ?: "",
                    onValueChange = { form.customerName.updateField(it) },
                    label = { Text("Customer Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = customerNameValue != null && !customerNameValid,
                    supportingText = {
                        if (customerNameValue != null && !customerNameValid && customerNameError.isNotEmpty()) {
                            Text(customerNameError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = customerEmailValue ?: "",
                    onValueChange = { form.customerEmail.updateField(it) },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = customerEmailValue != null && !customerEmailValid,
                    supportingText = {
                        if (customerEmailValue != null && !customerEmailValid && customerEmailError.isNotEmpty()) {
                            Text(customerEmailError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = phoneValue ?: "",
                    onValueChange = { form.phone.updateField(it) },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneValue != null && !phoneValid,
                    supportingText = {
                        if (phoneValue != null && !phoneValid && phoneError.isNotEmpty()) {
                            Text(phoneError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Product Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "📱 Product Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = productNameValue ?: "",
                    onValueChange = { form.productName.updateField(it) },
                    label = { Text("Product Name") },
                    leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    isError = productNameValue != null && !productNameValid,
                    supportingText = {
                        if (productNameValue != null && !productNameValid && productNameError.isNotEmpty()) {
                            Text(productNameError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = quantityValue?.toString() ?: "",
                        onValueChange = { form.quantityField.updateField(it.toIntOrNull()) },
                        label = { Text("Quantity") },
                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = quantityValue != null && !quantityValid,
                        supportingText = {
                            if (quantityValue != null && !quantityValid && quantityError.isNotEmpty()) {
                                Text(quantityError, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = unitPriceValue?.toString() ?: "",
                        onValueChange = { form.unitPriceField.updateField(it.toDoubleOrNull()) },
                        label = { Text("Unit Price (₹)") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = unitPriceValue != null && !unitPriceValid,
                        supportingText = {
                            if (unitPriceValue != null && !unitPriceValid && unitPriceError.isNotEmpty()) {
                                Text(unitPriceError, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Total Price Display
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Price:")
                        Text(
                            text = "₹${String.format("%.2f", totalPriceValue)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                
                // Product Tags
                Column {
                    Text(
                        text = "Product Tags",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableTags.size) { index ->
                            val tag = availableTags[index]
                            FilterChip(
                                selected = selectedTags.contains(tag),
                                onClick = {
                                    selectedTags = if (selectedTags.contains(tag)) {
                                        selectedTags - tag
                                    } else {
                                        selectedTags + tag
                                    }
                                },
                                label = { Text(tag) }
                            )
                        }
                    }
                }
            }
        }
        
        // Delivery Information Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🚚 Delivery Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = deliveryAddressValue ?: "",
                    onValueChange = { form.deliveryAddress.updateField(it) },
                    label = { Text("Delivery Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    minLines = 2,
                    maxLines = 3,
                    isError = deliveryAddressValue != null && !deliveryAddressValid,
                    supportingText = {
                        if (deliveryAddressValue != null && !deliveryAddressValid && deliveryAddressError.isNotEmpty()) {
                            Text(deliveryAddressError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = pincodeValue ?: "",
                    onValueChange = { form.pincode.updateField(it) },
                    label = { Text("Pincode") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = pincodeValue != null && !pincodeValid,
                    supportingText = {
                        if (pincodeValue != null && !pincodeValid && pincodeError.isNotEmpty()) {
                            Text(pincodeError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = specialInstructionsValue ?: "",
                    onValueChange = { form.specialInstructions.updateField(it) },
                    label = { Text("Special Instructions (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { form.reset() },
                enabled = isFormModified,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset")
            }
            
            Button(
                onClick = { 
                    // Build and submit the form
                    form.build().also { flow ->
                        // In a real app, you'd collect this flow and handle the result
                        println("Form submitted successfully!")
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.weight(2f)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Place Order")
            }
        }
        
        // Form Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isFormValid -> MaterialTheme.colorScheme.primaryContainer
                    formValidationState is ValidationState.Error -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = when {
                        isFormValid -> "✅ Order form is complete and ready to submit"
                        formValidationState is ValidationState.Error -> "❌ Please fix validation errors"
                        else -> "📝 Fill out the form to place your order"
                    },
                    fontWeight = FontWeight.Medium
                )
                
                if (isFormModified) {
                    Text(
                        text = "⚠️ Form has unsaved changes",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}

@Composable
private fun items(size: Int, itemContent: @Composable (Int) -> Unit) {
    repeat(size) { index ->
        itemContent(index)
    }
} 