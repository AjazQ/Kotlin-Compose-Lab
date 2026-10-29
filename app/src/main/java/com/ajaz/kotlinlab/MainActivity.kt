package com.ajaz.kotlinlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ajaz.kotlinlab.navigation.AppNav
import com.ajaz.kotlinlab.ui.theme.KotlinLabTheme
import com.ajaz.kotlinlab.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var themeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
            KotlinLabTheme(themeMode = themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNav(
                        themeMode = themeMode,
                        onThemeModeChange = { themeMode = it }
                    )
                }
            }
        }
    }
}
