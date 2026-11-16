package com.example.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.ai.AiAssistantScreen
import com.example.ui.calculator.CalculatorScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.finance.FinanceScreen
import com.example.ui.ingredients.IngredientsScreen
import com.example.ui.inventory.InventoryScreen
import com.example.ui.menu.MenuScreen
import com.example.ui.recipes.RecipesScreen
import com.example.ui.reports.ReportsScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.CyanAccent

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Tổng quan", Icons.Default.Dashboard)
    object Ingredients : Screen("ingredients", "Nguyên liệu", Icons.Default.Kitchen)
    object Recipes : Screen("recipes", "Công thức", Icons.Default.RestaurantMenu)
    object Calculator : Screen("calculator", "Calculator", Icons.Default.Calculate)
    object Menu : Screen("menu", "Thực đơn", Icons.Default.MenuBook)
    object Inventory : Screen("inventory", "Kho hàng", Icons.Default.Inventory)
    object Finance : Screen("finance", "Tài chính", Icons.Default.AttachMoney)
    object Reports : Screen("reports", "Báo cáo", Icons.Default.Assessment)
    object AiAssistant : Screen("ai_assistant", "Trợ lý AI", Icons.Default.AutoAwesome)
    object Settings : Screen("settings", "Cấu hình", Icons.Default.Settings)
}

@Composable
fun AppScaffold(
    onSignOut: () -> Unit
) {
    var currentRoute by remember { mutableStateOf(Screen.Dashboard.route) }

    val items = listOf(
        Screen.Dashboard,
        Screen.Ingredients,
        Screen.Recipes,
        Screen.Calculator,
        Screen.Menu,
        Screen.Inventory,
        Screen.Finance,
        Screen.Reports,
        Screen.AiAssistant,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = NavyPrimary,
                contentColor = CyanAccent
            ) {
                // Show primary bottom nav items
                val primaryItems = listOf(
                    Screen.Dashboard,
                    Screen.Ingredients,
                    Screen.Recipes,
                    Screen.Calculator,
                    Screen.AiAssistant,
                    Screen.Settings
                )
                primaryItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(text = screen.title, maxLines = 1) },
                        selected = currentRoute == screen.route,
                        onClick = { currentRoute = screen.route },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyanAccent,
                            selectedTextColor = CyanAccent,
                            unselectedIconColor = java.util.Objects.requireNonNull(android.graphics.Color.WHITE).let { androidx.compose.ui.graphics.Color.LightGray },
                            unselectedTextColor = androidx.compose.ui.graphics.Color.LightGray,
                            indicatorColor = NavyPrimary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                Screen.Dashboard.route -> DashboardScreen(onNavigate = { currentRoute = it })
                Screen.Ingredients.route -> IngredientsScreen()
                Screen.Recipes.route -> RecipesScreen()
                Screen.Calculator.route -> CalculatorScreen()
                Screen.Menu.route -> MenuScreen()
                Screen.Inventory.route -> InventoryScreen()
                Screen.Finance.route -> FinanceScreen()
                Screen.Reports.route -> ReportsScreen()
                Screen.AiAssistant.route -> AiAssistantScreen()
                Screen.Settings.route -> SettingsScreen(onSignOut = onSignOut)
                else -> DashboardScreen(onNavigate = { currentRoute = it })
            }
        }
    }
}
