package com.example.ratest.Utils

import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarIcon {
    data class Vector(val icon: ImageVector) : BottomBarIcon()
    data class Drawable(val resId: Int) : BottomBarIcon()
}
