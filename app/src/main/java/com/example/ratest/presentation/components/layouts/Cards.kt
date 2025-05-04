package com.example.ratest.presentation.components.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ratest.ui.theme.LightGreen
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun InfoCard(
    title: String,
    description: String,
    icon: ImageVector = Icons.Default.Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: Color = DarkGreen
) {
    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .background(color)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = LightGreen)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = Green)
                Text(description, style = MaterialTheme.typography.body2, color = White)
            }
        }
    }
}

@Composable
fun SmallCard(
    title: String,
    description: String = "",
    onClick: () -> Unit = {},
    color: Color = DarkGreen,
    icon: ImageVector = Icons.Default.Place
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(150.dp, 200.dp)
            .background(White, shape = RoundedCornerShape(8.dp))
            .border(2.dp, DarkGreen, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                LightGreen
            )
            Text(
                title,
                textAlign = TextAlign.Center,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun CardBackgroundImage(
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen)
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Green,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body2,
                        color = White
                    )
                }
            }

        }
    }
}
