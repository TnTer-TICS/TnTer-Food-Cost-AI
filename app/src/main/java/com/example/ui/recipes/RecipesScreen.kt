package com.example.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Dish
import com.example.data.RecipeItem
import com.example.data.RestaurantRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen() {
    val dishes by RestaurantRepository.dishes.collectAsState()
    val ingredients by RestaurantRepository.ingredients.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Công thức & Định lượng Món ăn", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = CyanAccent,
                contentColor = NavyPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm món mới")
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
            Text(
                text = "Quản lý định lượng chuẩn (Standard Recipe & Yield)",
                fontSize = 14.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dishes) { dish ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
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
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = dish.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = NavyPrimary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = NavyPrimary.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = dish.category,
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

                            Text(text = "Thành phần nguyên liệu:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = NavyPrimary)
                            Spacer(modifier = Modifier.height(4.dp))

                            dish.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "• ${item.ingredientName}", fontSize = 12.sp, color = Color.DarkGray)
                                    Text(text = "${item.quantity} ${item.unit} (${currencyFormat.format(item.cost)})", fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
                                    Text(text = "Giá vốn một phần:", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = currencyFormat.format(dish.totalCostPerPortion), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ErrorRed)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Giá bán niêm yết:", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = currencyFormat.format(dish.sellingPrice), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CyanAccent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddRecipeDialog(
            ingredients = ingredients,
            onDismiss = { showAddDialog = false },
            onSave = { newDish ->
                RestaurantRepository.addDish(newDish)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeDialog(
    ingredients: List<com.example.data.Ingredient>,
    onDismiss: () -> Unit,
    onSave: (Dish) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Món chính") }
    var sellingPrice by remember { mutableStateOf("") }
    var laborCost by remember { mutableStateOf("5000") }
    var packagingCost by remember { mutableStateOf("2000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo Món Ăn & Công thức Mới", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên món ăn") }, singleLine = true)
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Nhóm món") }, singleLine = true)
                OutlinedTextField(value = sellingPrice, onValueChange = { sellingPrice = it }, label = { Text("Giá bán thuần (VND)") }, singleLine = true)
                OutlinedTextField(value = laborCost, onValueChange = { laborCost = it }, label = { Text("Nhân công trực tiếp (VND)") }, singleLine = true)
                OutlinedTextField(value = packagingCost, onValueChange = { packagingCost = it }, label = { Text("Chi phí bao bì (VND)") }, singleLine = true)
                Text(text = "Hệ thống tự động sử dụng danh sách nguyên liệu chuẩn cho mẫu demo.", fontSize = 11.sp, color = Color.Gray)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = sellingPrice.toDoubleOrNull() ?: 0.0
                    val labor = laborCost.toDoubleOrNull() ?: 0.0
                    val packing = packagingCost.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && price > 0 && ingredients.isNotEmpty()) {
                        val sampleItems = listOf(
                            RecipeItem(ingredients[0].id, ingredients[0].name, 100.0, ingredients[0].usageUnit, ingredients[0].actualUnitCost),
                            RecipeItem(ingredients[4].id, ingredients[4].name, 150.0, ingredients[4].usageUnit, ingredients[4].actualUnitCost)
                        )
                        onSave(
                            Dish(
                                id = "dish_${System.currentTimeMillis()}",
                                code = "MON0${System.currentTimeMillis() % 100}",
                                name = name,
                                category = category,
                                servings = 1,
                                items = sampleItems,
                                directLaborCost = labor,
                                packagingCost = packing,
                                targetFoodCostPercentage = 30.0,
                                sellingPrice = price,
                                status = "Đang kinh doanh"
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Lưu công thức", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
