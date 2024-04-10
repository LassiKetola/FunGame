package com.example.sanajahti

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/*
@Composable
fun MovingCircles() {
    val position = remember { Animatable(400f) }

    /*
        LaunchedEffect(Unit) {
            position
            .animateTo(
                targetValue = 1400f,
                animationSpec = tween(durationMillis = 9000)
            )
        }
    */

    Canvas(
        modifier = Modifier
            .size(75.dp, 75.dp)
            .background(Color.Black)
    ) {
        drawCircle(color = Color(0xFFf72d69), center = Offset(0f, 0f), radius = 10f)
    }
}
*/

data class SliderItemStyles (
    val color : Color,
    val rotationY : Float,
    val offset : Offset,
    val alpha : Float
)

@Composable
fun OptionsSlider() {

}

@Composable
fun GameLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)

    ) {}
}

val salsaFont = FontFamily(
    Font(R.font.salsa_regular)
)

@Composable
fun Difficulty() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.Transparent)
    ) {
        Box (
            modifier = Modifier
                .align(Alignment.Center)
                .offset(-120.dp, 0.dp)
                .graphicsLayer { rotationY = -60f }
                .alpha(0.2f)
        ) {
            Text(text = "Easy", fontSize = 30.sp, fontFamily = salsaFont)
        }
        Box (modifier = Modifier.align(Alignment.Center)) {
            Text(text = "Advanced", fontSize = 40.sp, fontFamily = salsaFont)
        }
        Box (
            modifier = Modifier
                .align(Alignment.Center)
                .offset(120.dp, 0.dp)
                .graphicsLayer { rotationY = 60f }
                .alpha(0.2f)

        ) {
            Text(text = "Hard", fontSize = 30.sp, fontFamily = salsaFont)
        }
    }
}

@Composable
fun AnimatedLine() {
    Box(
        modifier = Modifier
            .size(160.dp, 1.dp)
            .background(color = Color(0xFFe9e9e9))
    ) {

    }
}

@Composable
fun PlayButton() {
    Box(
        modifier = Modifier
            .size(200.dp, 50.dp)
            .border(1.dp, color = Color(0xFF545454), RoundedCornerShape(20.dp))
            .background(color = Color(0xFF4E95FD), RoundedCornerShape(20.dp))
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "PLAY",
            fontSize = 24.sp,
            fontFamily = salsaFont,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun GameMode() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(0.0f to Color(0xFFE5F6FF), 1.0f to Color(0xFFF9F5FF)))
    ) {
       // Box(modifier = Modifier.align(Alignment.Center)) { Difficulty() }
        Box (modifier = Modifier.align(Alignment.Center).offset(0.dp, -125.dp)) {GameLogo()}
        Box (modifier = Modifier.align(Alignment.Center).offset(0.dp, 80.dp)) {Difficulty()}
        Box (modifier = Modifier.align(Alignment.Center).offset(0.dp, 120.dp)) {AnimatedLine()}
        Box (modifier = Modifier.align(Alignment.BottomCenter).offset(0.dp, -75.dp)) {PlayButton()}
    }
}