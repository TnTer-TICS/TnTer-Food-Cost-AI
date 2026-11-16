package com.example.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun DashboardScreen(
    onNavigate: (String) -> Unit
) {
    val dishes by RestaurantRepository.dishes.collectAsState()
    val sales by RestaurantRepository.salesRecords.collectAsState()
    val expenses by RestaurantRepository.expenses.collectAsState()

    val totalRevenue = sales.sumOf { it.totalRevenue }
    val totalCogs = sales.sumOf { it.totalCost }
    val foodCostPct = if (totalRevenue > 0) (totalCogs / totalRevenue) * 100.0 else 0.0
    val grossProfit = totalRevenue - totalCogs
    val totalExpenses = expenses.sumOf { it.amount }
    val netProfit = grossProfit - totalExpenses

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header Card with Demo Badge
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TnTer Food Cost AI [DEMO]",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Chi nhánh trung tâm • 15/11/2026",
                                color = CyanAccent,
                                fontSize = 13.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyanAccent
                        ) {
                            Text(
                                text = "Trực tuyến",
                                color = NavyPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Chỉ số Kinh doanh & Food Cost",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
        }

        // KPI Grid Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        title = "Doanh thu hôm nay",
                        value = currencyFormat.format(totalRevenue),
                        icon = Icons.Default.TrendingUp,
                        tint = SuccessGreen
                    )
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        title = "Food Cost %",
                        value = String.format(Locale.US, "%.1f%%", foodCostPct),
                        icon = Icons.Default.PieChart,
                        tint = if (foodCostPct > 32.0) ErrorRed else CyanAccent,
                        subtitle = if (foodCostPct > 32.0) "⚠️ Vượt mục tiêu (30%)" else "✓ Trong tầm kiểm soát"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        title = "Lợi nhuận gộp",
                        value = currencyFormat.format(grossProfit),
                        icon = Icons.Default.AccountBalanceWallet,
                        tint = SuccessGreen
                    )
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        title = "Lợi nhuận ròng ước tính",
                        value = currencyFormat.format(netProfit),
                        icon = Icons.Default.MonetizationOn,
                        tint = if (netProfit >= 0) SuccessGreen else ErrorRed
                    )
                }
            }
        }

        // Warning Alert Section for High Food Cost Dishes
        val highFoodCostDishes = dishes.filter { it.actualFoodCostPercentage > it.targetFoodCostPercentage }
        if (highFoodCostDishes.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Cảnh báo Food Cost vượt ngưỡng mục tiêu!",
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Có ${highFoodCostDishes.size} món ăn có Food Cost thực tế cao hơn mục tiêu đề ra. Hãy kiểm tra định lượng hoặc điều chỉnh giá bán.",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Món Bán Chạy & Hiệu suất",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                TextButton(onClick = { onNavigate("menu") }) {
                    Text(text = "Xem tất cả", color = CyanAccent)
                }
            }
        }

        items(dishes.take(4)) { dish ->
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
                        Text(
                            text = dish.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NavyPrimary
                        )
                        Text(
                            text = "Danh mục: ${dish.category} • Giá bán: ${currencyFormat.format(dish.sellingPrice)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = String.format(Locale.US, "Food Cost: %.1f%%", dish.actualFoodCostPercentage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (dish.actualFoodCostPercentage > dish.targetFoodCostPercentage) ErrorRed else SuccessGreen
                        )
                        Text(
                            text = "Lãi: ${currencyFormat.format(dish.grossMargin)}",
                            fontSize = 12.sp,
                            color = SuccessGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    subtitle: String? = null
) {
    Card(
        modifier = modifier,
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
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (subtitle.contains("⚠️")) ErrorRed else SuccessGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
