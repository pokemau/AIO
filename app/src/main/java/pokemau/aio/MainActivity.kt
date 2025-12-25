package pokemau.aio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pokemau.aio.ui.theme.AIOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIOTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var activeNumber by remember { mutableIntStateOf(1) }
                    var isEditing by remember { mutableStateOf(true) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center
                    ) {
                        SudokuBoard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            activeNumber = activeNumber,
                            isEditing = isEditing
                        )
                        ToggleEditing(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            isEditing = isEditing,
                            onToggle = { isEditing = it }
                        )
                        Numbers(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            activeNumber = activeNumber,
                            onNumberSelected = { activeNumber = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuBoard(modifier: Modifier = Modifier, activeNumber: Int, isEditing: Boolean) {
    val board = remember {
        mutableStateListOf(
            mutableStateListOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            mutableStateListOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            mutableStateListOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            mutableStateListOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            mutableStateListOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            mutableStateListOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            mutableStateListOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            mutableStateListOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            mutableStateListOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
    }

    val givenCells = remember {
        buildSet {
            board.forEachIndexed { row, rowList ->
                rowList.forEachIndexed { col, value ->
                    if (value != 0) add(Pair(row, col))
                }
            }
        }
    }

    var activeCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .border(3.dp, Color.Black)
    ) {
        for (row in 0 until 9) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                for (col in 0 until 9) {
                    val cellPosition = Pair(row, col)
                    val isGivenCell = givenCells.contains(cellPosition)
                    SudokuCell(
                        value = board[row][col],
                        row = row,
                        col = col,
                        modifier = Modifier.weight(1f),
                        isSelected = activeCell == Pair(row, col),
                        isGiven = isGivenCell,
                        onCellClick = {
                            activeCell = Pair(row, col)

                            if (isEditing && !isGivenCell) {
                                board[row][col] = activeNumber
                            }
                        }

                    )
                }
            }
        }
    }
}

@Composable
fun SudokuCell(
    value: Int,
    row: Int,
    col: Int,
    isSelected: Boolean,
    isGiven: Boolean,
    onCellClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val bgColor = if (isSelected) Color(0xFFCAE6FF) else Color.White

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(bgColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCellClick() }
            .drawBehind {
                val thickBorder = 3.dp.toPx()
                val thinBorder = 1.dp.toPx()

                val topWidth = if (row % 3 == 0 && row > 0) thickBorder else thinBorder
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = topWidth
                )

                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = thinBorder
                )

                val leftWidth = if (col % 3 == 0 && col > 0) thickBorder else thinBorder
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = leftWidth
                )

                drawLine(
                    color = Color.Black,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = thinBorder
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGiven) Color.Black else Color.Blue
            )
        }
    }
}

@Composable
fun Numbers(
    modifier: Modifier = Modifier, activeNumber: Int,
    onNumberSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (number in 1..9) {
            NumberButton(
                number = number,
                isActive = number == activeNumber,
                onClick = { onNumberSelected(number) }
            )
        }
    }
}

@Composable
fun RowScope.NumberButton(number: Int, isActive: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isActive) Color.Blue else Color.LightGray
    val textColor = if (isActive) Color.White else Color.Black

    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .background(backgroundColor)
            .clickable { onClick() }
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun ToggleEditing(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (isEditing) Color(0xFF2196F3) else Color.LightGray,
                    shape = CircleShape
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onToggle(!isEditing) }
                .border(2.dp, Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = if (isEditing) "Editing Mode" else "Notes Mode",
                tint = if (isEditing) Color.White else Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
