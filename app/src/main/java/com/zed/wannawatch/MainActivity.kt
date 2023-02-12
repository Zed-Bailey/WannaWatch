package com.zed.wannawatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.zed.wannawatch.ui.navigation.Navigation
import com.zed.wannawatch.ui.theme.WannaWatchTheme


class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WannaWatchTheme {
                Navigation()
            }
        }
    }
}