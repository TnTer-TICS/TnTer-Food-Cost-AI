package com.example.ui.ingredients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Ingredient
import com.example.data.RestaurantRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreen() {
    val ingredients by RestaurantRepository.ingredients.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val filteredIngredients = ingredients.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Nguyên Vật Liệu", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = CyanAccent,
                contentColor = NavyPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm nguyên liệu")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tìm kiếm theo tên, mã hoặc nhóm NVL...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Text(
                text = "Tổng số: ${filteredIngredients.size} nguyên liệu",
                fontSize = 13.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredIngredients) { ing ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = NavyPrimary.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            text = ing.code,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NavyPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ing.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = NavyPrimary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CyanAccent.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = ing.category,
                                        fontSize = 11.sp,
                                        color = NavyPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Giá mua (${ing.purchaseUnit}):", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = currencyFormat.format(ing.purchasePrice), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Column {
                                    Text(text = "Hao hụt sơ chế:", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = "${ing.yieldPercentage}%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (ing.yieldPercentage < 80.0) Color.Red else Color.DarkGray)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Giá thực tế (${ing.usageUnit}):", fontSize = 12.sp, color = Color.Gray)
                                    Text(
                                        text = "${currencyFormat.format(ing.actualUnitCost)} / ${ing.usageUnit}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CyanAccent
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Nhà cung cấp: ${ing.supplier.ifEmpty { "Không rõ" }}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddIngredientDialog(
            onDismiss = { showAddDialog = false },
            onSave = { newIng ->
                RestaurantRepository.addIngredient(newIng)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onSave: (Ingredient) -> Unit
) {
    var code by remember { mutableStateOf("NVL021") }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Thịt & Thủy Hải Sản") }
    var purchaseUnit by remember { mutableStateOf("kg") }
    var usageUnit by remember { mutableStateOf("g") }
    var conversionFactor by remember { mutableStateOf("1000") }
    var purchasePrice by remember { mutableStateOf("") }
    var supplier by remember { mutableStateOf("") }
    var yieldPercentage by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Nguyên Vật Liệu Mới", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Mã NVL") }, singleLine = true)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên nguyên liệu") }, singleLine = true)
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Nhóm nguyên liệu") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = purchaseUnit, onValueChange = { purchaseUnit = it }, label = { Text("ĐVT mua") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = usageUnit, onValueChange = { usageUnit = it }, label = { Text("ĐVT dùng") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = purchasePrice, onValueChange = { purchasePrice = it }, label = { Text("Giá nhập (VND)") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = conversionFactor, onValueChange = { conversionFactor = it }, label = { Text("Hệ số quy đổi") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = yieldPercentage, onValueChange = { yieldPercentage = it }, label = { Text("Thu hồi (%)") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = supplier, onValueChange = { supplier = it }, label = { Text("Nhà cung cấp") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = purchasePrice.toDoubleOrNull() ?: 0.0
                    val factor = conversionFactor.toDoubleOrNull() ?: 1000.0
                    val yield = yieldPercentage.toDoubleOrNull() ?: 100.0
                    if (name.isNotBlank() && price > 0) {
                        onSave(
                            Ingredient(
                                id = "ing_${System.currentTimeMillis()}",
                                code = code,
                                name = name,
                                category = category,
                                purchaseUnit = purchaseUnit,
                                usageUnit = usageUnit,
                                conversionFactor = factor,
                                purchasePrice = price,
                                supplier = supplier,
                                yieldPercentage = yield,
                                updatedAt = "2026-11-15"
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Lưu nguyên liệu", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
