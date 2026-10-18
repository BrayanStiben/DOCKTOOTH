package com.example.docktooth.features.time_pass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.docktooth.core.component.DocToothTopBar
import com.example.docktooth.core.theme.PrimaryBlue
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TimePassScreen(
    onBackClick: () -> Unit
) {
    var activeGameId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            DocToothTopBar(
                title = "Pasar el Tiempo",
                onBackClick = onBackClick,
                showLogo = true
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE3F1F8)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Top Row (2 Games)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    GameAppIcon(
                        title = "Mouth Match 3",
                        onClick = { activeGameId = "match3" }
                    ) {
                        MouthMatch3AppIcon()
                    }

                    GameAppIcon(
                        title = "Dental Whack",
                        onClick = { activeGameId = "whack" }
                    ) {
                        DentalWhackAppIcon()
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bottom Row (2 Games)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    GameAppIcon(
                        title = "2048 Dental",
                        onClick = { activeGameId = "2048" }
                    ) {
                        Muela2048AppIcon()
                    }

                    GameAppIcon(
                        title = "Trivia Dental",
                        onClick = { activeGameId = "trivia" }
                    ) {
                        TriviaDentalAppIcon()
                    }
                }
            }
        }
    }

    // Minigame Full Screen Windows Handlers
    when (activeGameId) {
        "match3" -> MouthMatch3FullScreenWindow(onDismiss = { activeGameId = null })
        "whack" -> DentalWhackFullScreenWindow(onDismiss = { activeGameId = null })
        "2048" -> Muela2048FullScreenWindow(onDismiss = { activeGameId = null })
        "trivia" -> TriviaDentalFullScreenWindow(onDismiss = { activeGameId = null })
    }
}

// Squircle App Icon Button Container
@Composable
fun GameAppIcon(
    title: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(105.dp)
            .clickable { onClick() }
    ) {
        Card(
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.size(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                content()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

// 1. Mouth Match 3 Icon Artwork
@Composable
fun MouthMatch3AppIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2F7DC)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("🍎", fontSize = 15.sp)
                Text("🦷", fontSize = 15.sp)
                Text("🪥", fontSize = 15.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("🍎", fontSize = 15.sp)
                Text("✨", fontSize = 15.sp)
                Text("🪥", fontSize = 15.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("🪥", fontSize = 15.sp)
                Text("🦷", fontSize = 15.sp)
                Text("🍎", fontSize = 15.sp)
            }
        }
    }
}

// 2. Dental Whack Icon Artwork
@Composable
fun DentalWhackAppIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE2D1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🔨", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("🦷", fontSize = 16.sp)
                Text("👾", fontSize = 22.sp)
                Text("🦷", fontSize = 16.sp)
            }
        }
    }
}

// 3. 2048 Dental Icon Artwork
@Composable
fun Muela2048AppIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F1F8)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Surface(color = Color(0xFFE3F1F8), shape = RoundedCornerShape(4.dp)) { Text("2", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) }
                Surface(color = Color(0xFFA6D0E7), shape = RoundedCornerShape(4.dp)) { Text("4", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Surface(color = Color(0xFF6D98BA), shape = RoundedCornerShape(4.dp)) { Text("16", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp)) }
                Surface(color = Color(0xFF1A4050), shape = RoundedCornerShape(4.dp)) { Text("2048", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 2.dp, vertical = 3.dp)) }
            }
        }
    }
}

// 4. Trivia Dental Icon Artwork
@Composable
fun TriviaDentalAppIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1ECFB)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🖐️", fontSize = 11.sp)
                Text("❤️", fontSize = 11.sp)
                Text("🛡️", fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text("❓", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Spacer(modifier = Modifier.height(2.dp))
            Surface(color = PrimaryBlue, shape = RoundedCornerShape(4.dp), modifier = Modifier.width(44.dp).height(5.dp)) {}
        }
    }
}

