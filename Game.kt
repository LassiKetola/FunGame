package com.example.sanajahti

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CellItem(cell : Cell) {
    val style = when (cell.selected) {
        true -> Styles(color = Color(0xFFebebeb), background = Color(0xFFdeecff), border = Color(0xFFa8cdff))
        else -> Styles(color = Color(0xFFf1f1f1), background = Color(0xFFffffff), border = Color(0xFFf2f2f2))
    }
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(1.dp, style.border, RoundedCornerShape(22.dp))
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(22.dp), ambientColor = Color.Black)
            .size(70.dp, 70.dp)
            .background(color = style.background, RoundedCornerShape(22.dp))
    ) {
        Text("${cell.value}", fontSize = 18.sp, color = Color(0xFF67686D))
    }
}

@Composable
fun InformationBar() {
    Box (
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .offset(0.dp, 30.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = "0:26",
            fontSize = 32.sp,
            color = Color(0xFFb4b4b4)
        )
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "0",
            fontSize = 60.sp,
            color = Color(0xFFaaaaaa)
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(0.dp, 25.dp),
            text = "score",
            fontSize = 24.sp,
            color = Color(0xFFaaaaaa)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GridComponent() {
    var pointerPosition by rememberSaveable { mutableStateOf<Pair<Int, Int>>(Pair(0, 0)) }
    var pointerIndex : Int by rememberSaveable { mutableStateOf(0) }
    var gridSize : Int = 0
    var grid by rememberSaveable { mutableStateOf<List<Cell>>(
        (0 .. 15).mapIndexed { idx, cell -> Cell(idx = idx, '-') }
    ) }
    var counter = 0

    val test = createSmile()

    LaunchedEffect(Unit) {
    }


    fun handleDragStart(offset : Offset) {
        val (x, y) = getPointerCoords(offset.x.toInt(), offset.y.toInt(), gridSize)
        if (x >= 0 && y >= 0) {
            pointerPosition = Pair(x, y)
            pointerIndex = findCellIndex(pointerPosition)
            grid = grid.map { cell ->
                if (cell.idx == pointerIndex) {
                    Cell(
                        idx = cell.idx,
                        value = cell.value,
                        selected = true,
                    )
                } else {
                    cell
                }
            }
        }
    }

    fun handleDrag(offset : Offset) {
        val (x, y) = getPointerCoords(offset.x.toInt(), offset.y.toInt(), gridSize)
        var previousPosition : Pair<Int, Int> = Pair(0, 0)
        if (x >= 0 && y >= 0) {
            previousPosition = pointerPosition
            pointerPosition = Pair(x, y)
            pointerIndex = findCellIndex(pointerPosition)
            if (pointerPosition != previousPosition) {
                counter = counter + 1
                grid = grid.map { cell ->
                    if (cell.idx == pointerIndex) {
                        Cell(
                            idx = cell.idx,
                            value = cell.value,
                            selected = true,
                            orderIdx = counter
                        )
                    }
                    else {
                        cell
                    }
                }
            }
        }
    }

    fun handleDragEnd() {
        counter = 0
        grid = grid.map { cell -> Cell(
            idx = cell.idx,
            value = cell.value,
            selected = false
        )}
    }

    FlowRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .offset(0.dp, 0.dp)
            .size(320.dp, 320.dp)
            .background(
                color = Color.Transparent,
                RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
            )
            .onGloballyPositioned { layoutCoordinates ->
                gridSize = layoutCoordinates.boundsInParent().bottomRight.x.toInt()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> handleDragStart(offset) },
                    onDrag = { change, _ -> handleDrag(change.position) },
                    onDragEnd = { handleDragEnd() },
                    onDragCancel = {},
                )
            }

    ) {
        grid.map { cell -> CellItem(cell) }
    }
}

@Composable
fun PuzzleGame() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(0.0f to Color(0xFFE5F6FF), 1.0f to Color(0xFFF9F5FF))
            )
    ) {

        Box(modifier = Modifier.align(Alignment.TopCenter)) { InformationBar() }
        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(0.dp, -75.dp)
        ) {
            GridComponent()
        }
    }
}