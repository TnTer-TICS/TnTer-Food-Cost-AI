package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class AiRepository {
    private val TAG = "AiRepository"
    private val model: GenerativeModel? by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.8-flash")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase AI model", e)
            null
        }
    }

    suspend fun askAssistant(prompt: String, restaurantContext: String): String = withContext(Dispatchers.IO) {
        val generativeModel = model ?: return@withContext "Trợ lý AI chưa được khởi tạo hoặc cấu hình Firebase AI Logic chưa sẵn sàng."

        val fullPrompt = """
            Bạn là Trợ lý AI F&B chuyên gia vận hành nhà hàng và quản lý Food Cost của TnTer Digital.
            Dữ liệu nhà hàng hiện tại:
            $restaurantContext
            
            Câu hỏi của người dùng:
            $prompt
            
            Hãy trả lời bằng tiếng Việt chuyên nghiệp, ngắn gọn, phân tích rõ số liệu, giả định và đề xuất giải pháp tối ưu chi phí và tăng lợi nhuận.
        """.trimIndent()

        try {
            withTimeout(45_000L) {
                val response = generativeModel.generateContent(fullPrompt)
                response.text ?: "AI không trả về kết quả."
            }
        } catch (e: Exception) {
            Log.e(TAG, "AI generation failed", e)
            "Lỗi kết nối hoặc xử lý AI: ${e.localizedMessage ?: "Vui lòng thử lại sau."}"
        }
    }
}
