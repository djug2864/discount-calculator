// パッケージ宣言
package com.example.discount

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import kotlin.math.floor

// メインのアクティビティ（画面本体）
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 各種設定の状態管理（テーマ、端数処理、切り上げ/切り捨て）
            var isDarkMode by remember { mutableStateOf(false) }
            var isRoundingEnabled by remember { mutableStateOf(true) }
            var isCeil by remember { mutableStateOf(false) }

            MaterialTheme(
                colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        isDarkMode = isDarkMode,
                        onDarkModeChange = { isDarkMode = it },
                        isRoundingEnabled = isRoundingEnabled,
                        onRoundingEnabledChange = { isRoundingEnabled = it },
                        isCeil = isCeil,
                        onCeilChange = { isCeil = it }
                    )
                }
            }
        }
    }
}

// 画面のレイアウトと処理
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isRoundingEnabled: Boolean,
    onRoundingEnabledChange: (Boolean) -> Unit,
    isCeil: Boolean,
    onCeilChange: (Boolean) -> Unit
) {
    var inputPrice by remember { mutableStateOf("") }
    var resultPrice by remember { mutableStateOf<Int?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // 画面移動に必要なContext（コンテキスト）を取得
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5割引計算機") },
                actions = {
                    // 右上の設定ボタン
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 金額表示エリア
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = if (inputPrice.isEmpty()) "0 円" else "$inputPrice 円",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // キーパッド部分
            val keys = listOf("7", "8", "9", "4", "5", "6", "1", "2", "3", "C", "0", "5割引")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(keys) { key ->
                    Button(
                        onClick = {
                            when (key) {
                                "C" -> inputPrice = "" // 入力クリア
                                "5割引" -> { // 割引計算
                                    val price = inputPrice.toDoubleOrNull()
                                    if (price != null) {
                                        val rawDiscounted = price * 0.5
                                        val finalPrice = if (isRoundingEnabled) {
                                            if (isCeil) ceil(rawDiscounted).toInt()
                                            else floor(rawDiscounted).toInt()
                                        } else {
                                            rawDiscounted.toInt()
                                        }
                                        resultPrice = finalPrice
                                    }
                                }
                                else -> {
                                    if (inputPrice.length < 8) {
                                        inputPrice += key
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(),
                        colors = if (key == "5割引") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        else if (key == "C") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Text(text = key, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 計算結果ダイアログ
    resultPrice?.let { price ->
        AlertDialog(
            onDismissRequest = { resultPrice = null },
            title = { Text("割引後の値段") },
            text = {
                Text(
                    text = "$price 円",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {
                TextButton(onClick = { resultPrice = null }) {
                    Text("閉じる")
                }
            }
        )
    }

    // 設定モーダル
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("設定") },
            text = {
                Column {
                    // ダークモード切り替え
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ダークモード")
                        Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // 端数処理ON/OFF
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("端数処理を有効化")
                        Switch(checked = isRoundingEnabled, onCheckedChange = onRoundingEnabledChange)
                    }
                    
                    // 切り上げ/切り捨て切替
                    if (isRoundingEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isCeil) "モード: 切り上げ" else "モード: 切り捨て")
                            Switch(checked = isCeil, onCheckedChange = onCeilChange)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // アプリ情報画面への移動ボタン（追加）
                    OutlinedButton(
                        onClick = {
                            showSettingsDialog = false
                            // AppInfoActivityへ移動
                            val intent = Intent(context, AppInfoActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("アプリ情報を見る")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("閉じる")
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isRoundingEnabled: Boolean,
    onRoundingEnabledChange: (Boolean) -> Unit,
    isCeil: Boolean,
    onCeilChange: (Boolean) -> Unit
) {
    var inputPrice by remember { mutableStateOf("") }
    var resultPrice by remember { mutableStateOf<Int?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5割引計算機") },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ディスプレイ部分
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = if (inputPrice.isEmpty()) "0 円" else "$inputPrice 円",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // キーパッド部分
            val keys = listOf("7", "8", "9", "4", "5", "6", "1", "2", "3", "C", "0", "5割引")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(keys) { key ->
                    Button(
                        onClick = {
                            when (key) {
                                "C" -> inputPrice = "" // 入力クリア
                                "5割引" -> { // 割引計算
                                    val price = inputPrice.toDoubleOrNull()
                                    if (price != null) {
                                        val rawDiscounted = price * 0.5
                                        val finalPrice = if (isRoundingEnabled) {
                                            if (isCeil) ceil(rawDiscounted).toInt()
                                            else floor(rawDiscounted).toInt()
                                        } else {
                                            rawDiscounted.toInt()
                                        }
                                        resultPrice = finalPrice
                                    }
                                }
                                else -> {
                                    if (inputPrice.length < 8) {
                                        inputPrice += key
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(),
                        colors = if (key == "5割引") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        else if (key == "C") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Text(text = key, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 計算結果表示モーダル
    resultPrice?.let { price ->
        AlertDialog(
            onDismissRequest = { resultPrice = null },
            title = { Text("割引後の値段") },
            text = {
                Text(
                    text = "$price 円",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {
                TextButton(onClick = { resultPrice = null }) {
                    Text("閉じる")
                }
            }
        )
    }

    // 設定画面モーダル
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("設定") },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ダークモード")
                        Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("端数処理を有効化")
                        Switch(checked = isRoundingEnabled, onCheckedChange = onRoundingEnabledChange)
                    }
                    if (isRoundingEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isCeil) "モード: 切り上げ" else "モード: 切り捨て")
                            Switch(checked = isCeil, onCheckedChange = onCeilChange)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("完了")
                }
            }
        )
    }
}
