package com.chris.textrecognization.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.chris.textrecognization.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "back")
            }
        }
    )
}