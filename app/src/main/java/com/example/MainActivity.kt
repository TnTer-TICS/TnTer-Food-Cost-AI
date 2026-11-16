package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.auth.AuthScreen
import com.example.ui.navigation.AppScaffold
import com.example.ui.theme.TnTerFoodCostTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TnTerFoodCostTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

                    if (currentUser == null) {
                        AuthScreen(
                            onAuthSuccess = {
                                currentUser = Firebase.auth.currentUser
                            }
                        )
                    } else {
                        AppScaffold(
                            onSignOut = {
                                currentUser = null
                            }
                        )
                    }
                }
            }
        }
    }
}
