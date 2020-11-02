package tictactoe

import java.lang.Exception
import java.util.*
import kotlin.math.abs

// 3*(1 - 1) + (1 - 1) = 0
// 3*(1 - 1) + (2 - 1) = 1
// 3*(1 - 1) + (3 - 1) = 2
// 3*(2 - 1) + (1 - 1) = 3
// 3*(2 - 1) + (2 - 1) = 4
fun getPosition(
    gameState: CharArray,
    dimension: Int
): Int {

    val scanner = Scanner(System.`in`)

    while (true) {

        print("Enter the coordinates: ")

        try {

            var input = scanner.nextLine().split(" ")
            var xPoint = input[0].toInt()
            var yPoint = input[1].toInt()

            if (isOutOfBound(xPoint, yPoint, dimension)) {
                throw Exception("Coordinates should be from 1 to $dimension!")
            }

            var index = dimension * (xPoint - 1) + (yPoint - 1)

            if (isOccupied(gameState[index])) {
                throw Exception("This cell is occupied! Choose another one!")
            }

            return index

        } catch (e: NumberFormatException) {
            println("You should enter numbers!")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun isOutOfBound(xPoint: Int, yPoint: Int, dimension: Int) = xPoint > dimension || yPoint > dimension
fun isOccupied(item: Char) = item == 'X' || item == 'O'

fun drawTheGameDesk(
    gameState: CharArray,
    dimension: Int
) {

    var output: String

    // Print the matrix
    println("-------------")
    for (i in gameState.indices) {
        when {
            i % dimension == 0 -> print("| ${gameState[i]} ")
            (i + 1) % dimension == 0 -> println("${gameState[i]} |")
            else -> print("${gameState[i]} ")
        }
    }
    println("-------------")
}

fun isCompleteRow(
    gameState: CharArray,
    start: Int,
    end: Int,
    step: Int
): Boolean {

    for (k in start..end step step) {
        if (gameState[start] != gameState[k] || gameState[k] == ' ') break
        if (k == end) {
            return true
        }
    }

    return false
}

fun explore(
    gameState: CharArray,
    index: Int,
    dimension: Int
): Boolean {

    var found: Boolean = false

    //we are on the diagonal
    if (index % (dimension + 1) == 0) {
        if (isCompleteRow(
                gameState,
                0,
                (dimension + 1) * (dimension - 1),
                dimension + 1
            )
        ) found = true
    }

    //we are on the anti-diagonal
    if (index != 0 && index % (dimension - 1) == 0) {
        if (isCompleteRow(
                gameState,
                dimension - 1,
                dimension * (dimension - 1),
                dimension - 1
            )
        ) found = true
    }

    //horizontal
    var row = index / dimension
    if (isCompleteRow(
            gameState,
            row * dimension,
            row * dimension + dimension - 1,
            1
        )
    ) found = true

    //vertical
    var column = index % dimension
    if (isCompleteRow(
            gameState,
            column,
            column + dimension * (dimension - 1),
            dimension
        )
    ) found = true

    return found
}

fun main() {

    val dimension = 5
    val gameState = " ".repeat(dimension * dimension).toCharArray()
    var lastMove: Char = 'O'
    var hasWinner = false
    var freeCells = gameState.size
    var index = 0

    drawTheGameDesk(gameState, dimension)

    do {

        var index = getPosition(gameState, dimension)
        freeCells--

        gameState[index] = when (lastMove) {
            'X' -> 'O'
            else -> 'X'
        }

        lastMove = gameState[index]
        hasWinner = explore(gameState, index, dimension)

        drawTheGameDesk(gameState, dimension)

    } while (!hasWinner && freeCells != 0)

    if (hasWinner) {
        println("${gameState[index]} wins")
    } else {
        println("Draw")
    }

}