package com.example.manifestie.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    errorDescription: String = "Some error occurred, please try again later.",
    onTryAgainClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(MaterialTheme.shapes.large)
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "ErrorBox_icon",
                modifier = Modifier
                    .size(50.dp),
                tint = Color.DarkGray
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                //.padding(vertical = 10.dp)
                ,
                text = "An error occurred!",
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .padding(bottom = 10.dp),
                text = errorDescription,
                color = Color.DarkGray,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onTryAgainClick,
                    modifier = Modifier.height(40.dp),
                ) {
                    Text(
                        text = "Try Again",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }



        }
    }
}