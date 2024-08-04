package com.chris.textrecognization.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chris.textrecognization.R
import kotlin.math.min

@Preview(showBackground = true)
@Composable
fun MainScreen(
    navigateToOCR: () -> Unit = {},
    recognizeText: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        onClick = { /*TODO*/ }) {

        val pagerState = rememberPagerState(pageCount = {
            2
        })

        Box(
            modifier = Modifier
                .fillMaxSize()) {
            
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.inversePrimary)
                .fillMaxWidth()
                .fillMaxHeight(1 / 2f)
                .heightIn(415.dp)) {}

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,

            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 160.dp, bottom = 8.dp),
                    text = "select your tool",
                    style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                HorizontalPager(

                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),

                    state = pagerState ) { page ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally), // Aligning the box horizontally
                        contentAlignment = Alignment.Center // Aligning the content inside the box
                    ) {
                        when (page) {
                        0 ->
                            Card(
                                colors = CardDefaults.cardColors(Color.Transparent),
                                onClick = { navigateToOCR() }
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(
                                            height = 300.dp,
                                            width = 280.dp
                                        ),
                                    contentScale = ContentScale.FillBounds,
                                    painter = painterResource(id = R.drawable.button_ocr),
                                    contentDescription = "ocr")
                            }

                        1 -> Card(
                            colors = CardDefaults.cardColors(Color.Transparent),
                            onClick = { recognizeText() }) {
                            Image(
                                modifier = Modifier
                                    .size(
                                        height = 300.dp,
                                        width = 280.dp
                                    ),
                                contentScale = ContentScale.FillBounds,
                                painter = painterResource(id = R.drawable.button_imagetotext),
                                contentDescription = "ocr")
                        }
                    }

                    }
                    
                    }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "scan document and \n" +
                                "convert photo to text " +
                                "\ninstantly",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "-Anywhere, Anytime!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }

}