// ==========================================
// 1. FULL-SCREEN WINDOW: Mouth Match 3
// ==========================================
@Composable
fun MouthMatch3FullScreenWindow(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val itemsList = remember { listOf("🪥", "🍎", "🦷", "🧴", "✨") }
        var board by remember { mutableStateOf(List(16) { itemsList.random() }) }
        var score by remember { mutableIntStateOf(0) }
        var cleanliness by remember { mutableIntStateOf(20) }
        var currentLevel by remember { mutableIntStateOf(1) }
        var selectedIndex by remember { mutableStateOf<Int?>(null) }
        var statusMsg by remember { mutableStateOf("Toca dos casillas para intercambiarlas y conectar 3 en línea.") }

        fun checkAndClearMatches(currentBoard: List<String>): Pair<List<String>, Boolean> {
            val newBoard = currentBoard.toMutableList()
            val toClear = mutableSetOf<Int>()

            // Check horizontal 3-in-a-row
            for (row in 0 until 4) {
                for (col in 0..1) {
                    val i1 = row * 4 + col
                    val i2 = row * 4 + col + 1
                    val i3 = row * 4 + col + 2
                    if (newBoard[i1] == newBoard[i2] && newBoard[i2] == newBoard[i3]) {
                        toClear.add(i1); toClear.add(i2); toClear.add(i3)
                    }
                }
            }

            // Check vertical 3-in-a-row
            for (col in 0 until 4) {
                for (row in 0..1) {
                    val i1 = row * 4 + col
                    val i2 = (row + 1) * 4 + col
                    val i3 = (row + 2) * 4 + col
                    if (newBoard[i1] == newBoard[i2] && newBoard[i2] == newBoard[i3]) {
                        toClear.add(i1); toClear.add(i2); toClear.add(i3)
                    }
                }
            }

            val matched = toClear.isNotEmpty()
            if (matched) {
                toClear.forEach { idx ->
                    newBoard[idx] = itemsList.random()
                }
            }
            return newBoard to matched
        }

        fun swapAndMatch(index1: Int, index2: Int) {
            val swappedBoard = board.toMutableList()
            val temp = swappedBoard[index1]
            swappedBoard[index1] = swappedBoard[index2]
            swappedBoard[index2] = temp

            val (clearedBoard, hasMatch) = checkAndClearMatches(swappedBoard)
            if (hasMatch) {
                score += 50
                cleanliness += 15
                if (cleanliness >= 100) {
                    currentLevel++
                    cleanliness = 20
                    statusMsg = "🎉 ¡Nivel completado! Pasas al Nivel $currentLevel (+50 pts)"
                } else {
                    statusMsg = "✨ ¡Conexión limpia realizada! 3 en línea (+50 pts)"
                }
                board = clearedBoard
            } else {
                score += 10
                statusMsg = "🔄 Casillas intercambiadas. ¡Busca conectar 3 iguales!"
                board = swappedBoard
            }
            selectedIndex = null
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                Surface(
                    color = PrimaryBlue,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mouth Match 3 - Nivel $currentLevel", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        TextButton(onClick = onDismiss) {
                            Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Scrollable Game Container with Generous Bottom Padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Metrics
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Puntos: $score pts", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                Text("Nivel $currentLevel (Limpieza: ${cleanliness.coerceAtMost(100)}%)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (cleanliness.coerceAtMost(100)) / 100f },
                                color = PrimaryBlue,
                                trackColor = Color(0xFFE2F1F8),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFFFCE7F3),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = statusMsg,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFBE185D),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    // Match 3 Board
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                            ) {
                                items(board.size) { index ->
                                    val isSel = selectedIndex == index
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSel) Color(0xFFFED7AA) else Color(0xFFFCE7F3)
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (isSel) 6.dp else 2.dp
                                        ),
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clickable {
                                                if (selectedIndex == null) {
                                                    selectedIndex = index
                                                } else if (selectedIndex == index) {
                                                    selectedIndex = null
                                                } else {
                                                    swapAndMatch(selectedIndex!!, index)
                                                }
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = board[index], fontSize = 32.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            board = List(16) { itemsList.random() }
                            statusMsg = "🎲 ¡Tablero reorganizado con éxito!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mezclar Tablero", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. FULL-SCREEN WINDOW: Dental Whack
// ==========================================
@Composable
fun DentalWhackFullScreenWindow(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var score by remember { mutableIntStateOf(0) }
        var activeIndex by remember { mutableIntStateOf(Random.nextInt(9)) }
        var toothHealth by remember { mutableIntStateOf(100) }
        var germsEliminated by remember { mutableIntStateOf(0) }
        var timerSeconds by remember { mutableIntStateOf(45) }

        LaunchedEffect(timerSeconds) {
            if (timerSeconds > 0 && toothHealth > 0) {
                delay(1000)
                timerSeconds--
                activeIndex = Random.nextInt(9)
                toothHealth = (toothHealth - 2).coerceAtLeast(0)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                Surface(
                    color = PrimaryBlue,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dental Whack!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        TextButton(onClick = onDismiss) {
                            Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Scrollable Game Container with Generous Bottom Padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Metrics
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("💥 Puntos: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                Text("⏳ Tiempo: ${timerSeconds}s", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                Text("👾 Eliminados: $germsEliminated", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Salud Dental: $toothHealth%", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                LinearProgressIndicator(
                                    progress = { toothHealth / 100f },
                                    color = Color(0xFF16A34A),
                                    trackColor = Color(0xFFFEE2E2),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                )
                            }
                        }
                    }

                    Text(
                        text = "🔨 ¡Toca rápidamente las bacterias 👾 antes de que infecten los dientes!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    // 3x3 Whack Grid
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                            ) {
                                items(9) { index ->
                                    val hasGerm = index == activeIndex && timerSeconds > 0 && toothHealth > 0
                                    Card(
                                        shape = RoundedCornerShape(18.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (hasGerm) Color(0xFFFEE2E2) else Color(0xFFE2F1F8)
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (hasGerm) 6.dp else 2.dp
                                        ),
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clickable {
                                                if (hasGerm) {
                                                    score += 10
                                                    germsEliminated++
                                                    toothHealth = (toothHealth + 5).coerceAtMost(100)
                                                    activeIndex = Random.nextInt(9)
                                                }
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (hasGerm) "👾" else "🦷",
                                                fontSize = 38.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (timerSeconds == 0 || toothHealth == 0) {
                        Button(
                            onClick = {
                                score = 0
                                germsEliminated = 0
                                toothHealth = 100
                                timerSeconds = 45
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("🔄 Volver a Jugar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("Terminar Juego", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. FULL-SCREEN WINDOW: 2048 Dental
// ==========================================
@Composable
fun Muela2048FullScreenWindow(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var score by remember { mutableIntStateOf(0) }
        var isGameWon by remember { mutableStateOf(false) }
        var isGameOver by remember { mutableStateOf(false) }

        var grid by remember {
            mutableStateOf(run {
                val g = MutableList(16) { 0 }
                val i1 = g.indices.random()
                g[i1] = if (Random.nextFloat() < 0.9f) 2 else 4
                val i2 = g.indices.filter { it != i1 }.random()
                g[i2] = if (Random.nextFloat() < 0.9f) 2 else 4
                g.toList()
            })
        }

        fun restartGame() {
            val g = MutableList(16) { 0 }
            val i1 = g.indices.random()
            g[i1] = if (Random.nextFloat() < 0.9f) 2 else 4
            val i2 = g.indices.filter { it != i1 }.random()
            g[i2] = if (Random.nextFloat() < 0.9f) 2 else 4
            grid = g.toList()
            score = 0
            isGameWon = false
            isGameOver = false
        }

        fun slideAndMergeRow(row: List<Int>): Pair<List<Int>, Int> {
            val nonZeros = row.filter { it != 0 }
            val result = mutableListOf<Int>()
            var gained = 0
            var i = 0
            while (i < nonZeros.size) {
                if (i < nonZeros.size - 1 && nonZeros[i] == nonZeros[i + 1]) {
                    val valMerged = nonZeros[i] * 2
                    result.add(valMerged)
                    gained += valMerged
                    i += 2
                } else {
                    result.add(nonZeros[i])
                    i += 1
                }
            }
            while (result.size < 4) result.add(0)
            return result to gained
        }

        fun checkStateAfterMove(newGrid: List<Int>, totalGained: Int) {
            if (newGrid != grid) {
                val mutableG = newGrid.toMutableList()
                val emptyIndices = mutableG.indices.filter { mutableG[it] == 0 }
                if (emptyIndices.isNotEmpty()) {
                    val spawnIdx = emptyIndices.random()
                    mutableG[spawnIdx] = if (Random.nextFloat() < 0.9f) 2 else 4
                }
                val finalG = mutableG.toList()
                grid = finalG
                score += totalGained

                if (finalG.contains(2048)) {
                    isGameWon = true
                } else {
                    // Check if game over (no zeros and no equal adjacent neighbors)
                    var canMove = finalG.contains(0)
                    if (!canMove) {
                        for (r in 0 until 4) {
                            for (c in 0 until 4) {
                                val curr = finalG[r * 4 + c]
                                if (c < 3 && curr == finalG[r * 4 + c + 1]) canMove = true
                                if (r < 3 && curr == finalG[(r + 1) * 4 + c]) canMove = true
                            }
                        }
                    }
                    if (!canMove) {
                        isGameOver = true
                    }
                }
            }
        }

        fun moveLeft() {
            if (isGameWon || isGameOver) return
            val newG = grid.toMutableList()
            var totalG = 0
            for (r in 0 until 4) {
                val row = newG.subList(r * 4, r * 4 + 4)
                val (slid, gained) = slideAndMergeRow(row)
                totalG += gained
                for (c in 0 until 4) newG[r * 4 + c] = slid[c]
            }
            checkStateAfterMove(newG, totalG)
        }

        fun moveRight() {
            if (isGameWon || isGameOver) return
            val newG = grid.toMutableList()
            var totalG = 0
            for (r in 0 until 4) {
                val row = newG.subList(r * 4, r * 4 + 4).reversed()
                val (slid, gained) = slideAndMergeRow(row)
                totalG += gained
                val rev = slid.reversed()
                for (c in 0 until 4) newG[r * 4 + c] = rev[c]
            }
            checkStateAfterMove(newG, totalG)
        }

        fun moveUp() {
            if (isGameWon || isGameOver) return
            val newG = grid.toMutableList()
            var totalG = 0
            for (c in 0 until 4) {
                val col = listOf(grid[c], grid[c + 4], grid[c + 8], grid[c + 12])
                val (slid, gained) = slideAndMergeRow(col)
                totalG += gained
                for (r in 0 until 4) newG[r * 4 + c] = slid[r]
            }
            checkStateAfterMove(newG, totalG)
        }

        fun moveDown() {
            if (isGameWon || isGameOver) return
            val newG = grid.toMutableList()
            var totalG = 0
            for (c in 0 until 4) {
                val col = listOf(grid[c + 12], grid[c + 8], grid[c + 4], grid[c])
                val (slid, gained) = slideAndMergeRow(col)
                totalG += gained
                val rev = slid.reversed()
                for (r in 0 until 4) newG[(3 - r) * 4 + c] = rev[r]
            }
            checkStateAfterMove(newG, totalG)
        }

        fun getTileColors(value: Int): Pair<Color, Color> {
            return when (value) {
                0 -> Color(0xFFF1F5F9) to Color.Transparent
                2 -> Color(0xFFE3F1F8) to Color(0xFF0F172A)
                4 -> Color(0xFFA6D0E7) to Color(0xFF0F172A)
                8 -> Color(0xFF93BDD8) to Color(0xFF0F172A)
                16 -> Color(0xFF80ABC9) to Color(0xFF0F172A)
                32 -> Color(0xFF6D98BA) to Color(0xFFFFFFFF)
                64 -> Color(0xFF5A85A9) to Color(0xFFFFFFFF)
                128 -> Color(0xFF477F98) to Color(0xFFFFFFFF)
                256 -> Color(0xFF3B667C) to Color(0xFFFFFFFF)
                512 -> Color(0xFF295568) to Color(0xFFFFFFFF)
                1024 -> Color(0xFF224D61) to Color(0xFFFFFFFF)
                2048 -> Color(0xFF1A4050) to Color(0xFFFFFFFF)
                else -> Color(0xFF0F2937) to Color(0xFFFFFFFF)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                Surface(
                    color = PrimaryBlue,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("2048 Dental", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        TextButton(onClick = onDismiss) {
                            Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Scrollable Container
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Metrics
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Puntaje", fontSize = 12.sp, color = Color.Gray)
                                Text("$score pts", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }

                            Button(
                                onClick = { restartGame() },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reiniciar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Win / Game Over Banner
                    if (isGameWon) {
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🎉 ¡FELICIDADES!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("¡Alcanzaste el 2048 y completaste el juego!", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF166534), textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { restartGame() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D))
                                ) {
                                    Text("🔄 Jugar de nuevo", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else if (isGameOver) {
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("💥 ¡FIN DEL JUEGO!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("No hay más movimientos posibles.", fontSize = 13.sp, color = Color(0xFF991B1B))
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { restartGame() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                                ) {
                                    Text("🔄 Intentar de nuevo", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // 4x4 Grid Board
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A4050)),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                items(grid.size) { i ->
                                    val valNum = grid[i]
                                    val (bgColor, textColor) = getTileColors(valNum)

                                    Card(
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = bgColor),
                                        modifier = Modifier.aspectRatio(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (valNum != 0) {
                                                Text(
                                                    text = "$valNum",
                                                    fontSize = when {
                                                        valNum >= 1000 -> 15.sp
                                                        valNum >= 100 -> 18.sp
                                                        valNum >= 10 -> 22.sp
                                                        else -> 24.sp
                                                    },
                                                    fontWeight = FontWeight.Bold,
                                                    color = textColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Direction Controls
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { moveUp() },
                            enabled = !isGameWon && !isGameOver,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text("⬆️ Arriba", fontWeight = FontWeight.Bold)
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { moveLeft() },
                                enabled = !isGameWon && !isGameOver,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("⬅️ Izquierda", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { moveRight() },
                                enabled = !isGameWon && !isGameOver,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("➡️ Derecha", fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { moveDown() },
                            enabled = !isGameWon && !isGameOver,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text("⬇️ Abajo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. FULL-SCREEN WINDOW: Trivia Dental
// ==========================================
@Composable
fun TriviaDentalFullScreenWindow(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val questions = remember {
            listOf(
                Triple("¿Cuántas veces al día se recomienda cepillarse los dientes?", listOf("1 vez al día", "Mínimo 2-3 veces al día", "Solo por la noche", "Cada 3 días"), 1),
                Triple("¿Cada cuánto tiempo debes cambiar tu cepillo de dientes?", listOf("Cada semana", "Cada 3 a 4 meses", "Una vez al año", "Nunca"), 1),
                Triple("¿Qué elemento es fundamental para limpiar entre los dientes?", listOf("Hilo dental", "Palillo de madera", "Agua sola", "Enjuague fuerte"), 0),
                Triple("¿Cuál es el principal enemigo del esmalte dental?", listOf("El agua pura", "El azúcar y los ácidos", "Las manzanas", "La pasta fluorada"), 1),
                Triple("¿Con qué frecuencia se recomienda visitar al odontólogo?", listOf("Solo cuando duele", "Mínimo cada 6 meses", "Cada 5 años", "Nunca"), 1),
                Triple("¿Qué función principal cumple el flúor en los dientes?", listOf("Blanquear al instante", "Fortalecer el esmalte y prevenir caries", "Endulzar la boca", "Ninguna"), 1)
            )
        }

        var currentIndex by remember { mutableIntStateOf(0) }
        var score by remember { mutableIntStateOf(0) }
        var selectedOption by remember { mutableStateOf<Int?>(null) }
        var isAnswered by remember { mutableStateOf(false) }

        val current = questions[currentIndex]

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                Surface(
                    color = PrimaryBlue,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Trivia Dental", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        TextButton(onClick = onDismiss) {
                            Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Scrollable Game Container with Generous Bottom Padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Progress
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pregunta ${currentIndex + 1} de ${questions.size}", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("Puntaje: $score pts", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (currentIndex + 1) / questions.size.toFloat() },
                                color = PrimaryBlue,
                                trackColor = Color(0xFFE2F1F8),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }

                    // Question Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = current.first,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            current.second.forEachIndexed { index, optionText ->
                                val isCorrectOption = index == current.third
                                val isSelected = selectedOption == index

                                val btnColor = when {
                                    !isAnswered -> Color(0xFFF1F5F9)
                                    isCorrectOption -> Color(0xFFDCFCE7)
                                    isSelected -> Color(0xFFFEE2E2)
                                    else -> Color(0xFFF1F5F9)
                                }

                                Surface(
                                    color = btnColor,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable(enabled = !isAnswered) {
                                            selectedOption = index
                                            isAnswered = true
                                            if (index == current.third) {
                                                score += 20
                                            }
                                        }
                                ) {
                                    Text(
                                        text = optionText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Action
                    if (isAnswered) {
                        Button(
                            onClick = {
                                if (currentIndex < questions.size - 1) {
                                    currentIndex++
                                    isAnswered = false
                                    selectedOption = null
                                } else {
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                if (currentIndex < questions.size - 1) "Siguiente Pregunta ➔" else "🎓 Finalizar Trivia ($score pts)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Surface(
                            color = Color(0xFFE0E7FF),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 Selecciona una opción para responder.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimaryBlue,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
