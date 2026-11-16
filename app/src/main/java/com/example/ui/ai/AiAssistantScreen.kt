package com.example.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AiRepository
import com.example.data.RestaurantRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent
import kotlinx.coroutines.launch

data class ChatMessage(val sender: String, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen() {
    val coroutineScope = rememberCoroutineScope()
    val aiRepository = remember { AiRepository() }
    val dishes by RestaurantRepository.dishes.collectAsState()
    val ingredients by RestaurantRepository.ingredients.collectAsState()

    var inputPrompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage("ai", "Xin chào! Tôi là Trợ lý AI F&B của TnTer Digital. Tôi đã sẵn sàng phân tích số liệu Food Cost, công thức món ăn và đề xuất tối ưu lợi nhuận cho nhà hàng của bạn. Bạn muốn hỏi gì hôm nay?")
        )
    }

    val suggestedQuestions = listOf(
        "Vì sao Food Cost món Phở Bò vượt ngưỡng?",
        "Nguyên liệu nào đang chiếm tỷ trọng chi phí cao nhất?",
        "Đề xuất giá bán phù hợp cho món Cơm Gà với Food Cost mục tiêu 30%?",
        "Làm thế nào để giảm hao hụt sơ chế thịt và hải sản?"
    )

    val restaurantContext = buildString {
        append("Danh sách món ăn:\n")
        dishes.forEach { append("- ${it.name}: Giá bán ${it.sellingPrice} VND, Giá vốn ${it.totalCostPerPortion} VND, Food Cost ${it.actualFoodCostPercentage}%\n") }
        append("\nDanh sách nguyên liệu chính:\n")
        ingredients.take(10).forEach { append("- ${it.name}: Giá ${it.purchasePrice} VND/${it.purchaseUnit}, hao hụt ${it.yieldPercentage}%\n") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trợ lý AI F&B (Gemini 3.8 Flash)", color = Color.White, fontWeight = FontWeight.Bold) },
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isAi = msg.sender == "ai"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isAi) Color.White else NavyPrimary,
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                if (isAi) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "TnTer AI F&B", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = msg.text,
                                    fontSize = 13.sp,
                                    color = if (isAi) NavyPrimary else Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Quick suggestion chips
            if (!isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestedQuestions.take(2).forEach { suggestion ->
                        OutlinedButton(
                            onClick = {
                                inputPrompt = suggestion
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
                        ) {
                            Text(text = suggestion, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputPrompt,
                    onValueChange = { inputPrompt = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nhập câu hỏi cho trợ lý AI...") },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                FloatingActionButton(
                    onClick = {
                        if (inputPrompt.isNotBlank() && !isLoading) {
                            val promptToSend = inputPrompt
                            messages.add(ChatMessage("user", promptToSend))
                            inputPrompt = ""
                            isLoading = true
                            coroutineScope.launch {
                                val reply = aiRepository.askAssistant(promptToSend, restaurantContext)
                                messages.add(ChatMessage("ai", reply))
                                isLoading = false
                            }
                        }
                    },
                    containerColor = CyanAccent,
                    contentColor = NavyPrimary,
                    modifier = Modifier.size(52.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NavyPrimary, strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Gửi")
                    }
                }
            }
        }
    }
}
