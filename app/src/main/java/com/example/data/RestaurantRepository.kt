package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object RestaurantRepository {
    private val _ingredients = MutableStateFlow(
        listOf(
            Ingredient("ing_1", "NVL001", "Thịt bò thăn", "Thịt & Thủy Hải Sản", "kg", "g", 1000.0, 250000.0, "Cty Thực phẩm Sạch Sài Gòn", 90.0, "2026-11-15"),
            Ingredient("ing_2", "NVL002", "Thịt heo ba chỉ", "Thịt & Thủy Hải Sản", "kg", "g", 1000.0, 140000.0, "Chợ đầu mối Thủ Đức", 85.0, "2026-11-15"),
            Ingredient("ing_3", "NVL003", "Thịt gà ta", "Thịt & Thủy Hải Sản", "kg", "g", 1000.0, 110000.0, "Trang trại Gà Việt", 80.0, "2026-11-14"),
            Ingredient("ing_4", "NVL004", "Xương ống bò", "Thịt & Thủy Hải Sản", "kg", "g", 1000.0, 65000.0, "Cty Thực phẩm Sạch Sài Gòn", 100.0, "2026-11-15"),
            Ingredient("ing_5", "NVL005", "Bánh phở tươi", "Đồ khô & Bánh", "kg", "g", 1000.0, 25000.0, "Lò mì Hưng Phát", 100.0, "2026-11-15"),
            Ingredient("ing_6", "NVL006", "Bún tươi", "Đồ khô & Bánh", "kg", "g", 1000.0, 18000.0, "Lò bún Thanh Tâm", 100.0, "2026-11-15"),
            Ingredient("ing_7", "NVL007", "Gạo thơm Jasmine", "Đồ khô & Bánh", "kg", "g", 1000.0, 22000.0, "Đại lý gạo miền Nam", 100.0, "2026-11-10"),
            Ingredient("ing_8", "NVL008", "Bột gạo", "Gia vị & Bột", "kg", "g", 1000.0, 20000.0, "Cty Tân Nam Phát", 100.0, "2026-11-01"),
            Ingredient("ing_9", "NVL009", "Bột năng", "Gia vị & Bột", "kg", "g", 1000.0, 24000.0, "Cty Tân Nam Phát", 100.0, "2026-11-01"),
            Ingredient("ing_10", "NVL010", "Tôm tươi sống", "Thịt & Thủy Hải Sản", "kg", "g", 1000.0, 220000.0, "Vựa hải sản Biển Đông", 70.0, "2026-11-15"),
            Ingredient("ing_11", "NVL011", "Hành lá, ngò rí", "Rau củ quả", "kg", "g", 1000.0, 35000.0, "Hợp tác xã Rau Đà Lạt", 80.0, "2026-11-15"),
            Ingredient("ing_12", "NVL012", "Giá đỗ", "Rau củ quả", "kg", "g", 1000.0, 15000.0, "Hợp tác xã Rau Đà Lạt", 95.0, "2026-11-15"),
            Ingredient("ing_13", "NVL013", "Hành tây", "Rau củ quả", "kg", "g", 1000.0, 20000.0, "Hợp tác xã Rau Đà Lạt", 90.0, "2026-11-12"),
            Ingredient("ing_14", "NVL014", "Chanh tươi", "Rau củ quả", "kg", "g", 1000.0, 25000.0, "Hợp tác xã Rau Đà Lạt", 100.0, "2026-11-14"),
            Ingredient("ing_15", "NVL015", "Ớt hiểm", "Rau củ quả", "kg", "g", 1000.0, 45000.0, "Hợp tác xã Rau Đà Lạt", 95.0, "2026-11-14"),
            Ingredient("ing_16", "NVL016", "Dầu ăn thực vật", "Gia vị & Bột", "lít", "ml", 1000.0, 42000.0, "Siêu thị Mega Market", 100.0, "2026-10-20"),
            Ingredient("ing_17", "NVL017", "Nước mắm nhỉ", "Gia vị & Bột", "lít", "ml", 1000.0, 65000.0, "Nước mắm Hưng Thịnh", 100.0, "2026-09-15"),
            Ingredient("ing_18", "NVL018", "Đường cát trắng", "Gia vị & Bột", "kg", "g", 1000.0, 24000.0, "Đường Biên Hòa", 100.0, "2026-10-10"),
            Ingredient("ing_19", "NVL019", "Hạt nêm Knorr", "Gia vị & Bột", "kg", "g", 1000.0, 55000.0, "Siêu thị Mega Market", 100.0, "2026-10-15"),
            Ingredient("ing_20", "NVL020", "Hộp giấy đựng food", "Bao bì", "cái", "cái", 1.0, 1500.0, "Cty Bao bì Xanh", 100.0, "2026-11-01")
        )
    )
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    private val _dishes = MutableStateFlow(
        listOf(
            Dish(
                id = "dish_1",
                code = "MON01",
                name = "Phở Bò Tái Nạm",
                category = "Món chính",
                servings = 1,
                items = listOf(
                    RecipeItem("ing_5", "Bánh phở tươi", 150.0, "g", 25.0),
                    RecipeItem("ing_1", "Thịt bò thăn", 80.0, "g", 277.78), // 250k / 0.9 = 277.78/kg => 0.2778/g
                    RecipeItem("ing_4", "Xương ống bò", 100.0, "g", 65.0),
                    RecipeItem("ing_11", "Hành lá, ngò rí", 15.0, "g", 43.75),
                    RecipeItem("ing_13", "Hành tây", 20.0, "g", 22.22)
                ),
                directLaborCost = 5000.0,
                packagingCost = 2000.0,
                targetFoodCostPercentage = 28.0,
                sellingPrice = 65000.0,
                status = "Đang kinh doanh"
            ),
            Dish(
                id = "dish_2",
                code = "MON02",
                name = "Bún Bò Huế Đặc Biệt",
                category = "Món chính",
                servings = 1,
                items = listOf(
                    RecipeItem("ing_6", "Bún tươi", 180.0, "g", 18.0),
                    RecipeItem("ing_1", "Thịt bò thăn", 60.0, "g", 277.78),
                    RecipeItem("ing_2", "Thịt heo ba chỉ", 60.0, "g", 164.71),
                    RecipeItem("ing_11", "Hành lá, ngò rí", 10.0, "g", 43.75)
                ),
                directLaborCost = 6000.0,
                packagingCost = 2000.0,
                targetFoodCostPercentage = 30.0,
                sellingPrice = 60000.0,
                status = "Đang kinh doanh"
            ),
            Dish(
                id = "dish_3",
                code = "MON03",
                name = "Cơm Gà Xối Mỡ",
                category = "Món chính",
                servings = 1,
                items = listOf(
                    RecipeItem("ing_7", "Gạo thơm Jasmine", 200.0, "g", 22.0),
                    RecipeItem("ing_3", "Thịt gà ta", 180.0, "g", 137.5),
                    RecipeItem("ing_16", "Dầu ăn thực vật", 30.0, "ml", 42.0)
                ),
                directLaborCost = 5500.0,
                packagingCost = 2500.0,
                targetFoodCostPercentage = 32.0,
                sellingPrice = 55000.0,
                status = "Đang kinh doanh"
            ),
            Dish(
                id = "dish_4",
                code = "MON04",
                name = "Bánh Bèo Miền Trung",
                category = "Khai vị",
                servings = 1,
                items = listOf(
                    RecipeItem("ing_8", "Bột gạo", 100.0, "g", 20.0),
                    RecipeItem("ing_9", "Bột năng", 20.0, "g", 24.0),
                    RecipeItem("ing_10", "Tôm tươi sống", 50.0, "g", 314.29)
                ),
                directLaborCost = 4000.0,
                packagingCost = 2000.0,
                targetFoodCostPercentage = 25.0,
                sellingPrice = 40000.0,
                status = "Đang kinh doanh"
            ),
            Dish(
                id = "dish_5",
                code = "MON05",
                name = "Bánh Bột Lọc Tôm Thịt",
                category = "Khai vị",
                servings = 1,
                items = listOf(
                    RecipeItem("ing_9", "Bột năng", 120.0, "g", 24.0),
                    RecipeItem("ing_2", "Thịt heo ba chỉ", 40.0, "g", 164.71),
                    RecipeItem("ing_10", "Tôm tươi sống", 40.0, "g", 314.29)
                ),
                directLaborCost = 4500.0,
                packagingCost = 2000.0,
                targetFoodCostPercentage = 28.0,
                sellingPrice = 45000.0,
                status = "Đang kinh doanh"
            )
        )
    )
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    private val _suppliers = MutableStateFlow(
        listOf(
            Supplier("sup_1", "Cty Thực phẩm Sạch Sài Gòn", "Anh Tuấn", "0901234567", "Thịt bò & Xương"),
            Supplier("sup_2", "Chợ đầu mối Thủ Đức", "Chị Mai", "0912345678", "Thịt heo & Thực phẩm"),
            Supplier("sup_3", "Trang trại Gà Việt", "Chú Ba", "0987654321", "Thịt gà ta"),
            Supplier("sup_4", "Hợp tác xã Rau Đà Lạt", "Anh Hùng", "0933445566", "Rau củ quả sạch"),
            Supplier("sup_5", "Nước mắm Hưng Thịnh", "Đại lý Phía Nam", "02838383838", "Gia vị")
        )
    )
    val suppliers: StateFlow<List<Supplier>> = _suppliers.asStateFlow()

    private val _inventoryTransactions = MutableStateFlow(
        listOf(
            InventoryTransaction("tx_1", "NHẬP KHO", "ing_1", "Thịt bò thăn", 20.0, "kg", 250000.0, 5000000.0, "Nhập nguyên liệu định kỳ", "Kho Sài Gòn", "2026-11-15 07:30"),
            InventoryTransaction("tx_2", "NHẬP KHO", "ing_5", "Bánh phở tươi", 30.0, "kg", 25000.0, 750000.0, "Nhập mới buổi sáng", "Kho Sài Gòn", "2026-11-15 06:00"),
            InventoryTransaction("tx_3", "XUẤT KHO", "ing_1", "Thịt bò thăn", 5.0, "kg", 250000.0, 1250000.0, "Xuất cho bếp chế biến Phở", "Bếp Trưởng", "2026-11-15 09:00"),
            InventoryTransaction("tx_4", "HỦY HÀNG", "ing_11", "Hành lá, ngò rí", 1.5, "kg", 35000.0, 52500.0, "Héo úa do bảo quản", "Bếp Trưởng", "2026-11-14 22:00"),
            InventoryTransaction("tx_5", "NHẬP KHO", "ing_10", "Tôm tươi sống", 15.0, "kg", 220000.0, 3300000.0, "Nhập hải sản tươi", "Kho Sài Gòn", "2026-11-15 08:00")
        )
    )
    val inventoryTransactions: StateFlow<List<InventoryTransaction>> = _inventoryTransactions.asStateFlow()

    private val _salesRecords = MutableStateFlow(
        listOf(
            SaleRecord("sale_1", "dish_1", "Phở Bò Tái Nạm", 45, 65000.0, 2925000.0, 18225.0 * 45, "2026-11-15"),
            SaleRecord("sale_2", "dish_2", "Bún Bò Huế Đặc Biệt", 32, 60000.0, 1920000.0, 18000.0 * 32, "2026-11-15"),
            SaleRecord("sale_3", "dish_3", "Cơm Gà Xối Mỡ", 28, 55000.0, 1540000.0, 17600.0 * 28, "2026-11-15"),
            SaleRecord("sale_4", "dish_4", "Bánh Bèo Miền Trung", 20, 40000.0, 800000.0, 10000.0 * 20, "2026-11-15"),
            SaleRecord("sale_5", "dish_5", "Bánh Bột Lọc Tôm Thịt", 25, 45000.0, 1125000.0, 12600.0 * 25, "2026-11-15")
        )
    )
    val salesRecords: StateFlow<List<SaleRecord>> = _salesRecords.asStateFlow()

    private val _expenses = MutableStateFlow(
        listOf(
            ExpenseRecord("exp_1", "Chi phí nhân sự", "Lương bếp & phục vụ ca sáng/chiều", 1200000.0, "2026-11-15"),
            ExpenseRecord("exp_2", "Điện nước", "Tiền điện nước ước tính ngày", 350000.0, "2026-11-15"),
            ExpenseRecord("exp_3", "Thuê mặt bằng", "Phân bổ tiền thuê mặt bằng ngày", 600000.0, "2026-11-15"),
            ExpenseRecord("exp_4", "Vận hành khác", "Gas, gia vị phụ trợ, bao bì", 250000.0, "2026-11-15")
        )
    )
    val expenses: StateFlow<List<ExpenseRecord>> = _expenses.asStateFlow()

    // Methods to modify state for full interactivity
    fun addIngredient(ingredient: Ingredient) {
        _ingredients.value = _ingredients.value + ingredient
    }

    fun updateIngredient(ingredient: Ingredient) {
        _ingredients.value = _ingredients.value.map { if (it.id == ingredient.id) ingredient else it }
    }

    fun deleteIngredient(id: String) {
        _ingredients.value = _ingredients.value.filter { it.id != id }
    }

    fun addDish(dish: Dish) {
        _dishes.value = _dishes.value + dish
    }

    fun updateDish(dish: Dish) {
        _dishes.value = _dishes.value.map { if (it.id == dish.id) dish else it }
    }

    fun deleteDish(id: String) {
        _dishes.value = _dishes.value.filter { it.id != id }
    }

    fun addInventoryTransaction(tx: InventoryTransaction) {
        _inventoryTransactions.value = listOf(tx) + _inventoryTransactions.value
    }

    fun addExpense(expense: ExpenseRecord) {
        _expenses.value = listOf(expense) + _expenses.value
    }
}
