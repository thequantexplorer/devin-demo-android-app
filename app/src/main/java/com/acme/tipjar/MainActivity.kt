package com.acme.tipjar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.acme.tipjar.ui.TipJarApp
import com.acme.tipjar.ui.theme.TipJarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipJarTheme {
                TipJarApp()
            }
        }
    }
}
