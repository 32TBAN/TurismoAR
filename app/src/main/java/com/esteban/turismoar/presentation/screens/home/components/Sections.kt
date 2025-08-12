package com.esteban.turismoar.presentation.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.esteban.turismoar.R
import com.esteban.turismoar.presentation.components.buttons.CustomButton
import com.esteban.turismoar.presentation.components.inputs.ImageSlider
import com.esteban.turismoar.presentation.components.text.SectionTitle
import com.esteban.turismoar.presentation.navigation.RoutesScreen
import com.esteban.turismoar.ui.theme.DarkGreen
import com.esteban.turismoar.utils.Utils

@Composable
fun TopSection(navController: NavController) {
    val images = Utils.getList()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
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
            onClick = { navController.navigate(RoutesScreen) },
            icon = Icons.Default.PlayArrow
        )
    }
}
