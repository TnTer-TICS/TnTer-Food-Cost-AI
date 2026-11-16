package com.example.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp

data class Ingredient(
    @DocumentId val id: String = "",
    val code: String = "",
    val name: String = "",
    val category: String = "Thịt & Thủy Hải Sản",
    val purchaseUnit: String = "kg",
    val usageUnit: String = "g",
    val conversionFactor: Double = 1000.0, // 1 kg = 1000 g
    val purchasePrice: Double = 0.0, // VND per purchase unit
    val supplier: String = "",
    val yieldPercentage: Double = 100.0, // % usable after preparation (e.g. 80%)
    val updatedAt: String = "2026-11-15"
) {
    val actualUnitCost: Double
        get() {
            val baseCostPerUsageUnit = purchasePrice / conversionFactor
            return if (yieldPercentage > 0) baseCostPerUsageUnit / (yieldPercentage / 100.0) else baseCostPerUsageUnit
        }
}

data class RecipeItem(
    val ingredientId: String = "",
    val ingredientName: String = "",
    val quantity: Double = 0.0, // in usage unit
    val unit: String = "g",
    val unitCost: Double = 0.0
) {
    val cost: Double
        get() = quantity * unitCost
}

data class Dish(
    @DocumentId val id: String = "",
    val code: String = "",
    val name: String = "",
    val category: String = "Món chính",
    val servings: Int = 1,
    val items: List<RecipeItem> = emptyList(),
    val directLaborCost: Double = 0.0,
    val packagingCost: Double = 0.0,
    val targetFoodCostPercentage: Double = 30.0,
    val sellingPrice: Double = 0.0, // VND excluding VAT
    val status: String = "Đang kinh doanh", // Đang kinh doanh / Tạm ngưng
    val imageUrl: String = ""
) {
    val totalCostPerPortion: Double
        get() {
            val totalIngredientsCost = items.sumOf { it.cost }
            val portionBase = if (servings > 0) servings else 1
            return (totalIngredientsCost / portionBase) + directLaborCost + packagingCost
        }

    val actualFoodCostPercentage: Double
        get() = if (sellingPrice > 0) (totalCostPerPortion / sellingPrice) * 100.0 else 0.0

    val grossMargin: Double
        get() = sellingPrice - totalCostPerPortion

    val grossMarginPercentage: Double
        get() = if (sellingPrice > 0) (grossMargin / sellingPrice) * 100.0 else 0.0
}

data class Supplier(
    @DocumentId val id: String = "",
    val name: String = "",
    val contactPerson: String = "",
    val phone: String = "",
    val category: String = ""
)

data class InventoryTransaction(
    @DocumentId val id: String = "",
    val type: String = "NHẬP KHO", // NHẬP KHO, XUẤT KHO, HỦY HÀNG, KIỂM KÊ
    val ingredientId: String = "",
    val ingredientName: String = "",
    val quantity: Double = 0.0,
    val unit: String = "kg",
    val unitPrice: Double = 0.0,
    val totalAmount: Double = 0.0,
    val reason: String = "",
    val performedBy: String = "Quản lý kho",
    val timestamp: String = "2026-11-15 08:30"
)

data class SaleRecord(
    @DocumentId val id: String = "",
    val dishId: String = "",
    val dishName: String = "",
    val quantitySold: Int = 0,
    val unitPrice: Double = 0.0,
    val totalRevenue: Double = 0.0,
    val totalCost: Double = 0.0,
    val date: String = "2026-11-15"
)

data class ExpenseRecord(
    @DocumentId val id: String = "",
    val category: String = "Chi phí nhân sự", // Chi phí nhân sự, Điện nước, Thuê mặt bằng, Vận hành khác
    val description: String = "",
    val amount: Double = 0.0,
    val date: String = "2026-11-15"
)
