package com.example.isemptyparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.isemptyparcial.ui.theme.IsEmptyParcialTheme
import com.example.isemptyparcial.ui.theme.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IsEmptyParcialTheme {
                MainScreen()
            }
        }
    }
}
///xd