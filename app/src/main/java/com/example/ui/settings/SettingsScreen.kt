package com.example.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Logout
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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSignOut: () -> Unit
) {
    val currentUser = Firebase.auth.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản trị Hệ thống & Cấu hình", color = Color.White, fontWeight = FontWeight.Bold) },
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
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Business, contentDescription = null, tint = CyanAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Thông tin Doanh nghiệp & Thương hiệu", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)
                    }

                    Text(text = "Tên doanh nghiệp: TnTer Digital F&B Group", fontSize = 14.sp)
                    Text(text = "Chi nhánh quản lý: Chi nhánh Trung tâm TP.HCM", fontSize = 14.sp)
                    Text(text = "Mã tài khoản: ${currentUser?.email ?: currentUser?.uid ?: "Chế độ Trải nghiệm [DEMO]"}", fontSize = 13.sp, color = Color.Gray)
                    Text(text = "Vai trò: Chủ doanh nghiệp / Quản lý nhà hàng", fontSize = 13.sp, color = Color.Gray)
                    Text(text = "Mục tiêu Food Cost chuẩn: 30.0%", fontSize = 13.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            Firebase.auth.signOut()
                            onSignOut()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Đăng xuất tài khoản", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text(
                text = "TnTer Food Cost AI v1.0 • Phát triển bởi TnTer Digital",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
