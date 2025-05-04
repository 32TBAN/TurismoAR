package com.example.ratest.presentation.components.layouts.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.R
import com.example.ratest.presentation.components.models.CustomButton
import com.example.ratest.presentation.components.models.ImageSlider
import com.example.ratest.presentation.components.models.SectionTitle
import com.example.ratest.presentation.navigation.RoutesScreen
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.utils.Utils

@Composable
fun TopSection(navController: NavController) {
    val images = Utils.getList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageSlider(images = images)

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle(title = stringResource(R.string.text_title_section_home), color = DarkGreen)

        Text(
            stringResource(R.string.text_subtitle_home),
            fontSize = 14.sp,
            color = DarkGreen,
            textAlign = TextAlign.Center
        )

        CustomButton(
            text = stringResource(R.string.button_text_home1),
            onClick = { navController.navigate(RoutesScreen) }
        )
    }
}
