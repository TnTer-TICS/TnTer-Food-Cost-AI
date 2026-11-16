package com.example.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen() {
    val transactions by RestaurantRepository.inventoryTransactions.collectAsState()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Kho hàng & Giao dịch", color = Color.White, fontWeight = FontWeight.Bold) },
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
            Text(text = "Lịch sử Nhập xuất kho & Hao hụt", fontSize = 14.sp, color = Color.Gray)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(transactions) { tx ->
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
                                    Icon(imageVector = Icons.Default.Inventory, contentDescription = null, tint = CyanAccent)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = tx.ingredientName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NavyPrimary)
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (tx.type == "NHẬP KHO") CyanAccent.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = tx.type,
                                        fontSize = 11.sp,
                                        color = if (tx.type == "NHẬP KHO") NavyPrimary else Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Số lượng: ${tx.quantity} ${tx.unit}", fontSize = 12.sp, color = Color.DarkGray)
                                Text(text = "Tổng tiền: ${currencyFormat.format(tx.totalAmount)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Lý do: ${tx.reason}", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "Thời gian: ${tx.timestamp} • Người thực hiện: ${tx.performedBy}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
