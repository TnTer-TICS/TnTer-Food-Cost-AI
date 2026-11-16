package com.example.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RestaurantRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val dishes by RestaurantRepository.dishes.collectAsState()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Thực đơn & Menu Engineering", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
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
            Text(text = "Danh sách món ăn kinh doanh & Biên lợi nhuận", fontSize = 14.sp, color = Color.Gray)

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
                                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = CyanAccent)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = dish.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (dish.status == "Đang kinh doanh") SuccessGreen.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = dish.status,
                                        fontSize = 11.sp,
                                        color = if (dish.status == "Đang kinh doanh") SuccessGreen else Color.DarkGray,
                                        fontWeight = FontWeight.Bold,
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
                                    Text(text = "Giá vốn:", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = currencyFormat.format(dish.totalCostPerPortion), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Column {
                                    Text(text = "Giá bán:", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = currencyFormat.format(dish.sellingPrice), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CyanAccent)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Food Cost %:", fontSize = 12.sp, color = Color.Gray)
                                    Text(
                                        text = String.format(Locale.US, "%.1f%%", dish.actualFoodCostPercentage),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (dish.actualFoodCostPercentage > dish.targetFoodCostPercentage) ErrorRed else SuccessGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Lãi gộp: ${currencyFormat.format(dish.grossMargin)}", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                                Text(text = "Biên lãi gộp: ${String.format(Locale.US, "%.1f%%", dish.grossMarginPercentage)}", fontSize = 12.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
