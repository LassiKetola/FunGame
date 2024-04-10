package com.example.sanajahti

import androidx.compose.ui.graphics.Color

data class Cell (
    val idx : Int,
    val value : Char,
    val selected : Boolean = false,
    val orderIdx : Int? = 0
)

data class Styles (
    val background : Color,
    val color : Color,
    val border : Color
)

// Game pointer range with offsets
data class Range (val x : Int, val y : Int) {
    fun padding(padding : Int) : IntRange {
        return (this.x + padding) .. (this.y - padding)
    }
}

// Converts grid coordinates to index: findCellIndex(3, 3) = 15
fun findCellIndex(position : Pair<Int, Int>) : Int {
    return position.first + (position.second * 4)
}

// Converts index to grid coordinates: getCoordsFromIndex(15) = (3, 3)
fun getCoordsFromIndex(index: Int): Pair<Int, Int> {
    val y = index / 4
    val x = index % 4
    return Pair(y, x)
}

// Returns all adjacents cells for grid coordinate: getAdjacentCells(0,0) = [Pair(0,1), Pair(1,0) Pair(1,1)]
fun getAdjacentCells(cell : Pair<Int, Int>) : List<Pair<Int, Int>> {
    val adjacentCells : List<(Int, Int) -> Pair<Int, Int>> = listOf(
        // list of functions that returns all adjacent cells for a given Pair for example: Pair(2, 2)
        { x, y -> Pair(x - 1, y - 1) },
        { x, y -> Pair(x - 1, y) },
        { x, y -> Pair(x - 1, y + 1) },
        { x, y -> Pair(x, y + 1) },
        { x, y -> Pair(x + 1, y + 1) },
        { x, y -> Pair(x + 1, y) },
        { x, y -> Pair(x + 1, y - 1) },
        { x, y -> Pair(x, y - 1) }
    )
    val isWithinBounds : (Pair<Int, Int>) -> Boolean = { pair ->
        pair.first >= 0
        && pair.second >= 0
        && pair.first <= 3
        && pair.second <= 3
    }
    return adjacentCells
        .map {fn -> fn(cell.first, cell.second)}
        .filter {pair -> isWithinBounds(pair)}
}

// Returns grid coordinate based on which range pointer is located in the parent component:
// getPointerCoord(100, 100) = Pair(0, 1) = "moving finger on top of the grids first element"
fun getPointerCoords(offsetX : Int, offsetY: Int, gridSize : Int) : Pair<Int, Int> {
    val offset = 30
    val cell = gridSize / 4
    val x = when(offsetX) {
        in Range(0, cell).padding(offset)            -> 0
        in Range(cell, cell * 2).padding(offset)     -> 1
        in Range(cell * 2, cell * 3).padding(offset) -> 2
        in Range(cell * 3, cell * 4).padding(offset) -> 3
        else -> -1
    }
    val y = when(offsetY) {
        in Range(0, cell).padding(offset)            -> 0
        in Range(cell, cell * 2).padding(offset)     -> 1
        in Range(cell * 2, cell * 3).padding(offset) -> 2
        in Range(cell * 3, cell * 4).padding(offset) -> 3
        else -> -1
    }
    return Pair(x, y)
}

// Returns a list of cell data classes that are randomly positioned next to each other in the grid
fun createFirstWord(word : String) : List<Cell> {
    var result = mutableListOf<Cell>()
    var currentCell : Pair<Int, Int> = Pair(3, 3)
    var usedCells = mutableListOf<Pair<Int, Int>>()
    var currentIndex : Int
    var adjacentCells : List<Pair<Int, Int>> = getAdjacentCells(currentCell)
    for (i in 0 until word.length) {
        // set current grid pair to the list of used cells
        usedCells.add(currentCell)
        // set current grid pair position index in variable
        currentIndex = findCellIndex(currentCell)
        // create new cell item with correct letter and index
        result.add(Cell(currentIndex, word[i]))
        // move to a new random adjacent cell location in the grid
        currentCell = adjacentCells.random()
        // set new adjacent cells for the new grid position and remove all cells that are already used
        adjacentCells = getAdjacentCells(currentCell).filter { cell -> !usedCells.contains(cell) }
    }
    return result
}

// Replaces the newly generated cells with grid cells that had those positions
fun mergeCells(gridCells : List<Cell>, newCells : List<Cell>) : List<Cell> {
    // start reducing cells into a new list, but starting value is app grid containing cells
    return newCells.fold(gridCells) { oldList, newListValue ->
        (oldList.filter { cell -> cell.idx != newListValue.idx }) + newListValue
    }
}

// Fills the game grid with given words
fun wordAlgorithm(word : String, grid : List<Cell>) : List<Cell>  {
    var cell : Pair<Int, Int>? = null
    var adjacentCells : List<Pair<Int, Int>> = listOf()
    var usedCells : MutableList<Pair<Int, Int>> = mutableListOf()
    var continuation : Boolean
    var result : MutableList<Cell> = mutableListOf()
    for (i in 0 until word.length) {
        continuation = false

        // if first character in word, check if it exists in the grid
        // if it exists continue from that position to generate as much overlapping words as possible
        if (i == 0) {
            for (j in 0 until grid.size) {
                if (grid[j].value == word[i]) {
                    cell = getCoordsFromIndex(j)
                    adjacentCells = getAdjacentCells(cell)
                    break
                }
            }
            // if first char was not in the grid use a random location for it
            if (cell == null) {
                var availableCells = grid.filter { c -> c.value == '-' }
                cell = getCoordsFromIndex(availableCells.random().idx)
                adjacentCells = getAdjacentCells(cell)
                usedCells.add(cell)
                result.add(Cell(idx = findCellIndex(cell), value = word[i]))
            }
        }

        else {
            for (adjacentCell in adjacentCells) {
                if (grid[findCellIndex(adjacentCell)].value == word[i]) {
                    cell = adjacentCell
                    adjacentCells = getAdjacentCells(cell).filter { c -> !usedCells.contains(c) }
                    usedCells.add(cell)
                    continuation = true
                    break
                }
            }
            if (continuation == false) {
                val availableCells = adjacentCells.filter { c -> grid[findCellIndex(c)].value == '-' }
                cell = availableCells.random()
                adjacentCells = getAdjacentCells(cell).filter { c -> !usedCells.contains(c) }
                usedCells.add(cell)
                result.add(Cell(idx = findCellIndex(cell), value = word[i]))
            }
        }

    }
    return result
}
