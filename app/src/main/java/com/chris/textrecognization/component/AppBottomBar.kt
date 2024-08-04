package com.chris.textrecognization.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chris.textrecognization.R

@Composable
fun AppBottomBar(
    label: String,
    onClick: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { onClick() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_camera_24),
                    contentDescription = label,
                    tint = Color.Black
                )
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    text = label)
            }
        }
    }
}