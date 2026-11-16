package com.example.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    var rawCostInput by remember { mutableStateOf("22500") }
    var targetFoodCostInput by remember { mutableStateOf("30") }
    var laborCostInput by remember { mutableStateOf("5000") }
    var packagingCostInput by remember { mutableStateOf("2000") }
    var actualSellingPriceInput by remember { mutableStateOf("75000") }

    val rawCost = rawCostInput.toDoubleOrNull() ?: 0.0
    val targetFoodCostPct = targetFoodCostInput.toDoubleOrNull() ?: 30.0
    val laborCost = laborCostInput.toDoubleOrNull() ?: 0.0
    val packagingCost = packagingCostInput.toDoubleOrNull() ?: 0.0
    val actualSellingPrice = actualSellingPriceInput.toDoubleOrNull() ?: 0.0

    val totalDirectCost = rawCost + laborCost + packagingCost
    val suggestedPrice = if (targetFoodCostPct > 0) totalDirectCost / (targetFoodCostPct / 100.0) else 0.0
    val actualFoodCostPct = if (actualSellingPrice > 0) (totalDirectCost / actualSellingPrice) * 100.0 else 0.0
    val grossMargin = actualSellingPrice - totalDirectCost
    val grossMarginPct = if (actualSellingPrice > 0) (grossMargin / actualSellingPrice) * 100.0 else 0.0

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Cost Calculator & Định Giá", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = CyanAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Công cụ mô phỏng giá bán & Food Cost", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)
                    }

                    OutlinedTextField(
                        value = rawCostInput,
                        onValueChange = { rawCostInput = it },
                        label = { Text("Tổng giá vốn nguyên liệu một phần (VND)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = laborCostInput,
                            onValueChange = { laborCostInput = it },
                            label = { Text("Nhân công trực tiếp") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = packagingCostInput,
                            onValueChange = { packagingCostInput = it },
                            label = { Text("Bao bì / Takeaway") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = targetFoodCostInput,
                        onValueChange = { targetFoodCostInput = it },
                        label = { Text("Tỷ lệ Food Cost mục tiêu (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = actualSellingPriceInput,
                        onValueChange = { actualSellingPriceInput = it },
                        label = { Text("Giá bán dự kiến thực tế (VND)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Results Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Kết quả phân tích tài chính", color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    ResultRow(label = "Tổng chi phí trực tiếp:", value = currencyFormat.format(totalDirectCost))
                    ResultRow(label = "Giá bán đề xuất (theo FC mục tiêu):", value = currencyFormat.format(suggestedPrice))
                    ResultRow(label = "Food Cost % thực tế tại giá bán:", value = String.format(Locale.US, "%.2f%%", actualFoodCostPct))

                    Divider(color = Color.White.copy(alpha = 0.2f))

                    ResultRow(label = "Lợi nhuận gộp một phần:", value = currencyFormat.format(grossMargin))
                    ResultRow(label = "Biên lợi nhuận gộp %:", value = String.format(Locale.US, "%.2f%%", grossMarginPct))

                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (actualFoodCostPct <= targetFoodCostPct) SuccessGreen else ErrorRed
                    ) {
                        Text(
                            text = if (actualFoodCostPct <= targetFoodCostPct) "✓ Đạt chuẩn Food Cost mục tiêu" else "⚠️ Food Cost vượt mức đề xuất",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
