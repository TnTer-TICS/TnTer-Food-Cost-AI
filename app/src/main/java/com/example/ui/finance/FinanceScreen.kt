package com.example.ui.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
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
fun FinanceScreen() {
    val sales by RestaurantRepository.salesRecords.collectAsState()
    val expenses by RestaurantRepository.expenses.collectAsState()

    val totalRevenue = sales.sumOf { it.totalRevenue }
    val totalCogs = sales.sumOf { it.totalCost }
    val grossProfit = totalRevenue - totalCogs
    val totalExpenses = expenses.sumOf { it.amount }
    val netProfit = grossProfit - totalExpenses

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Doanh thu & Chi phí", color = Color.White, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Báo cáo P&L (Lãi / Lỗ) Ngày 15/11/2026", color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    FinanceRow(label = "Tổng Doanh thu thuần:", value = currencyFormat.format(totalRevenue))
                    FinanceRow(label = "Giá vốn hàng bán (COGS):", value = currencyFormat.format(totalCogs))
                    FinanceRow(label = "Lợi nhuận gộp:", value = currencyFormat.format(grossProfit))
                    FinanceRow(label = "Tổng chi phí hoạt động:", value = currencyFormat.format(totalExpenses))

                    Divider(color = Color.White.copy(alpha = 0.2f))

                    FinanceRow(label = "Lợi nhuận ròng ước tính:", value = currencyFormat.format(netProfit), isHighlight = true)
                }
            }

            Text(text = "Chi tiết Chi phí hoạt động (OPEX)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(expenses) { exp ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = exp.category, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NavyPrimary)
                                Text(text = exp.description, fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(text = currencyFormat.format(exp.amount), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ErrorRed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FinanceRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = if (isHighlight) Color.White else Color.White.copy(alpha = 0.8f), fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
        Text(text = value, color = if (isHighlight) CyanAccent else Color.White, fontWeight = FontWeight.Bold, fontSize = if (isHighlight) 16.sp else 14.sp)
    }
}
